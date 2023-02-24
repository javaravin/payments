package com.payment.payerin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayInResponse {

    private String paymentId;

    private String status;

    private String rrn;

}
