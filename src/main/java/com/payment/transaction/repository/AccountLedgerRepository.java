package com.payment.transaction.repository;

import com.payment.customers.entity.BOD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLedgerRepository extends JpaRepository<BOD,Long> {

}
