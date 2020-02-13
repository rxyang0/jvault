package com.yangrichard.filesystem;

import com.google.gson.JsonObject;
import com.yangrichard.io.Reader;

import java.io.*;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private File vault;
    private VaultDirectory root;
    private char[] password;

    // REQUIRES: vaultFolder exists and is a directory
    // EFFECTS: loads existing vault filesystem
    public Vault(File vaultFolder, char[] password) throws IOException {
        vault = vaultFolder;
        boolean unlocked = unlock(password);
        loadEntries(Reader.readJson(new File(vault.getAbsolutePath(), "filesystem.json")));
    }

    // REQUIRES: vaultFolder does not exist
    // EFFECTS: creates new vault filesystem
    public Vault(File vaultFolder, String name, char[] password) {
        vault = vaultFolder;
        boolean makeFolders = vault.mkdirs();
        boolean unlocked = unlock(password);
        root = new VaultDirectory(name, name);
    }

    // MODIFIES: this
    // EFFECTS: decrypts filesystem and sets password
    private boolean unlock(char[] password) {
        if (password != null) {
            this.password = password;
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: locks vault by clearing password
    private void lock() {
        password = null;
    }

    // REQUIRES: filesystem has member "entries"
    // MODIFIES: this
    // EFFECTS: loads filesystem entries from JSON into root directory
    private void loadEntries(JsonObject filesystem) {
        root = new VaultDirectory(filesystem.get("name").getAsString(), filesystem.get("encryptedName").getAsString());
        root.addEntries(filesystem.get("entries").getAsJsonArray());
        root.updateSize();
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
        JsonObject vaultObj = new JsonObject();
        vaultObj.add("filesystem", root.toJson());
        return vaultObj;
    }

}
