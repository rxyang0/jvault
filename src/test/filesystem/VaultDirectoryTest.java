package filesystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VaultDirectoryTest {

    private static final String TEST_ID_0 = "00000000-0000-0000-0000-000000000000";
    private static final String TEST_ID_1 = "11111111-1111-1111-1111-111111111111";
    private static final String TEST_ID_2 = "22222222-2222-2222-2222-222222222222";
    private static final String TEST_ID_3 = "33333333-3333-3333-3333-333333333333";

    private static final String DIR_ID = TEST_ID_0;
    private static final String DIR_NAME = "test";
    private static final int DIR_SIZE = 0;
    private static final VaultFile TEST_FILE = new VaultFile(TEST_ID_1, "test","txt", 1024);
    private static final JsonObject TEST_OBJ_FILE = new JsonObject();
    static {
        TEST_OBJ_FILE.addProperty("id", TEST_ID_1);
        TEST_OBJ_FILE.addProperty("name", "test");
        TEST_OBJ_FILE.addProperty("extension", "txt");
        TEST_OBJ_FILE.addProperty("size", 1024);
    }

    private VaultDirectory directory;

    @BeforeEach
    public void runBefore() {
        directory = new VaultDirectory(DIR_ID, DIR_NAME);
    }

    @Test
    public void testConstructor() {
        assertEquals(DIR_ID, directory.getId());
        assertEquals(DIR_NAME, directory.getName());
        assertEquals(0, directory.size());
        assertEquals(0, directory.getEntries().size());
    }

    @Test
    public void testAddEntry() {
        directory.addEntry(TEST_FILE);

        assertEquals(1, directory.getEntries().size());
        assertEquals(TEST_FILE.size(), directory.size());
    }

    @Test
    public void testDeleteEntry() {
        directory.addEntry(TEST_FILE);
        directory.deleteEntry(TEST_FILE);

        assertEquals(0, directory.getEntries().size());
        assertEquals(0, directory.size());
    }

    @Test
    public void testAddEntriesSingleDepth() {
        JsonArray testArr = new JsonArray();
        testArr.add(TEST_OBJ_FILE);
        testArr.add(TEST_OBJ_FILE);
        directory.addEntries(testArr);

        assertEquals(2, directory.getEntries().size());
        assertEquals(TEST_OBJ_FILE.get("size").getAsInt() * 2, directory.size());
    }

    @Test
    public void testAddEntriesDoubleDepth() {
        JsonArray testArr = new JsonArray();
        testArr.add(TEST_OBJ_FILE);

        JsonObject testDir = new JsonObject();
        testDir.addProperty("id", DIR_ID);
        testDir.addProperty("name", DIR_NAME);
        testDir.addProperty("size", 0);

        JsonArray testArrLower = new JsonArray();
        testArrLower.add(TEST_OBJ_FILE);
        testArrLower.add(TEST_OBJ_FILE);

        testDir.add("entries", testArrLower);

        testArr.add(testDir);

        directory.addEntries(testArr);

        assertEquals(2, directory.getEntries().size());
        assertEquals(TEST_OBJ_FILE.get("size").getAsInt() * 3, directory.size());
    }

    @Test
    public void testUpdateSizeRecursive() {
        directory.addEntry(TEST_FILE);

        VaultDirectory subDir = new VaultDirectory(DIR_ID, DIR_NAME);
        subDir.addEntry(TEST_FILE);

        directory.addEntry(subDir);

        assertEquals(TEST_FILE.size() * 2, directory.size());
    }

    @Test
    public void testGetPathOfEntry() {
        VaultFile one = new VaultFile(TEST_ID_1, "one","txt", 1024);
        directory.addEntry(one);

        VaultDirectory subDir = new VaultDirectory(TEST_ID_2, DIR_NAME);
        VaultFile two = new VaultFile(TEST_ID_3, "two", "txt", 2048);
        subDir.addEntry(two);

        directory.addEntry(subDir);

        assertEquals("", directory.getPathOfEntry(DIR_ID));
        assertEquals(TEST_ID_1, directory.getPathOfEntry(TEST_ID_1));
        assertEquals(TEST_ID_2, directory.getPathOfEntry(TEST_ID_2));
        assertEquals(TEST_ID_2 + "/" + TEST_ID_3, directory.getPathOfEntry(TEST_ID_3));
    }

    @Test
    public void testGetPathOfEntryNotFound() {
        assertNull(directory.getPathOfEntry("123"));
    }

    @Test
    public void testToJson() {
        directory.addEntry(TEST_FILE);

        VaultDirectory subDir = new VaultDirectory(DIR_ID, DIR_NAME);
        subDir.addEntry(TEST_FILE);

        directory.addEntry(subDir);

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        assertEquals(524, gson.toJson(directory.toJson()).length());
    }

}
