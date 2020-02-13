package util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

// Handles all encryption and decryption processes
public class CryptoProvider {

    public static final String KEY_METHOD = "PBKDF2WithHmacSHA256";
    public static final int ITERATION_COUNT = 100000;
    public static final int KEY_LENGTH = 256;
    public static final int SALT_LENGTH = 16;
    public static final String CIPHER_METHOD = "AES/GCM/NoPadding";
    public static final int GCM_TAG_LENGTH = 128;
    public static final int IV_LENGTH = 16;

    private char[] password;
    private byte[] salt;
    private Cipher cipher;

    // EFFECTS: stores password and generates a random salt, then instantiates new cipher
    public CryptoProvider(char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.password = password;
        this.salt = generateSecureBytes(SALT_LENGTH);
        cipher = Cipher.getInstance(CIPHER_METHOD);
    }

    // EFFECTS: stores password and salt, then instantiates new cipher
    public CryptoProvider(char[] password, byte[] salt) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.password = password;
        this.salt = salt;
        cipher = Cipher.getInstance(CIPHER_METHOD);
    }

    // EFFECTS: generates random IV, encrypts input data, then prepends IV
    public byte[] encrypt(byte[] input) throws InvalidKeySpecException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] iv = generateSecureBytes(IV_LENGTH);

        cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        byte[] encrypted = cipher.doFinal(input);

        // Prepend encrypted data with IV
        byte[] data = new byte[IV_LENGTH + encrypted.length];
        System.arraycopy(iv, 0, data, 0, IV_LENGTH);
        System.arraycopy(encrypted, 0, data, IV_LENGTH, encrypted.length);

        return data;
    }

    // EFFECTS: uses IV and key to decrypt input data
    public byte[] decrypt(byte[] input) throws InvalidKeySpecException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Split input into IV and data
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[input.length - IV_LENGTH];
        System.arraycopy(input, 0, iv, 0, IV_LENGTH);
        System.arraycopy(input, IV_LENGTH, encrypted, 0, encrypted.length);

        cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        return cipher.doFinal(encrypted);
    }

    // EFFECTS: regenerates key from password and salt
    protected SecretKey getKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Define new password-based encryption specification based on these parameters
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);

        // Use a secret-key factory to construct a key
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_METHOD);
        return new SecretKeySpec(skf.generateSecret(spec).getEncoded(), "AES");
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

    public byte[] getSalt() {
        return salt;
    }

}
