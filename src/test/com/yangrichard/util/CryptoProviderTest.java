package com.yangrichard.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoProviderTest {

    private static final char[] PASSWORD = "test123".toCharArray();
    private static final byte[] SECRET_DATA = "Trust The Natural Recursion".getBytes();

    private CryptoProvider crypto;

    @BeforeEach
    public void runBefore() {
        try {
            crypto = new CryptoProvider(PASSWORD);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            fail(e);
        }
    }

    @Test
    public void testGenerateSecureBytesLength() {
        assertEquals(16, CryptoProvider.generateSecureBytes(16).length);
    }

    @Test
    public void testGenerateKeyFromPassword() {
        SecretKey testKey = null;
        try {
            testKey = CryptoProvider.generateKeyFromPassword(PASSWORD, CryptoProvider.generateSecureBytes(16));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            fail(e);
        }
        assertNotNull(testKey);
    }

    @Test
    public void testEncryptDecryptCorrectPassword() {
        byte[] encryptedData = encryptTestData();   // implicitly tests encrypt method
        runBefore();    // reset CryptoProvider to emulate restart of program

        try {
            byte[] decryptedData = crypto.decrypt(encryptedData);
            assertEquals(new String(SECRET_DATA), new String(decryptedData));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            fail("Error in decryption");
        }
    }

    @Test
    public void testEncryptDecryptIncorrectPassword() {
        byte[] encryptedData = encryptTestData();   // implicitly tests encrypt method
        runBefore();    // reset CryptoProvider to emulate restart of program
        crypto.setPassword((String.valueOf(PASSWORD) + "incorrect").toCharArray());  // set incorrect password

        try {
            crypto.decrypt(encryptedData);
            fail("Error in decryption");
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            // Caught successfully
        }
    }

    private byte[] encryptTestData() {
        try {
            return crypto.encrypt(SECRET_DATA);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            fail("Error in encryption");
        }
        return null;
    }

}
