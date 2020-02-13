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
    public void testGetKey() {
        SecretKey testKey = null;
        try {
            testKey = crypto.getKey();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            fail(e);
        }
        assertEquals(CryptoProvider.KEY_LENGTH / 8, testKey.getEncoded().length);
    }

    @Test
    public void testEncryptDecryptCorrectPassword() {
        byte[] encrypted = encryptTestData(SECRET_DATA);        // implicitly tests encrypt method
        byte[] salt = crypto.getSalt();                         // save salt

        byte[] decrypted = new byte[0];
        try {
            crypto = new CryptoProvider(PASSWORD, salt);        // reset CryptoProvider to simulate restart of program
            decrypted = crypto.decrypt(encrypted);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchPaddingException e) {
            fail(e);
        }
        assertEquals(new String(SECRET_DATA), new String(decrypted));
    }

    @Test
    public void testEncryptDecryptIncorrectPassword() {
        byte[] encrypted = encryptTestData(SECRET_DATA);        // implicitly tests encrypt method
        byte[] salt = crypto.getSalt();                         // save salt

        char[] incorrect = (String.valueOf(PASSWORD) + "incorrect").toCharArray();  // set incorrect password
        try {
            crypto = new CryptoProvider(incorrect, salt);       // reset CryptoProvider to simulate restart of program
            crypto.decrypt(encrypted);
            fail();
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchPaddingException e) {
            // Caught successfully
        }
    }

    // EFFECTS: helper method that encrypts SECRET_DATA
    private byte[] encryptTestData(byte[] input) {
        try {
            return crypto.encrypt(input);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            fail(e);
        }
        return null;
    }

}
