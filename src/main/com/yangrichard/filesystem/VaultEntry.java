package com.yangrichard.filesystem;

// Contains fields and methods common to all entries in a vault filesystem (e.g. files, directories)
public abstract class VaultEntry {

    protected String name;          // plaintext name of file or directory
    protected String encryptedName;
    protected int size;             // in bytes

    // EFFECTS: sets random UUID
    public VaultEntry(String name, String encryptedName) {
        this.name = name;
        this.encryptedName = encryptedName;
    }

    public int size() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedName() {
        return encryptedName;
    }

}
