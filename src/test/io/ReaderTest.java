package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {

    private static final File TEST_FILE = new File("data/testReaderWriter.json");
    private static final String CORRECT = "{\n  \"testProperty\": \"testValue\"\n}";

    private Reader reader;

    @BeforeEach
    public void runBefore() {
        reader = new Reader(TEST_FILE);
    }

    @Test
    public void testReadBytes() {
        try {
            assertEquals(CORRECT, new String(reader.readBytes()));
        } catch (IOException e) {
            fail("Read error");
        }
    }

    @Test
    public void testReadJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try {
            assertEquals(CORRECT, gson.toJson(reader.readJson()));
        } catch (IOException e) {
            fail("Read error");
        }
    }

}
