package com.payment.customers.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "account_purse")
public class AccountPurseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purseNumber;
    private String currency;
    private Double availableBalance;
    private Double ledgerBalance;

    private String isPrimary;

    private LocalDate createAt;
    private LocalDate updateAt;

}
