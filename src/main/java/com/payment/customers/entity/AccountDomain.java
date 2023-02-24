package com.payment.customers.entity;

import com.payment.customers.model.AccountStatus;
import com.payment.customers.model.AccountType;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
@Data
@Entity(name = "Account")
public class AccountDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customer_id;

    private Long programId;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountNumber;
    private Long clientId;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(targetEntity = AccountPurseDomain.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Set<AccountPurseDomain> accountPurseDomain;
    private LocalDate createAt;
    private LocalDate updateAt;
}
