package io;

import com.google.gson.JsonObject;
import util.JsonProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        return JsonProvider.parseJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))));
    }

}
