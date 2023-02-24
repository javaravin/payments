package com.payment.customers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {

    private Long id;

    private Long customer_id;

    private Long programId;

    private AccountType accountType;
    private AccountStatus accountStatus;
    private String accountNumber;
    private Long clientId;
    private Set<AccountPurse> purse;
    private LocalDate createAt;
    private LocalDate updateAt;
}
