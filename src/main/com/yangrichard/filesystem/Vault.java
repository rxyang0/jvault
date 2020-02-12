package com.yangrichard.filesystem;

import com.google.gson.JsonObject;

import java.io.*;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private File vault;
    private VaultDirectory root;
    private char[] password;

    // EFFECTS: creates new vault filesystem with path in host filesystem and empty root
    public Vault(File vaultFolder, char[] password) {
        vault = vaultFolder;
        if (! vaultFolder.exists()) {
            boolean mkdirs = vault.mkdirs();
            root = new VaultDirectory("Root", "Root");
        } else {
            boolean success = unlock(password);
        }
    }

    // MODIFIES: this
    private boolean unlock(char[] password) {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: locks vault by clearing password
    private void lock() {
        password = null;
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
        File encrypted = new File(vault.getAbsolutePath() + "/" + root.getPathOfEntry(file));
        // Decrypt and save to outputDir
    }

    // EFFECTS: returns JsonObject containing filesystem data of this vault
    private JsonObject exportToJson() {
        JsonObject obj = new JsonObject();
        obj.add("filesystem", root.toJson());
        return obj;
    }

}
