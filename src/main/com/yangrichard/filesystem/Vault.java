package com.yangrichard.filesystem;

import java.io.*;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private File vaultFolder;
    private VaultDirectory root;

    // EFFECTS: creates new vault filesystem with path in host filesystem and empty root
    public Vault(File vaultFolder) {
        this.vaultFolder = vaultFolder;
        root = new VaultDirectory("Root", "Root");
    }

    // MODIFIES: dir
    // EFFECTS: add encrypted contents of input file to vault directory
    public void addFile(File inputFile, VaultDirectory dir) {
        String fileName = inputFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        VaultFile file = new VaultFile(fileName, fileName, extension, (int) inputFile.length());
        // Encrypt and set reference to saved file
        dir.addEntry(file);
    }

    // EFFECTS: decrypts and saves contents of file in vault to output directory
    public void saveFile(VaultFile file, File outputDir) {
        File encrypted = new File(vaultFolder.getAbsolutePath() + "/" + root.getPathOfEntry(file));
        // Decrypt and save to outputDir
    }

}
