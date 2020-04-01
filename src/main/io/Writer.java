package io;

import com.google.gson.JsonObject;
import util.JsonProvider;

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
    public void writeJson(JsonObject obj) throws IOException {
        writeBytes(JsonProvider.saveJson(obj).getBytes());
    }

}
