package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

public class Writer {

    private File file;

    public Writer(File outputFile) {
        file = outputFile;
    }

    // EFFECTS: writes all bytes to file
    public void writeBytes(byte[] outputBytes) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(outputBytes);
        out.close();
    }

    // EFFECTS: writes JSON data to file
    public void writeJson(JsonObject outObj) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writeBytes(gson.toJson(outObj).getBytes());
    }

}
