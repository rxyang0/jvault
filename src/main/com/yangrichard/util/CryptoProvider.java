package com.yangrichard.util;

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

    private static final String KEY_METHOD = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final String CIPHER_METHOD = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 16;

    private char[] password;
    private byte[] lastSalt;
    private byte[] lastIV;
    private Cipher cipher;

    // EFFECTS: stores password and instantiates new cipher
    public CryptoProvider(char[] password) throws NoSuchPaddingException, NoSuchAlgorithmException {
        setPassword(password);
        cipher = Cipher.getInstance(CIPHER_METHOD);
    }

    // EFFECTS: uses a generated secret key to encrypt binary data with AES-GCM
    public byte[] encrypt(byte[] input) throws InvalidKeySpecException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        lastSalt = generateSecureBytes(SALT_LENGTH);
        lastIV = generateSecureBytes(IV_LENGTH);

        SecretKey key = generateKeyFromPassword(password, lastSalt);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, lastIV));

        return cipher.doFinal(input);
    }

    // EFFECTS: regenerates key from password and salt, and uses it with same IV to decrypt data
    public byte[] decrypt(byte[] input, byte[] salt, byte[] iv) throws InvalidKeySpecException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        SecretKey key = generateKeyFromPassword(password, salt);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        return cipher.doFinal(input);
    }

    // EFFECTS: create a key from the given password and a generated salt
    protected static SecretKey generateKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
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
    // EFFECTS: sets password
    protected void setPassword(char[] password) {
        this.password = password;
    }

    public byte[] getLastSalt() {
        return lastSalt;
    }

    public byte[] getLastIV() {
        return lastIV;
    }

}
