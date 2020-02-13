package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Reader {

    private File file;

    public Reader(File inputFile) {
        file = inputFile;
    }

    // EFFECTS: reads and returns all bytes from a file
    public byte[] readBytes() throws IOException {
        byte[] inputBytes = new byte[(int) file.length()];
        FileInputStream in = new FileInputStream(file);
        int numBytes = in.read(inputBytes);
        in.close();
        return inputBytes;
    }

    // EFFECTS: reads JSON data from file
    public JsonObject readJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
    }

}
