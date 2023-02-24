package com.payment.security;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Component
public class SecurityKeyGenerationator {

    public static boolean validatePublishableKey(String publishableKey) {
        if (publishableKey != null && publishableKey.startsWith("pk_")) {
            String secretKeyString = publishableKey.substring(3);
            try {
                // Convert the Base64-encoded secret key string to a SecretKey object
                byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyString);
                SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
                // Perform additional validation if needed
                return true;
            } catch (IllegalArgumentException e) {
                // Invalid Base64 encoding
                return false;
            }
        } else {
            return false;
        }
    }

    public  SecretKey generateSecretKey() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // Convert the UUID to bytes
        byte[] uuidBytes = uuid.toString().getBytes();
        // Use the bytes to create a SecretKey object
        return new SecretKeySpec(uuidBytes, "AES");
    }

    public  String generatePublishableKey(SecretKey secretKey) {
        // Convert the SecretKey to a Base64-encoded string
        String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        // Prepend the string "pk_" to create the publishable key
        return "pk_" + secretKeyString;
    }
}
