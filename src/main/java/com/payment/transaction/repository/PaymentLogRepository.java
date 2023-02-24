package com.payment.transaction.repository;

import com.payment.transaction.model.TransactionLog;
import com.payment.transaction.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentLogRepository  extends JpaRepository<TransactionLog, Long> {

    List<TransactionLog> findAllByPaymentId(Long paymentId);

    Optional<TransactionLog> findByAccountNumberAndPaymentIdAndRrn(String accountNumber, Long paymentId,String rrn);
    List<TransactionLog> findAllByAccountNumberAndRrn(String accountNumber, String rrn);
    Optional<TransactionLog> findByAccountNumberAndPaymentIdAndRrnAndStatus(String accountNumber, Long paymentId, String rrn, TransactionStatus status);

}
