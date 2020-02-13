package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.CryptoProvider;

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
    public void testGenerateKey() {
        byte[] testKey = new byte[0];
        try {
            testKey = CryptoProvider.generateKey(PASSWORD);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            fail(e);
        }
        assertNotEquals(0, testKey.length);
    }

    @Test
    public void testEncryptDecryptCorrectPassword() {
        byte[] encryptedData = encryptTestData();   // implicitly tests encrypt method
        byte[] lastSaltAndIV = crypto.getLastSaltAndIV();
        runBefore();    // reset CryptoProvider to emulate restart of program

        try {
            byte[] decryptedData = crypto.decrypt(encryptedData, lastSaltAndIV);
            assertEquals(new String(SECRET_DATA), new String(decryptedData));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            fail("Error in decryption");
        }
    }

    @Test
    public void testEncryptDecryptIncorrectPassword() {
        byte[] encryptedData = encryptTestData();   // implicitly tests encrypt method
        byte[] lastSaltAndIV = crypto.getLastSaltAndIV();
        runBefore();    // reset CryptoProvider to emulate restart of program
        crypto.setPassword((String.valueOf(PASSWORD) + "incorrect").toCharArray());  // set incorrect password

        try {
            crypto.decrypt(encryptedData, lastSaltAndIV);
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
