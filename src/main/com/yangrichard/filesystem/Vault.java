package com.yangrichard.filesystem;

import com.google.gson.JsonObject;
import com.yangrichard.io.Reader;
import com.yangrichard.io.Writer;
import com.yangrichard.util.CryptoProvider;
import org.apache.commons.codec.binary.Base32;

import java.io.*;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    protected File vault;
    protected File dataRoot;
    private VaultDirectory root;
    protected char[] password;

    // EFFECTS: loads existing vault filesystem, or creates new vault if folder does not exist
    public Vault(File vaultFolder, char[] password) throws IOException {
        vault = vaultFolder;
        dataRoot = new File(vault, "data");
        if (vaultFolder.exists()) {
            unlock(password);
            loadEntries(Reader.readJson(new File(vault.getAbsolutePath(), "filesystem.json")));
        } else {
            boolean makeFolders = vault.mkdirs();
            unlock(password);
            root = new VaultDirectory(vaultFolder.getName(), vaultFolder.getName());
            boolean makeDataFolder = dataRoot.mkdir();
            save();
        }
    }

    // MODIFIES: this
    // EFFECTS: decrypts filesystem and sets password
    protected void unlock(char[] password) {
        this.password = password;
    }

    // MODIFIES: this
    // EFFECTS: locks vault by clearing password
    protected void lock() {
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
    public void addFile(File inputFile, VaultDirectory dir) throws Exception {
        String fileName = inputFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        VaultFile file = new VaultFile(fileName, fileName, extension, (int) inputFile.length());

        // TODO: add file in specified VaultDirectory, rather than root
        CryptoProvider crypto = new CryptoProvider(password);
        byte[] encryptedBytes = crypto.encrypt(Reader.readBytes(inputFile));
        String encryptedFileName = new Base32().encodeAsString(crypto.getLastSaltAndIV()) + ".txt";
        crypto.destroy();
        File encryptedFile = new File(dataRoot.getAbsolutePath(), encryptedFileName);
        Writer.writeBytes(encryptedBytes, encryptedFile);
        dir.addEntry(file);
    }

//    // EFFECTS: decrypts and saves contents of file in vault to output directory
//    public void saveFile(VaultFile file, File outputDir) {
//        File encrypted = new File(vault.getAbsolutePath() + "/" + root.getPathOfEntry(file));
//        // Decrypt and save to outputDir
//    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void save() throws IOException {
        Writer.writeJson(root.toJson(), new File(vault, "filesystem.json"));
    }

    public VaultDirectory getRoot() {
        return root;
    }

}
