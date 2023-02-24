package com.payment.customers.repository;

import com.payment.customers.entity.CustomerDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDomain, Long> {
}
