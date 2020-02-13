package filesystem;

import exceptions.CryptoException;
import io.Reader;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class VaultTest {

    private static final File TEST_VAULT_EXISTS = new File("data/test-vault");
    private static final File TEST_VAULT_NO_EXIST = new File("data/no-vault");
    private static final char[] TEST_PASSWORD = "test123".toCharArray();

    private Vault vault;

    @BeforeEach
    public void runBefore() {
        deleteTestVaults();
        try {
            vault = new Vault(TEST_VAULT_EXISTS, TEST_PASSWORD);
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testConstructorExistingVault() {
        try {
            vault = new Vault(TEST_VAULT_EXISTS, TEST_PASSWORD);
            assertTrue(vault.vaultFolder.exists());
            assertTrue(vault.dataFolder.exists());
            assertNotNull(vault.filesystem);
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testConstructorCreateVault() {
        try {
            vault = new Vault(TEST_VAULT_NO_EXIST, TEST_PASSWORD);
            assertTrue(vault.vaultFolder.exists());
            assertTrue(vault.dataFolder.exists());
            assertNotNull(vault.filesystem);
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testUnlockNewSalt() {
        try {
            vault.unlock(TEST_PASSWORD);
            assertNotNull(vault.crypto.getSalt());
        } catch (CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testUnlockExistingSalt() {
        try {
            vault.unlock(TEST_PASSWORD);
            byte[] salt = vault.crypto.getSalt();
            vault.lock();

            vault.unlock(TEST_PASSWORD);
            assertEquals(new String(salt), new String(vault.crypto.getSalt()));
        } catch (CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testAddFile() {
        try {
            byte[] original = new Reader(new File(TEST_VAULT_EXISTS, "filesystem.json")).readBytes();

            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.save();

            byte[] updated = new Reader(new File(TEST_VAULT_EXISTS, "filesystem.json")).readBytes();

            assertNotEquals(new String(original), new String(updated));
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testDeleteFile() {
        try {
            byte[] original = new Reader(new File(TEST_VAULT_EXISTS, "filesystem.json")).readBytes();
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.deleteFile("");       // Try deleting non-existent file
            vault.deleteFile("testReaderWriter.json");
            vault.save();
            byte[] updated = new Reader(new File(TEST_VAULT_EXISTS, "filesystem.json")).readBytes();

            assertEquals(0, vault.dataFolder.listFiles().length);
            assertEquals(new String(original), new String(updated));
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testSaveFile() {
        try {
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.saveFile("testReaderWriter.json", vault.dataFolder);

            assertTrue(new File(vault.dataFolder, "testReaderWriter.json").exists());

            vault.saveFile("", vault.dataFolder);       // Try saving non-existent file
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @AfterAll
    public static void deleteTestVaults() {
        try {
            FileUtils.deleteDirectory(TEST_VAULT_EXISTS);
        } catch (IOException ignored) {}
        try {
            FileUtils.deleteDirectory(TEST_VAULT_NO_EXIST);
        } catch (IOException ignored) {}
    }

}
