package com.payment.customers.repository;

import com.payment.customers.entity.AccountDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountDomain, Long> {

    Optional<AccountDomain> findByAccountNumberAndClientId(String accountNumber,Long clientId);
    Optional<AccountDomain> findByAccountNumber(String accountNumber);

}
