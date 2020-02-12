package com.yangrichard.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoProviderTest {

    private static final char[] password = "test123".toCharArray();

    private CryptoProvider crypto;

    @BeforeEach
    public void runBefore() {
        try {
            crypto = new CryptoProvider(password);
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
            testKey = crypto.generateKeyFromPassword();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            fail(e);
        }
        assertNotNull(testKey);
    }

}
