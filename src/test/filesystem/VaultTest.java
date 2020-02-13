package filesystem;

import io.Reader;
import filesystem.Vault;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class VaultTest {

    private static final File TEST_VAULT_EXISTS = new File("data/test-vault");
    private static final File TEST_VAULT_NO_EXIST = new File("data/test-vault-2");
    private static final char[] TEST_PASSWORD = "test123".toCharArray();

    private Vault vault;

    @BeforeEach
    public void runBefore() {
        try {
            FileUtils.deleteDirectory(TEST_VAULT_EXISTS);
            vault = new Vault(TEST_VAULT_EXISTS, TEST_PASSWORD);
            FileUtils.deleteDirectory(TEST_VAULT_NO_EXIST);
        } catch (IOException e) {
            fail("Load test vault error");
        }
    }

    @Test
    public void testConstructorExistingVault() {
        try {
            vault = new Vault(TEST_VAULT_EXISTS, TEST_PASSWORD);
        } catch (IOException e) {
            fail(e);
        }
        assertNotNull(vault.vault);
        assertNotNull(vault.dataRoot);
    }

    @Test
    public void testConstructorCreateVault() {
        try {
            vault = new Vault(TEST_VAULT_NO_EXIST, TEST_PASSWORD);
        } catch (IOException e) {
            fail(e);
        }
        assertNotNull(vault.vault);
        assertNotNull(vault.dataRoot);
    }

    @Test
    public void testUnlock() {
        vault.unlock(TEST_PASSWORD);
        assertEquals(vault.password, TEST_PASSWORD);
    }


    @Test
    public void testLock() {
        vault.lock();
        assertNull(vault.password);
    }

    @Test
    public void testAddFile() {
        try {
            byte[] original = Reader.readBytes(new File(TEST_VAULT_EXISTS, "filesystem.json"));

            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.save();

            byte[] updated = Reader.readBytes(new File(TEST_VAULT_EXISTS, "filesystem.json"));

            assertNotEquals(new String(original), new String(updated));
        } catch (Exception e) {
            fail(e);
        }
    }

}
