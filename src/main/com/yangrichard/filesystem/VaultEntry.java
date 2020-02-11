package com.yangrichard.filesystem;

// Contains fields and methods common to all entries in a vault filesystem (e.g. files, directories)
public abstract class VaultEntry {

    private String path;
    protected String name;
    protected int size;     // in bytes

    public String getName() {
        return name;
    }

    public int size() {
        return size;
    }

    public String getPath() {
        return path;
    }

}
