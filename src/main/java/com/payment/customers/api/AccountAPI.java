package com.payment.customers.api;

import com.payment.customers.entity.AccountDomain;
import com.payment.customers.model.Account;
import com.payment.customers.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/accounts")
public class AccountAPI {

    @Autowired
    AccountService accountService;

    @GetMapping("/{accountNumber}/{clientId}")
    public ResponseEntity<Account> getAccount(@PathVariable("accountNumber") String accountNumber, @PathVariable("clientId") Long clientCode ){
        return ResponseEntity.ok(accountService.getAccountDetails(accountNumber, clientCode));
    }

    /**
     *
     * Ravi Nagna
     * Ac
     * Avl LdgBala
     * Statement
     * ---------------
     * mode/UTR/time/amount/cr/dr/balance/frmaccount
     *
     *
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAccounts(){
        return ResponseEntity.of(Optional.of(accountService.getAccounts()));
    }

}
