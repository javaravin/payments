package com.payment.customers.repository;

import com.payment.customers.entity.BOD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BODRepository extends JpaRepository<BOD, Long> {

   List<BOD> findAllByPaymentId(Long paymentId);
}
