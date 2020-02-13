package com.yangrichard.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Reader {

    // EFFECTS: reads and returns all bytes from a file
    public static byte[] readBytes(File inputFile) throws IOException {
        byte[] inputBytes = new byte[(int) inputFile.length()];
        FileInputStream in = new FileInputStream(inputFile);
        int numBytes = in.read(inputBytes);
        in.close();
        return inputBytes;
    }

    // EFFECTS: reads JSON data from file
    public static JsonObject readJson(File inputFile) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(new JsonReader(new FileReader(inputFile)), JsonObject.class);
    }

}
