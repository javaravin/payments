package com.payment.payerin.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreRequest {


    private String requestId;

    private String frmAccountNo;

    private String frmBranch;

    private String frmCurrency;
    private String frmActHolderName;

    private String toAccount;

    private String toAccountName;

    private Double transAmt;

    private Mode paymentMethod;

    private String device;

    private String transDesc;

    private String rrn;


}
