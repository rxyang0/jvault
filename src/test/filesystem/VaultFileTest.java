package filesystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VaultFileTest {

    private static final String FILE_ID = "11111111-1111-1111-1111-111111111111";
    private static final String FILE_NAME = "test.txt";
    private static final int FILE_SIZE = 1024;

    private VaultFile file;

    @BeforeEach
    public void runBefore() {
        file = new VaultFile(FILE_ID, FILE_NAME, FILE_SIZE);
    }

    @Test
    public void testConstructor() {
        assertEquals(FILE_ID, file.getId());
        assertEquals(FILE_NAME, file.getName());
        assertEquals(FILE_SIZE, file.size());
    }

    @Test
    public void testToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        assertEquals(88, gson.toJson(file.toJson()).length());
    }

}
