package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.Reader;
import io.Writer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriterTest {

    private static final File TEST_FILE = new File("data/testReaderWriter.json");
    private static final String CORRECT = "{\n  \"testProperty\": \"testValue\"\n}";

    @Test
    public void testWriteBytes() {
        try {
            Writer.writeBytes(CORRECT.getBytes(), TEST_FILE);
            assertEquals(CORRECT, new String(Reader.readBytes(TEST_FILE)));
        } catch (IOException e) {
            fail("Write error");
        }
    }

    @Test
    public void testWriteJson() {
        JsonObject correctJson = new JsonObject();
        correctJson.addProperty("testProperty", "testValue");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer.writeJson(correctJson, TEST_FILE);
            assertEquals(CORRECT, gson.toJson(Reader.readJson(TEST_FILE)));
        } catch (IOException e) {
            fail("Write error");
        }
    }

}
