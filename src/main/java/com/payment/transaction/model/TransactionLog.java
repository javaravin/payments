package com.payment.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "transaction_log")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {
    @Id
    private String uuid;

    private String requestId;

    private String transCode;

    private String accountNumber;
    private String toAccountNumber;

    private String purseNumber;

    private String frmCurrency;
    private Double availableBalance;
    private Double ledgerBalance;
    private String description;
    private Long clientId;
    private Long programId;
    private String fromClientId;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String paymentHash;

    private String errorDescription;

    private Long paymentId;

    private String paymentType;

    private String rrn;

    private LocalDate createdAt;
    private LocalDate updateAt;
}
