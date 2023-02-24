package com.payment.customers.service;

import com.payment.customers.entity.AccountDomain;
import com.payment.customers.entity.AccountPurseDomain;
import com.payment.customers.model.Account;
import com.payment.customers.model.AccountPurse;
import com.payment.customers.repository.AccountRepository;
import com.payment.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account getAccountDetails(String accountNumber, Long clientId){
       Optional<AccountDomain> accountDomain = accountRepository.findByAccountNumberAndClientId(accountNumber, clientId);
         if(accountDomain.isEmpty()){
             throw new NotFoundException("Does not exist the accountNumber :"+accountNumber);
         }
        return getAccountDetails(accountDomain.get());
    }


    public List<Account> getAccounts(){
        List<AccountDomain> accountDomains=accountRepository.findAll();
        return accountDomains.stream().map(this::getAccountDetails).collect(Collectors.toList());
    }



    private Account getAccountDetails(AccountDomain accountDomain){

        return Account.builder().
        id(accountDomain.getId())
                .accountNumber(accountDomain.getAccountNumber())
                .accountType(accountDomain.getAccountType())
                .purse(accountDomain.getAccountPurseDomain().stream().map(this::getAccountPurse).collect(Collectors.toSet()))
                .customer_id(accountDomain.getCustomer_id())
                .accountStatus(accountDomain.getStatus())
                .createAt(accountDomain.getCreateAt())
                .updateAt(accountDomain.getUpdateAt())
                .build();


    }

    private AccountPurse getAccountPurse(AccountPurseDomain accountPurseDomain){
        return AccountPurse.builder()
                .isPrimary("Y")
                .id(accountPurseDomain.getId())
                .purseCode(accountPurseDomain.getPurseNumber())
                .currency(accountPurseDomain.getCurrency())
                .availableBalance(accountPurseDomain.getAvailableBalance())
                .ledgerBalance(accountPurseDomain.getLedgerBalance())
                .createAt(accountPurseDomain.getCreateAt())
                .updateAt(accountPurseDomain.getUpdateAt())
                .build();
    }

}
