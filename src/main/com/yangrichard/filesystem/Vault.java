package com.yangrichard.filesystem;

import com.google.gson.JsonObject;
import com.yangrichard.io.Reader;
import com.yangrichard.io.Writer;
import com.yangrichard.util.CryptoProvider;
import org.apache.commons.codec.binary.Base32;

import java.io.*;

// Handles all filesystem entries and functionality in a vault
public class Vault {

    private File vault;
    private File dataRoot;
    private VaultDirectory root;
    private char[] password;

    // EFFECTS: loads existing vault filesystem, or creates new vault if folder does not exist
    public Vault(File vaultFolder, char[] password) throws IOException {
        if (vaultFolder.exists()) {
            vault = vaultFolder;
            boolean unlocked = unlock(password);
            loadEntries(Reader.readJson(new File(vault.getAbsolutePath(), "filesystem.json")));
        } else {
            vault = vaultFolder;
            boolean makeFolders = vault.mkdirs();
            boolean unlocked = unlock(password);
            root = new VaultDirectory(vaultFolder.getName(), vaultFolder.getName());
            boolean makeDataFolder = dataRoot.mkdir();
            save();
        }
        dataRoot = new File(vault, "data");
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

        // TODO: add file in specified VaultDirectory, rather than root
        try {
            CryptoProvider crypto = new CryptoProvider(password);
            byte[] encryptedBytes = crypto.encrypt(Reader.readBytes(inputFile));
            String encryptedFileName = new Base32().encodeAsString(crypto.getLastSaltAndIV()) + ".txt";
            crypto.destroy();
            File encryptedFile = new File(dataRoot.getAbsolutePath(), encryptedFileName);
            Writer.writeBytes(encryptedBytes, encryptedFile);
            dir.addEntry(file);
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error in encryption");
            e.printStackTrace();
        }
    }

//    // EFFECTS: decrypts and saves contents of file in vault to output directory
//    public void saveFile(VaultFile file, File outputDir) {
//        File encrypted = new File(vault.getAbsolutePath() + "/" + root.getPathOfEntry(file));
//        // Decrypt and save to outputDir
//    }

    // EFFECTS: saves filesystem data of this vault to filesystem.json in root folder
    public void save() {
        try {
            Writer.writeJson(root.toJson(), new File(vault, "filesystem.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VaultDirectory getRoot() {
        return root;
    }

}
