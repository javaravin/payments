package com.payment.transaction;

import com.payment.customers.model.AccountStatus;
import com.payment.exception.InvalidPaymentRequestException;
import com.payment.payerin.request.PaymentRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Component
public class TransactionUtil {

    public static void checkAccountBalance(Double frmActAvailableBalance, Double transactionAmount){

        if(transactionAmount > frmActAvailableBalance) {
            throw new InvalidPaymentRequestException("Insufficient founds");

        }

    }

    public static void domesticCurrencyCheck(String frmCurrency, String targetCurrency){

        if(!frmCurrency.equalsIgnoreCase(targetCurrency)) {
            throw new InvalidPaymentRequestException("Domestic transaction currency should be same");

        }

    }
    public static void checkActStatus(AccountStatus accountStatus, String message){

        if(!AccountStatus.ACTIVE.equals(accountStatus) ) {
            throw new InvalidPaymentRequestException(message);

        }

    }

    public static String hashValues(Double transAmount, String frmAccountNo, String toAccountNumber, String transCode, String currency) {
        return String.valueOf(transAmount).concat(frmAccountNo).concat(String.valueOf(toAccountNumber)).concat(transCode).concat(currency).concat("bank");
    }

    public static String generateHash(String data) {
        try {
            // Create a new MessageDigest object with the SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Generate a hash value for the input data
            byte[] hash = md.digest(data.getBytes());

            // Convert the hash value to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidPaymentRequestException("Hash Generation failed ");
        }
        }
    public static long generateRandom(int length) {
        SecureRandom secureRandom=new SecureRandom();

        char[] digits = new char[length];
        digits[0] = (char) (secureRandom.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (secureRandom.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    public static void validateHashValue(String actualValue, PaymentRequest paymentRequest, String tranCode){
       String expectedHash= generateHash(hashValues(paymentRequest.getTransAmt(), paymentRequest.getFrmAccountNo(),
                paymentRequest.getToAccount(),tranCode, paymentRequest.getFrmCurrency()));
       if(!actualValue.equals(expectedHash)){
           throw new InvalidPaymentRequestException("Mismatched params Invalid payment request ");
       }
    }
}
