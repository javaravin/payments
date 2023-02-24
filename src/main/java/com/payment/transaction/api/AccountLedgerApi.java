package com.payment.transaction.api;

import com.payment.customers.entity.BOD;
import com.payment.transaction.ledger.AccountLedgers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ledgers")
public class AccountLedgerApi {

    @Autowired
    private AccountLedgers accountLedgers;
    @GetMapping
    public String getBods(Long paymentId){
       String header="TrsAmt,A/C\t\t,Type,Curr" +
               ",fromAccount,\tOPB,\tCLB,\tTrsTime,\t\t paymentId,\t" +
               "TrsCode ";
       String data= accountLedgers.accountLedgers().stream().map(bod->bod.toString()).collect(Collectors.joining("\n"));
        return header.concat("\n").concat(data);
    }
    @GetMapping("/{paymentId}")
    public List<BOD> getBod(@PathVariable("paymentId") Long paymentId){
        return accountLedgers.accountLedgerByPaymentId(paymentId);
    }
}
