package com.payment;

import com.payment.customers.entity.AccountDomain;
import com.payment.customers.entity.AccountPurseDomain;
import com.payment.customers.model.AccountStatus;
import com.payment.customers.model.AccountType;
import com.payment.customers.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PaymentsApplication {

	@Autowired
	AccountRepository accountRepository;
	public static void main(String[] args) {
		SpringApplication.run(PaymentsApplication.class, args);
	}
	//@PostConstruct
	public void metaDataInsert(){
		AccountDomain accountDomain = new AccountDomain();
		SecureRandom secureRandom=new SecureRandom();
		long randInRange = secureRandom.nextLong();

		accountDomain.setAccountNumber(String.format(String.format("%d", generateRandom(12))));
		accountDomain.setClientId(1l);
		accountDomain.setCustomer_id(generateRandom(8));
		accountDomain.setCreateAt(LocalDate.now());
		accountDomain.setUpdateAt(LocalDate.now());
		accountDomain.setProgramId(1l);
		Set<AccountPurseDomain> accountPursDomains =new HashSet<>();
		AccountPurseDomain accountPurseDomain = new AccountPurseDomain();

		accountPurseDomain.setCurrency("USD");
		accountPurseDomain.setIsPrimary("Y");
		accountPurseDomain.setAvailableBalance(200d);
		accountPurseDomain.setLedgerBalance(200d);
		accountPurseDomain.setCreateAt(LocalDate.now());
		accountPurseDomain.setPurseNumber(String.valueOf(generateRandom(5)));
		accountPursDomains.add(accountPurseDomain);
		accountDomain.setAccountType(AccountType.CORP);
		accountDomain.setStatus(AccountStatus.ACTIVE);
		accountDomain.setAccountPurseDomain(accountPursDomains);
		accountRepository.save(accountDomain);
		metaDataInsertUser();
		//List<Account> accountList=accountRepository.findAll();
		//System.out.println(accountList);
	}

	public void metaDataInsertUser(){
		AccountDomain accountDomain = new AccountDomain();
		SecureRandom secureRandom=new SecureRandom();
		long randInRange = secureRandom.nextLong();

		accountDomain.setAccountNumber(String.format(String.format("%d", generateRandom(12))));
		accountDomain.setClientId(1l);
		accountDomain.setCustomer_id(generateRandom(8));
		accountDomain.setCreateAt(LocalDate.now());
		accountDomain.setUpdateAt(LocalDate.now());
		accountDomain.setProgramId(1l);
		Set<AccountPurseDomain> accountPursDomains =new HashSet<>();
		AccountPurseDomain accountPurseDomain = new AccountPurseDomain();
		accountPurseDomain.setCurrency("USD");
		accountPurseDomain.setIsPrimary("Y");
		accountPurseDomain.setCreateAt(LocalDate.now());
		accountPurseDomain.setPurseNumber(String.valueOf(generateRandom(5)));
		accountPurseDomain.setAvailableBalance(200d);
		accountPurseDomain.setLedgerBalance(200d);
		accountPursDomains.add(accountPurseDomain);
		accountDomain.setAccountType(AccountType.USER);
		accountDomain.setStatus(AccountStatus.ACTIVE);

		accountDomain.setAccountPurseDomain(accountPursDomains);
		accountRepository.save(accountDomain);

		//List<Account> accountList=accountRepository.findAll();
		//System.out.println(accountList);
	}

	public  long generateRandom(int length) {
		SecureRandom secureRandom=new SecureRandom();

		char[] digits = new char[length];
		digits[0] = (char) (secureRandom.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (secureRandom.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

}
