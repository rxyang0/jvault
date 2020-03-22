package filesystem;

import com.google.gson.JsonObject;
import exceptions.CryptoException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.JsonProvider;

import java.io.File;
import java.io.IOException;

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
            assertTrue(vault.getVaultFolder().exists());
            assertTrue(vault.getDataFolder().exists());
            assertNotNull(vault.getIndex());
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testConstructorCreateVault() {
        try {
            vault = new Vault(TEST_VAULT_NO_EXIST, TEST_PASSWORD);
            assertTrue(vault.getVaultFolder().exists());
            assertTrue(vault.getDataFolder().exists());
            assertNotNull(vault.getIndex());
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testUnlockNewSalt() {
        try {
            vault.unlock(TEST_PASSWORD);
            assertNotNull(vault.crypto);
        } catch (CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testUnlockExistingSalt() {
        try {
            vault.unlock(TEST_PASSWORD);
            String saltB64 = vault.crypto.toJson().get("salt").getAsString();
            vault.lock();       // Destroys and clears CryptoProvider

            vault.unlock(TEST_PASSWORD);
            assertEquals(saltB64, vault.crypto.toJson().get("salt").getAsString());
        } catch (CryptoException | IOException e) {
            fail(e);
        }
    }

    @Test
    public void testAddFile() {
        try {
            JsonObject original = vault.getIndex();
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.sync();
            JsonObject updated = vault.getIndex();

            assertEquals(1, vault.getDataFolder().listFiles().length);
            assertNotEquals(JsonProvider.GSON.toJson(original), JsonProvider.GSON.toJson(updated));
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testDeleteFile() {
        try {
            JsonObject original = vault.getIndex();
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.delete(vault.getRoot().getEntries().get(0), vault.getRoot());
            vault.sync();
            JsonObject updated = vault.getIndex();

            assertEquals(0, vault.getDataFolder().listFiles().length);
            assertEquals(JsonProvider.GSON.toJson(original), JsonProvider.GSON.toJson(updated));
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testDeleteDir() {
        try {
            vault.createDir("test", vault.getRoot());
            File dirOnDisk = new File(vault.getDataFolder(), vault.getRoot().getEntries().get(0).getId());
            vault.delete(vault.getRoot().getEntries().get(0), vault.getRoot());

            assertEquals(vault.getRoot().getEntries().size(), 0);
            assertFalse(dirOnDisk.exists());
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    public void testCreateDir() {
        try {
            vault.createDir("test", vault.getRoot());

            assertEquals(vault.getRoot().getEntries().size(), 1);
            assertTrue(new File(vault.getDataFolder(), vault.getRoot().getEntries().get(0).getId()).exists());
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    public void testSaveFile() {
        try {
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            vault.saveFile("testReaderWriter.json", vault.getDataFolder());

            assertTrue(new File(vault.getDataFolder(), "testReaderWriter.json").exists());

            vault.saveFile("", vault.getDataFolder());       // Try saving non-existent file
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testSaveFileToBytes() {
        try {
            vault.addFile(new File("data/testReaderWriter.json"), vault.getRoot());
            assertTrue(vault.save(vault.getRoot().getEntries().get(0)).length > 0);
        } catch (IOException | CryptoException e) {
            fail(e);
        }
    }

    @Test
    public void testSaveDir() {
        try {
            vault.createDir("test", vault.getRoot());
            assertTrue(vault.save(vault.getRoot().getEntries().get(0)).length == 0);
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
