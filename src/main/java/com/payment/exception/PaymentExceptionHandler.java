package com.payment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> badException(NotFoundException exception) {
        Map<String,String> response=new HashMap<>(1);
        response.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = PaymentErrorException.class)
    public ResponseEntity<Object> exception(PaymentErrorException exception) {
        Map<String,String> response=new HashMap<>(1);
        response.put("message", exception.getMessage());

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(value = InvalidPaymentRequestException.class)
    public ResponseEntity<Object> exception(InvalidPaymentRequestException exception) {
        Map<String,String> response=new HashMap<>(1);
        response.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> globalException(Exception exception) {
        Map<String,String> response=new HashMap<>(1);
        response.put("message", "Internal server error");
        exception.printStackTrace();

        return ResponseEntity.internalServerError().body(response);
    }
}
