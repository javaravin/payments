package com.payment.payerin.api;

import com.payment.customers.model.Payment;
import com.payment.payerin.request.PaymentRequest;
import com.payment.payerin.request.PreRequest;
import com.payment.payerin.service.PayeInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payerIn")
public class PayeInAPI {
    @Autowired
    PayeInService payeInService;
    @PostMapping("/{clientId}")
    public ResponseEntity initialize(@PathVariable("clientId") String clientId, @RequestBody PreRequest payInPreRequest){
        Payment  payment =payeInService.initialize(clientId, payInPreRequest);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{clientId}/payment")
    public ResponseEntity confirm(@PathVariable("clientId") String clientId, @RequestBody PaymentRequest paymentRequest){
        Payment  payment =payeInService.doPayment(clientId, paymentRequest);
        return ResponseEntity.ok(payment);
    }

}
