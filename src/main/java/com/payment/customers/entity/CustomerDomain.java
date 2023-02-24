package com.payment.customers.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name = "customer_profile")
public class CustomerDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_type")
    private String customerType;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;


    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender_type")
    private String genderType;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "email_one")
    private String emailOne;

    @Column(name = "mobile_one")
    private String mobileOne;

    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "ssn")
    private String ssn;

    @Column(name = "program_id")
    private String programId;

    @Column(name = "clientId")
    private String clientId;

    private LocalDate createAt;
    private LocalDate updateAt;
}
