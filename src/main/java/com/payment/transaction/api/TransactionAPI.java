package com.payment.transaction.api;

import com.payment.customers.model.Payment;
import com.payment.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("transactions")
public class TransactionAPI {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{paymentId}")
    public List<Payment> getPayments(@PathVariable("paymentId") Long paymentId){
        return transactionService.getPaymentsByPaymentId(paymentId);
    }

}
