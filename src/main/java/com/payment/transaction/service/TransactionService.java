package com.payment.transaction.service;

import com.payment.customers.model.Payment;
import com.payment.transaction.component.TransactionLogDBProcess;
import com.payment.transaction.model.TransactionLog;
import com.payment.transaction.repository.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    PaymentLogRepository paymentLogRepository;


    public List<Payment> getPaymentsByPaymentId(Long paymentId){

       List<TransactionLog> transactionLogs = paymentLogRepository.findAllByPaymentId(paymentId);
       return transactionLogs.stream().map(this::getPayment).collect(Collectors.toList());
    }


    private Payment getPayment(TransactionLog data){
        return Payment.builder()
               // uuid(data.getUuid())
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
                .accountNumber(data.getAccountNumber())
                .paymentType(data.getPaymentType())
                .description(data.getErrorDescription())
                .build();
    }


}
