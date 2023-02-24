package com.payment.transaction.component;

import com.payment.customers.entity.AccountDomain;
import com.payment.customers.entity.AccountPurseDomain;
import com.payment.customers.entity.BOD;
import com.payment.customers.model.Payment;
import com.payment.customers.repository.AccountRepository;
import com.payment.customers.repository.BODRepository;
import com.payment.exception.InvalidPaymentRequestException;
import com.payment.exception.NotFoundException;
import com.payment.payerin.request.PaymentRequest;
import com.payment.transaction.TransactionUtil;
import com.payment.transaction.ledger.AccountLedgers;
import com.payment.transaction.model.TransactionLog;
import com.payment.payerin.request.PreRequest;
import com.payment.transaction.model.TransactionStatus;
import com.payment.transaction.repository.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TransactionLogDBProcess {
    @Autowired
    private PaymentProcess paymentProcess;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PaymentLogRepository paymentLogRepository;

    @Autowired
    AccountLedgers accountLedgers;

    @Transactional
    public Long checkAccountBalanceAndStatus(PreRequest preRequest, Long clientId) {
        AccountDomain accountDomain = null;
        AccountPurseDomain accountPurseDomain = null;
        TransactionLog transactionLog = null;
        Long paymentId = null;
        try {
            //From account
            paymentId = TransactionUtil.generateRandom(11);
            accountDomain = paymentProcess.getAccount(preRequest.getFrmAccountNo(), clientId);
            accountPurseDomain = paymentProcess.getAccountPurse(preRequest.getFrmCurrency(), accountDomain);
            //Target Account
            AccountDomain targetAccount = paymentProcess.getAccount(preRequest.getToAccount(), clientId);
            AccountPurseDomain targetPurse = paymentProcess.getAccountPurse(preRequest.getFrmCurrency(), accountDomain);

            //Validation
            TransactionUtil.domesticCurrencyCheck(accountPurseDomain.getCurrency(), targetPurse.getCurrency());
            TransactionUtil.checkAccountBalance(targetPurse.getAvailableBalance(), preRequest.getTransAmt());
            TransactionUtil.checkActStatus(accountDomain.getStatus(), "From account status is not in active");
            TransactionUtil.checkActStatus(targetAccount.getStatus(), "To account status is not in active");

            String hash = TransactionUtil.generateHash(TransactionUtil.hashValues(preRequest.getTransAmt(), preRequest.getFrmAccountNo(), preRequest.getToAccount(), "PayIn", preRequest.getFrmCurrency()));
            transactionLog = persistPayInRequest(preRequest, "PayIn", accountDomain, accountPurseDomain, "", TransactionStatus.INITIALIZE, hash, paymentId, "D",false);

        } catch (InvalidPaymentRequestException | NotFoundException exception) {
            exception.printStackTrace();
            transactionLog = persistPayInRequest(preRequest, "PayIn", accountDomain, accountPurseDomain, exception.getMessage(), TransactionStatus.ERROR, "", paymentId, "D",false);
            System.out.println("log inserted successfully ");

        } catch (Exception e) {
            e.printStackTrace();
            transactionLog = persistPayInRequest(preRequest, "PayIn", accountDomain, accountPurseDomain, e.getMessage(), TransactionStatus.ERROR, "", paymentId, "D",false);


        }
        paymentLogRepository.save(transactionLog);
        return paymentId;

    }

    @Transactional
    public void doPayment(Long clientId,PaymentRequest paymentRequest, TransactionLog transactionLog) {
        AccountDomain accountDomain = null;
        AccountPurseDomain accountPurseDomain = null;
        Long paymentId = null;
        List<TransactionLog> transactionLogs= new ArrayList<>(2);
        //Target Account
        List<BOD> bods= new ArrayList<>(2);


        try {
            //From account

            paymentId=transactionLog.getPaymentId();

            TransactionUtil.validateHashValue(transactionLog.getPaymentHash(), paymentRequest, "PayIn");
            accountDomain = paymentProcess.getAccount(paymentRequest.getFrmAccountNo(), clientId);
            accountPurseDomain = paymentProcess.getAccountPurse(paymentRequest.getFrmCurrency(), accountDomain);
            Double debitorOpeningBalance=accountPurseDomain.getAvailableBalance();
            //Target Account
            AccountDomain targetAccount = paymentProcess.getAccount(paymentRequest.getToAccount(), clientId);
            AccountPurseDomain targetPurse = paymentProcess.getAccountPurse(paymentRequest.getFrmCurrency(), targetAccount);
            Double creditorOpeningBalance=targetPurse.getAvailableBalance();

            TransactionUtil.checkAccountBalance(targetPurse.getAvailableBalance(), paymentRequest.getTransAmt());
            TransactionUtil.checkActStatus(accountDomain.getStatus(), "From account status is not in active");
            TransactionUtil.checkActStatus(targetAccount.getStatus(), "To account status is not in active");
            //Amount debiting
            Double atomicLongDebit = new Double(-paymentRequest.getTransAmt());
            //Debiting source account
            AccountPurseDomain debitPurse=paymentProcess.updateBalance(paymentRequest.getFrmAccountNo(), paymentRequest.getFrmCurrency(), atomicLongDebit, clientId);
            Double debitorClosingBalance=debitPurse.getAvailableBalance();

            //Crediting to account
            Double atomicLongCredit = new Double(paymentRequest.getTransAmt());
            AccountPurseDomain targetUpdatedPurseDomain=paymentProcess.updateBalance(paymentRequest.getToAccount(), paymentRequest.getFrmCurrency(), atomicLongCredit, clientId);
            Double creditorClosingBalance=targetUpdatedPurseDomain.getAvailableBalance();

            transactionLog.setAvailableBalance(debitPurse.getAvailableBalance());
            transactionLog.setLedgerBalance(debitPurse.getLedgerBalance());
            transactionLog.setStatus(TransactionStatus.SUCCESS);
            transactionLog.setUpdateAt(LocalDate.now());
            //Credit Entry:
            TransactionLog transactionLogCR= TransactionLog.builder()
                    .uuid(UUID.randomUUID().toString())
                    .createdAt(LocalDate.now())
                    .rrn(paymentRequest.getRrn())
                    .paymentId(paymentRequest.getPaymentId())
                    .paymentType("C")
                    .transCode("PayIn")
                    .purseNumber(targetPurse.getPurseNumber())
                    .clientId(targetAccount.getClientId())
                    .requestId(paymentRequest.getRequestId())
                    .frmCurrency(paymentRequest.getFrmCurrency())
                    .status(TransactionStatus.SUCCESS)
                    .accountNumber(paymentRequest.getToAccount())
                    .availableBalance(targetUpdatedPurseDomain.getAvailableBalance())
                    .ledgerBalance(targetUpdatedPurseDomain.getLedgerBalance())
                    .ledgerBalance(targetUpdatedPurseDomain.getLedgerBalance())
                    .build();
            transactionLogs.add(transactionLogCR);
            transactionLogs.add(transactionLog);
            //debitEntry
            BOD dbBOD=setBOD(transactionLog, debitorOpeningBalance,debitorClosingBalance,null,paymentRequest.getTransAmt());
            //CreditEntryEntry
            BOD crBOD= setBOD(transactionLogCR, creditorOpeningBalance,creditorClosingBalance,paymentRequest.getFrmAccountNo(),paymentRequest.getTransAmt());
            bods.add(crBOD);
            bods.add(dbBOD);
            accountLedgers.updateAccountLedger(bods);
        } catch (InvalidPaymentRequestException | NotFoundException exception) {
            exception.printStackTrace();
            transactionLog.setUpdateAt(LocalDate.now());
            transactionLog.setStatus(TransactionStatus.ERROR);
            transactionLog.setErrorDescription(exception.getMessage());
            transactionLogs.add(transactionLog);
            System.out.println("log inserted successfully ");


        } /*catch (Exception e) {
            e.printStackTrace();
            transactionLog = persistPayInRequest(paymentRequest, "PayIn", accountDomain, accountPurseDomain, e.getMessage(), TransactionStatus.ERROR, "", paymentId, "D");


        }*/
        paymentLogRepository.saveAll(transactionLogs);


    }



    public Payment getPayment(Long paymentId, String rrn, String accountNumber, TransactionLog data) {
        if (Objects.isNull(data)) {
             data = getTransactionLogByPaymentIdAndAccountNumberAndRrn(accountNumber, paymentId, rrn);
        }

        return Payment.builder().
                uuid(data.getUuid())
                .status(data.getStatus())
                .requestId(data.getRequestId())
                .description(data.getDescription())
                .availableBalance(data.getAvailableBalance())
                .programId(data.getProgramId())
                .clientId(data.getClientId())
                .currency(data.getFrmCurrency())
                //.ledgerBalance(data.getLedgerBalance())
                .purseNumber(data.getPurseNumber())
                .paymentId(data.getPaymentId())
                .paymentType(data.getPaymentType())
                .description(data.getErrorDescription())
                .accountNumber(data.getAccountNumber())
                .build();
    }

    private TransactionLog getTransactionLogByPaymentIdAndAccountNumberAndRrn(String accountNumber, Long paymentId, String rrn) {
        Optional<TransactionLog> transactionLog = paymentLogRepository.findByAccountNumberAndPaymentIdAndRrn(accountNumber, paymentId, rrn);

        if (transactionLog.isEmpty()) {
            throw new NotFoundException("paymentId not found");
        }
        return transactionLog.get();
    }

    public TransactionLog getTransactionLogByPaymentIdAndAccountNumberAndRrnAndStatus(String accountNumber, Long paymentId, String rrn, TransactionStatus status) {
        Optional<TransactionLog> transactionLog = paymentLogRepository.findByAccountNumberAndPaymentIdAndRrnAndStatus(accountNumber, paymentId, rrn,status);

        if (transactionLog.isEmpty()) {
            throw new NotFoundException("paymentId not found");
        }
        return transactionLog.get();
    }

    public void checkDuplicateRequest(String accountNumber, String rrn) {
        List<TransactionLog> transactionLog = paymentLogRepository.findAllByAccountNumberAndRrn(accountNumber,  rrn);

        if (!transactionLog.isEmpty()) {
            throw new InvalidPaymentRequestException("Duplicate request ");
        }
    }
    private TransactionLog persistPayInRequest(PreRequest preRequest, String transCode, AccountDomain accountDomain,
                                               AccountPurseDomain accountPurseDomain, String errorMessage, TransactionStatus status,
                                               String paymentHash, Long paymentId, String paymentType,boolean isCredit) {

        return TransactionLog.builder().uuid(UUID.randomUUID().toString())
                .requestId(preRequest.getRequestId())
                .accountNumber(isCredit?accountDomain.getAccountNumber():preRequest.getFrmAccountNo())
                .frmCurrency(preRequest.getFrmCurrency())
                .description(preRequest.getTransDesc())
                .transCode(transCode)
                .purseNumber(Objects.nonNull(accountPurseDomain) ? accountPurseDomain.getPurseNumber() : null)
                .availableBalance(Objects.nonNull(accountPurseDomain) ? accountPurseDomain.getAvailableBalance() : 0)
                .ledgerBalance(Objects.nonNull(accountPurseDomain)?accountPurseDomain.getLedgerBalance():0)
                .clientId(Objects.nonNull(accountPurseDomain) ? accountDomain.getClientId() : null)
                .programId(Objects.nonNull(accountPurseDomain) ? accountDomain.getProgramId() : null)
                .status(status)
                .toAccountNumber(preRequest.getToAccount())
                .errorDescription(errorMessage)
                .paymentHash(paymentHash)
                .paymentType(paymentType)
                .paymentId(paymentId)
                .rrn(preRequest.getRrn())
                .createdAt(LocalDate.now())
                .build();

    }

    private BOD setBOD(TransactionLog transactionLog, Double openingBalance,Double closingBalance,String  frmAccountNumber, Double transAmnt){
        return BOD.builder().
        openingBalance(openingBalance)
                .closingBalance(closingBalance)
                .accountNumber(transactionLog.getAccountNumber())
                .fromAccount(frmAccountNumber)
                .transactionAmt(transAmnt)
                .currency(transactionLog.getFrmCurrency())
                .transType(transactionLog.getPaymentType())
                .createdAt(LocalDateTime.now())
                .paymentId(transactionLog.getPaymentId())
                .transCode(transactionLog.getTransCode())
                .build();

    }


}
