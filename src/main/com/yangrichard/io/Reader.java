package com.yangrichard.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Reader {

    // EFFECTS: reads and returns all bytes from a file
    public static byte[] readBytes(File inputFile) throws IOException {
        if (inputFile.isDirectory()) {
            throw new FileNotFoundException();
        }
        byte[] inputBytes = new byte[(int) inputFile.length()];
        FileInputStream in = new FileInputStream(inputFile);
        int numBytes = in.read(inputBytes);
        in.close();
        return inputBytes;
    }

}
