package filesystem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import filesystem.VaultDirectory;
import filesystem.VaultFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VaultDirectoryTest {

    private static final String DIR_NAME = "test";
    private static final String DIR_NAME_ENCRYPTED = "qwerty";
    private static final JsonObject TEST_OBJ = new JsonObject();
    static {
        TEST_OBJ.addProperty("name", "test");
        TEST_OBJ.addProperty("encryptedName", "test");
        TEST_OBJ.addProperty("extension", "test");
        TEST_OBJ.addProperty("size", 1024);
    }

    private VaultDirectory directory;

    @BeforeEach
    public void runBefore() {
        directory = new VaultDirectory(DIR_NAME, DIR_NAME_ENCRYPTED);
    }

    @Test
    public void testConstructor() {
        assertEquals(DIR_NAME, directory.getName());
        assertEquals(0, directory.size());
        assertEquals(0, directory.getEntries().size());
    }

    @Test
    public void testAddEntry() {
        VaultFile file = new VaultFile("test", "test","txt", 1024);
        directory.addEntry(file);

        assertEquals(1, directory.getEntries().size());
        assertEquals(1024, directory.size());
    }

    @Test
    public void testAddEntriesSingleFile() {
        JsonArray testArr = new JsonArray();
        testArr.add(TEST_OBJ);
        directory.addEntries(testArr);

        assertEquals(1, directory.getEntries().size());
        assertEquals(TEST_OBJ.get("size").getAsInt(), directory.size());
    }

    @Test
    public void testAddEntriesDirectory() {
        JsonArray testArr = new JsonArray();

        JsonArray testDirEntries = new JsonArray();
        testDirEntries.add(TEST_OBJ);
        testDirEntries.add(TEST_OBJ);

        JsonObject testDir = new JsonObject();
        testDir.addProperty("name", "test");
        testDir.addProperty("encryptedName", "test");
        testDir.addProperty("size", 32);
        testDir.add("entries", testDirEntries);

        testArr.add(testDir);
        directory.addEntries(testArr);

        assertEquals(TEST_OBJ.get("size").getAsInt() * 2, directory.size());
    }

    @Test
    public void testDeleteEntry() {
        VaultFile file = new VaultFile("test", "test","txt", 1024);
        directory.addEntry(file);
        directory.deleteEntry(file);

        assertEquals(0, directory.getEntries().size());
        assertEquals(0, directory.size());
    }

    @Test
    public void testUpdateSizeRecursive() {
        directory.addEntry(new VaultFile("test", "test","txt", 1024));
        VaultDirectory subDir = new VaultDirectory("sub", "sub");
        subDir.addEntry(new VaultFile("test", "test","txt", 2048));
        directory.addEntry(subDir);

        assertEquals(3072, directory.size());
    }

    @Test
    public void testGetPathOfEntry() {
        VaultFile one = new VaultFile("one", "one","txt", 1024);
        directory.addEntry(one);

        VaultDirectory subDir = new VaultDirectory("sub", "sub");
        VaultFile two = new VaultFile("two", "two", "txt", 2048);
        subDir.addEntry(two);
        directory.addEntry(subDir);

        assertEquals("one", directory.getPathOfEntry(one));
        assertEquals("sub/two", directory.getPathOfEntry(two));
    }

    @Test
    public void testGetPathOfEntryNotFound() {
        VaultFile one = new VaultFile("one", "one","txt", 1024);

        assertNull(directory.getPathOfEntry(one));
    }

    @Test
    public void testToJson() {
        assertNotNull(directory.toJson());
    }

}
