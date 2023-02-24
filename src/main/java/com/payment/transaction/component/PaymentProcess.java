package com.payment.transaction.component;

import com.payment.customers.entity.AccountDomain;
import com.payment.customers.entity.AccountPurseDomain;
import com.payment.customers.repository.AccountRepository;
import com.payment.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentProcess {
    @Autowired
    private AccountRepository accountRepository;

    public Double getAccountBalance(String accountNumber, String currency, Long clientId) {
        AccountDomain accountDomain = getAccount(accountNumber, clientId);
        return getAccountPurse(currency, accountDomain).getAvailableBalance();
    }

    public AccountPurseDomain updateBalance(String accountNumber, String currency, Double atomicLong, Long clientId) {
        AccountDomain accountDomain = getAccount(accountNumber,clientId);
        AccountPurseDomain accountPurseDomain = getAccountPurse(currency, accountDomain);
        synchronized (this) {
            accountPurseDomain.setAvailableBalance(atomicLong + accountPurseDomain.getAvailableBalance());
            accountPurseDomain.setLedgerBalance(atomicLong + accountPurseDomain.getLedgerBalance());
            accountPurseDomain.setUpdateAt(LocalDate.now());
            accountDomain.getAccountPurseDomain().add(accountPurseDomain);
            AccountDomain accountDomain1 = accountRepository.save(accountDomain);
        }
        return accountPurseDomain;
    }


    public AccountDomain getAccount(String accountNumber,Long clientId) {
        Optional<AccountDomain> account = accountRepository.findByAccountNumberAndClientId(accountNumber, clientId);
        System.out.println("account"+account);
        if (account.isEmpty()) {
            throw new NotFoundException("Account not found:"+accountNumber);
        }
        return account.get();
    }

    public AccountPurseDomain getAccountPurse(String currency, AccountDomain accountDomain) {
        AccountPurseDomain purse = accountDomain.getAccountPurseDomain().stream().filter(p -> p.getCurrency().equalsIgnoreCase(currency)).findAny()
                .orElseThrow(() -> new NotFoundException("Account not exist :".concat(accountDomain.getAccountNumber()).concat(" for  this currency:").concat(currency)));
        return purse;
    }
}
