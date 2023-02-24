package com.payment.security;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecureKeyGeneratorExample {
    public static void main(String[] args) throws Exception {


       SecurityKeyGenerationator securityKeyGenerationator=new SecurityKeyGenerationator();














        // Generate a new RSA key pair with a key length of 2048 bits
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Extract the public and private keys
        PublicKey publicKey = keyPair.getPublic();
        /*System.out.println("publicKey:"+publicKey);
        */
        PrivateKey privateKey = keyPair.getPrivate();

        // Convert the keys to Base64-encoded strings
        String publishableKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        //securityKeyGenerationator.validatePublicKey(publishableKey);

        System.out.println("PublishableKey:"+publishableKey);

        String secretKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("privatekey:"+secretKey);
        System.out.println("secretKey:"+secretKey);
        byte[] data = "sireesha".getBytes();


        byte[] encrypted = encrypt(data, publicKey);


        byte[] decrypted = decrypt(encrypted, privateKey);
        String result = new String(decrypted);

        if ("sireesha".equals(result)) {
            System.out.println("Keys are valid!");
        } else {
            System.out.println("Keys are not valid.");
        }



        //part2:
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publishableKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey DecodepublicKey = keyFactory.generatePublic(keySpec);
        System.out.println("part2 Result"+publicKey.equals(DecodepublicKey));
        System.out.println(DecodepublicKey);

        byte[] data2 = "sireesha".getBytes();


        byte[] encrypted2 = encrypt(data, DecodepublicKey);


        byte[] decrypted2 = decrypt(encrypted2, privateKey);
        String result2= new String(decrypted2);

        if ("sireesha".equals(result2)) {
            System.out.println("Keys are valid!2");
        } else {
            System.out.println("Keys are not valid.");
        }

        //part 3:
        String test=securityKeyGenerationator.generatePublishableKey(securityKeyGenerationator.generateSecretKey());
        System.out.println("Test"+test);
        System.out.println("Result 3"+securityKeyGenerationator.validatePublishableKey(test));


        // String decodePulblicKey =String.valueOf( .);
        String decodesecretKey =String.valueOf( Base64.getDecoder().decode(secretKey));
       // System.out.println(decodePulblicKey);
        System.out.println(decodesecretKey);
       // System.out.println(publishableKey.equals(decodePulblicKey) +""+secretKey.equals(decodesecretKey));



// Generate an RSA public key object from the key specification

        System.out.println(publicKey.equals(DecodepublicKey));

        System.out.println(publishableKey.equals(Base64.getEncoder().encodeToString(DecodepublicKey.getEncoded())));


        // Validate the keys
        //byte[] data = "sireesha".getBytes();


        //byte[] encrypted = encrypt(data, publicKey);
        String dataPublishBleKey = String.valueOf(encrypted);
        System.out.println("data:"+dataPublishBleKey);
       // PublicKey publicKey1=decode(dataPublishBleKey);
        byte[] decrypted1 = decrypt(encrypted, privateKey);
        String result1 = new String(decrypted1);
        System.out.println("result1:"+result1);

        //byte[] decrypted = decrypt(encrypted, privateKey);
        //String result = new String(decrypted);

        /*if ("Hello, World!".equals(result)) {
            System.out.println("Keys are valid!");
        } else {
            System.out.println("Keys are not valid.");
        }*/
    }

    private static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    private static  PublicKey decode(String publishableKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publishableKey));

// Generate an RSA public key object from the key specification
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey decodepublicKey = keyFactory.generatePublic(keySpec);

        return decodepublicKey;

    }
}
