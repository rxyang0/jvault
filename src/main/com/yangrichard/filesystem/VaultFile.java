package com.yangrichard.filesystem;

// Represents a file entry in the vault filesystem
public class VaultFile extends VaultEntry {

    private String extension;   // without period

    // EFFECTS: constructs VaultFile with name, extension, and size
    public VaultFile(String name, String encryptedName, String extension, int size) {
        super(name, encryptedName);
        this.extension = extension;
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

}
