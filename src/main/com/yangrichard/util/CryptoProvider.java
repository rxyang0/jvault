package com.yangrichard.util;

import javax.crypto.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// Handles all encryption and decryption processes
public class CryptoProvider {

    private static final String CIPHER_METHOD = "AES/GCM/NoPadding";

    private char[] password;
    private Cipher cipher;

    // EFFECTS: stores password and instantiates new cipher
    public CryptoProvider(char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.password = password;
        cipher = Cipher.getInstance(CIPHER_METHOD);
    }

    // EFFECTS: securely generates given number of random bytes
    private byte[] generateSecureBytes(int num) {
        byte[] bytes = new byte[num];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

}
