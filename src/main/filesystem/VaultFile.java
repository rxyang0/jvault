package filesystem;

import com.google.gson.JsonObject;

// Represents a file entry in the vault filesystem
public class VaultFile extends VaultEntry {

    // EFFECTS: sets id, name, extension, size
    public VaultFile(String id, String name, int size) {
        super(id, name);
        this.size = size;
    }

    // EFFECTS: returns JsonObject containing data this entry
    @Override
    public JsonObject toJson() {
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("id", getId());
        fileJson.addProperty("name", getName());
        fileJson.addProperty("size", size);
        return fileJson;
    }

}
