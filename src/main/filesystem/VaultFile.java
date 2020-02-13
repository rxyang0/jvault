package filesystem;

import com.google.gson.JsonObject;

// Represents a file entry in the vault filesystem
public class VaultFile extends VaultEntry {

    private String extension;   // without period

    // EFFECTS: constructs VaultFile with name, extension, and size
    public VaultFile(String name, String id, String extension, int size) {
        super(id, name);
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
        fileJson.addProperty("id", id.toString());
        fileJson.addProperty("name", name);
        fileJson.addProperty("extension", extension);
        fileJson.addProperty("size", size);
        return fileJson;
    }

}
