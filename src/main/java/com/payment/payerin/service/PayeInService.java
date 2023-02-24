package com.payment.payerin.service;

import com.payment.customers.model.Payment;
import com.payment.exception.InvalidPaymentRequestException;
import com.payment.payerin.request.PaymentRequest;
import com.payment.payerin.request.PreRequest;
import com.payment.transaction.component.TransactionLogDBProcess;
import com.payment.transaction.model.TransactionLog;
import com.payment.transaction.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class PayeInService {

    private static final Logger log= Logger.getLogger(PayeInService.class.getName());
    @Autowired
    private TransactionLogDBProcess transactionLogDBProcess;

    public Payment initialize(String clientId, PreRequest payInPreRequest){
        log.info("initialize request has received for custId"+clientId);
        inputValidation(payInPreRequest);
     transactionLogDBProcess.checkDuplicateRequest(payInPreRequest.getFrmAccountNo(),payInPreRequest.getRrn());
        Long paymentId =transactionLogDBProcess.checkAccountBalanceAndStatus(payInPreRequest, Long.valueOf(clientId));
        log.info("initialize request has received for custId"+clientId);

        return transactionLogDBProcess.getPayment(paymentId, payInPreRequest.getRrn(), payInPreRequest.getFrmAccountNo(),null);
    }

    public Payment doPayment(String clientId, PaymentRequest paymentRequest){
        log.info("paymentConfirm request has received for custId"+clientId);
       TransactionLog transactionLog = transactionLogDBProcess.getTransactionLogByPaymentIdAndAccountNumberAndRrnAndStatus(paymentRequest.getFrmAccountNo(),
                paymentRequest.getPaymentId(), paymentRequest.getRrn(), TransactionStatus.INITIALIZE);
        transactionLogDBProcess.doPayment(Long.valueOf(clientId), paymentRequest, transactionLog);
        log.info("paymentConfirm request has received for custId"+clientId);
        return transactionLogDBProcess.getPayment(paymentRequest.getPaymentId(), paymentRequest.getRrn(), paymentRequest.getFrmAccountNo(),transactionLog);
    }

    private void doPaymentInputValidation(PaymentRequest paymentRequest){

        if(Objects.isNull(paymentRequest.getPaymentId()))
            throw new InvalidPaymentRequestException("Invalid request missing PaymentId");
        inputValidation(paymentRequest);
    }

    private void inputValidation(PreRequest paymentRequest){

        if(Objects.isNull(paymentRequest.getFrmAccountNo()))
            throw new InvalidPaymentRequestException("Invalid request missing FrmAccountNo");

        if(Objects.isNull(paymentRequest.getTransAmt()))
            throw new InvalidPaymentRequestException("Invalid request missing TransAmt");
        if(Objects.isNull(paymentRequest.getToAccount()))
            throw new InvalidPaymentRequestException("Invalid request missing ToAccount");
        if(Objects.isNull(paymentRequest.getRrn()))
            throw new InvalidPaymentRequestException("Invalid request missing rrn");
        if(Objects.isNull(paymentRequest.getRequestId()))
            throw new InvalidPaymentRequestException("Invalid request missing RequestId");

    }
}
