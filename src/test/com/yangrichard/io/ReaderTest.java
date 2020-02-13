package com.yangrichard.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {

    private static final String CORRECT = "{\n  \"testProperty\": \"testValue\"\n}";

    @Test
    public void testReadBytes() {
        try {
            assertEquals(CORRECT, new String(Reader.readBytes(new File("data/testReaderWriter.json"))));
        } catch (IOException e) {
            fail("Read error");
        }
    }

    @Test
    public void testReadJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            assertEquals(CORRECT, gson.toJson(Reader.readJson(new File("data/testReaderWriter.json"))));
        } catch (IOException e) {
            fail("Read error");
        }
    }

}
