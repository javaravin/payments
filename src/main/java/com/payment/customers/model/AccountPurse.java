package com.payment.customers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.customers.entity.AccountDomain;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountPurse {
    private Long id;
    private AccountDomain accountDomainId;
    private String purseCode;
    private String currency;
    private Double availableBalance;
    private Double ledgerBalance;

    private String isPrimary;

    private LocalDate createAt;
    private LocalDate updateAt;

}
