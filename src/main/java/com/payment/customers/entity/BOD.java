package com.payment.customers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "BOD")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BOD {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@JsonIgnore
    private Long id;

    private Double transactionAmt;

    private String accountNumber;

    private String transType;

    private String currency;

    private String fromAccount;

    private Double openingBalance;
    private Double closingBalance;
    private LocalDateTime createdAt;

    private Long paymentId;

    private String transCode;

    @Override
    public String toString() {

        return
                 String.format("%.02f", transactionAmt) +
                ", " + accountNumber +
                ","+ transType +
                "," + currency +
                ", "+ String.format("%12s", fromAccount!=null?fromAccount:"") +
                ", " + String.format("%.02f", openingBalance) +
                ", "+ String.format("%.02f", closingBalance) +
                ", " + createdAt +
                ", " +paymentId +
                ", " + transCode ;

    }
}
