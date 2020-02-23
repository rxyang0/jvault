package util;

import com.google.gson.JsonObject;
import exceptions.CryptoException;
import io.Jsonable;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

// Handles all encryption and decryption processes
public class CryptoProvider implements Jsonable {

    protected static String KEY_METHOD = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    protected static String CIPHER_METHOD = "AES/GCM/NoPadding";
    protected static int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 16;

    private char[] password;
    private byte[] salt;
    private Cipher cipher;

    // EFFECTS: stores password and generates a random salt, then instantiates new cipher
    public CryptoProvider(char[] password) throws CryptoException {
        this.password = password;
        this.salt = generateSecureBytes(SALT_LENGTH);
        initCipher();
    }

    // EFFECTS: stores password and salt, then instantiates new cipher
    public CryptoProvider(char[] password, byte[] salt) throws CryptoException {
        this.password = password;
        this.salt = salt;
        initCipher();
    }

    // MODIFIES: this
    // EFFECTS: instantiates cipher with CIPHER_METHOD
    private void initCipher() throws CryptoException {
        try {
            cipher = Cipher.getInstance(CIPHER_METHOD);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    // EFFECTS: regenerates key from password and salt
    protected SecretKey getKey() throws CryptoException {
        try {
            // Define new password-based encryption specification based on these parameters
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);

            // Use a secret-key factory to construct a key
            SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_METHOD);
            return new SecretKeySpec(skf.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    // EFFECTS: generates random IV, encrypts input data, then prepends IV
    public byte[] encrypt(byte[] input) throws CryptoException {
        try {
            byte[] iv = generateSecureBytes(IV_LENGTH);

            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(input);

            // Prepend encrypted data with IV
            byte[] data = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, data, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, data, IV_LENGTH, encrypted.length);

            return data;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new CryptoException(e);
        }
    }

    // EFFECTS: uses IV and key to decrypt input data
    public byte[] decrypt(byte[] input) throws CryptoException {
        try {
            // Split input into IV and data
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[input.length - IV_LENGTH];
            System.arraycopy(input, 0, iv, 0, IV_LENGTH);
            System.arraycopy(input, IV_LENGTH, encrypted, 0, encrypted.length);

            cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return cipher.doFinal(encrypted);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new CryptoException(e);
        }
    }

    // EFFECTS: securely generates given number of random bytes
    protected static byte[] generateSecureBytes(int num) {
        byte[] bytes = new byte[num];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    // MODIFIES: this
    // EFFECTS: sets password and salt to null
    public void destroy() {
        password = null;
        salt = null;
    }

    @Override
    public JsonObject toJson() {
        JsonObject crypto = new JsonObject();
        crypto.addProperty("salt", Base64.getEncoder().encodeToString(salt));
        return crypto;
    }

}
