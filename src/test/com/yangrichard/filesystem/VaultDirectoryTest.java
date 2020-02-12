package com.yangrichard.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VaultDirectoryTest {

    private static final String DIR_NAME = "test";

    private VaultDirectory directory;

    @BeforeEach
    public void runBefore() {
        directory = new VaultDirectory(DIR_NAME);
    }

    @Test
    public void testConstructor() {
        assertEquals(DIR_NAME, directory.getName());
        assertEquals(0, directory.size());
        assertEquals(0, directory.getEntries().size());
    }

    @Test
    public void testAddEntry() {
        VaultFile file = new VaultFile("test", "txt", 1024);
        directory.addEntry(file);

        assertEquals(1, directory.getEntries().size());
        assertEquals(1024, directory.size());
    }

    @Test
    public void testDeleteEntry() {
        VaultFile file = new VaultFile("test", "txt", 1024);
        directory.addEntry(file);
        directory.deleteEntry(file);

        assertEquals(0, directory.getEntries().size());
        assertEquals(0, directory.size());
    }

    @Test
    public void testUpdateSizeRecursive() {
        directory.addEntry(new VaultFile("test", "txt", 1024));
        VaultDirectory subDir = new VaultDirectory("sub");
        subDir.addEntry(new VaultFile("test", "txt", 2048));
        directory.addEntry(subDir);

        assertEquals(3072, directory.size());
    }

}
