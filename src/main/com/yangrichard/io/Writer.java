package com.yangrichard.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Writer {

    public static void writeBytes(byte[] outputBytes, File outputFile) throws IOException {
        if (outputFile.isDirectory()) {
            throw new FileNotFoundException();
        }
        FileOutputStream out = new FileOutputStream(outputFile);
        out.write(outputBytes);
        out.close();
    }

}
