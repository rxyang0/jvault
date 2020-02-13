package com.yangrichard.filesystem;

import com.google.gson.JsonObject;

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

    // EFFECTS: returns JsonObject containing data this entry
    @Override
    public JsonObject toJson() {
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("name", name);
        fileJson.addProperty("encryptedName", encryptedName);
        fileJson.addProperty("extension", extension);
        fileJson.addProperty("size", size);
        return fileJson;
    }

}
