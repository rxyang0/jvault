package com.yangrichard.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VaultFileTest {

    private static final String FILE_NAME = "test";
    private static final String FILE_NAME_ENCRYPTED = "qwerty";
    private static final String FILE_EXTENSION = "txt";
    private static final int FILE_SIZE = 1024;

    private VaultFile file;

    @BeforeEach
    public void runBefore() {
        file = new VaultFile(FILE_NAME, FILE_NAME_ENCRYPTED, FILE_EXTENSION, FILE_SIZE);
    }

    @Test
    public void testConstructor() {
        assertEquals(FILE_NAME, file.getName());
        assertEquals(FILE_NAME_ENCRYPTED, file.getEncryptedName());
        assertEquals(FILE_EXTENSION, file.getExtension());
        assertEquals(FILE_SIZE, file.size());
    }

}
