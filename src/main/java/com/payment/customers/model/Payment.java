package com.payment.customers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.transaction.model.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Payment {
    private String uuid;

    private String requestId;

    private String accountNumber;
    private String purseNumber;

    private String currency;
    private Double availableBalance;
    private Double ledgerBalance;
    private String description;
    private Long clientId;
    private Long programId;
    private String fromAccount;
    private String fromClientId;

    private Long paymentId;

    private String paymentType;

    private TransactionStatus status;
}
