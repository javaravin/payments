package com.payment.exception;

public class InvalidPaymentRequestException extends RuntimeException{

    public InvalidPaymentRequestException(String message) {
        super(message);
    }
}
