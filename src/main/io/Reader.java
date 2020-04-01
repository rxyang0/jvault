package io;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import util.JsonProvider;

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
        return JsonProvider.getGson().fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
    }

}
