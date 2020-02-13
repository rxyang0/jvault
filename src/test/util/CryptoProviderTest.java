package util;

import exceptions.CryptoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.*;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoProviderTest {

    private static final char[] PASSWORD = "test123".toCharArray();
    private static final byte[] SECRET_DATA = "Trust The Natural Recursion".getBytes();

    private CryptoProvider crypto;

    @BeforeEach
    public void runBefore() {
        try {
            crypto = new CryptoProvider(PASSWORD);
        } catch (CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testConstructorInvalidCipherMethod() {
        String origCipherMethod = CryptoProvider.CIPHER_METHOD;
        CryptoProvider.CIPHER_METHOD = "";      // Invalidate cipher method
        try {
            crypto = new CryptoProvider(PASSWORD);
            fail();
        } catch (CryptoException e) {
            // Caught successfully
        } finally {
            CryptoProvider.CIPHER_METHOD = origCipherMethod;
        }
    }

    @Test
    public void testGetKeyInvalidKeyMethod() {
        String origKeyMethod = CryptoProvider.KEY_METHOD;
        try {
            CryptoProvider.KEY_METHOD = "";     // Invalidate key method
            SecretKey testKey = crypto.getKey();
            fail();
        } catch (CryptoException e) {
            // Caught successfully
        } finally {
            CryptoProvider.KEY_METHOD = origKeyMethod;
        }
    }

    @Test
    public void testEncryptDecryptCorrectPassword() {
        byte[] encrypted = encryptTestData();                   // implicitly tests encrypt method
        byte[] salt = crypto.getSalt();                         // save salt

        byte[] decrypted = new byte[0];
        try {
            crypto = new CryptoProvider(PASSWORD, salt);        // reset CryptoProvider to simulate restart of program
            decrypted = crypto.decrypt(encrypted);
        } catch (CryptoException e) {
            fail(e);
        }
        assertEquals(new String(SECRET_DATA), new String(decrypted));
    }

    @Test
    public void testEncryptDecryptIncorrectPassword() {
        byte[] encrypted = encryptTestData();                   // implicitly tests encrypt method
        byte[] salt = crypto.getSalt();                         // save salt

        char[] incorrect = (String.valueOf(PASSWORD) + "incorrect").toCharArray();  // set incorrect password
        try {
            crypto = new CryptoProvider(incorrect, salt);       // reset CryptoProvider to simulate restart of program
            crypto.decrypt(encrypted);
            fail();
        } catch (CryptoException e) {
            // Caught successfully
        }
    }

    @Test
    public void testEncryptInvalidAlgorithmParameters() {
        int origGcmTagLength = CryptoProvider.GCM_TAG_LENGTH;
        try {
            CryptoProvider.GCM_TAG_LENGTH = 0;     // Invalidate GCM_TAG_LENGTH
            crypto.encrypt(SECRET_DATA);
        } catch (CryptoException e) {
            // Caught successfully
        } finally {
            CryptoProvider.GCM_TAG_LENGTH = origGcmTagLength;
        }
    }

    @Test
    public void testGenerateSecureBytesLength() {
        assertEquals(16, CryptoProvider.generateSecureBytes(16).length);
    }

    @Test
    public void testDestroy() {
        crypto.destroy();
        assertNull(crypto.getSalt());
    }

    // EFFECTS: helper method that encrypts SECRET_DATA
    private byte[] encryptTestData() {
        try {
            return crypto.encrypt(SECRET_DATA);
        } catch (CryptoException e) {
            fail(e);
        }
        return null;
    }

}
