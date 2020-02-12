package com.yangrichard.filesystem;

import java.util.UUID;

// Contains fields and methods common to all entries in a vault filesystem (e.g. files, directories)
public abstract class VaultEntry {

    protected String name;
    protected int size;     // in bytes
    private UUID id;        // used as filename of encrypted file

    // EFFECTS: sets random UUID
    public VaultEntry() {
        id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public int size() {
        return size;
    }

    public UUID getId() {
        return id;
    }

}
