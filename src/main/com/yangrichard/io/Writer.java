package com.yangrichard.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

public class Writer {

    // EFFECTS: writes all bytes to file
    public static void writeBytes(byte[] outputBytes, File outputFile) throws IOException {
        FileOutputStream out = new FileOutputStream(outputFile);
        out.write(outputBytes);
        out.close();
    }

    // EFFECTS: writes JSON data to file
    public static void writeJson(JsonObject outObj, File outputFile) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writeBytes(gson.toJson(outObj).getBytes(), outputFile);
    }

}
