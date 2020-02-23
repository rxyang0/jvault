package filesystem;

import com.google.gson.JsonObject;
import io.Jsonable;

import java.util.UUID;

// Contains fields and methods common to all entries in a vault filesystem (e.g. files, directories)
public abstract class VaultEntry implements Jsonable {

    private String name;          // plaintext name of file or directory
    private UUID id;
    protected int size;             // in bytes

    // EFFECTS: sets id and name
    public VaultEntry(String id, String name) {
        this.id = UUID.fromString(id);
        this.name = name;
    }

    // EFFECTS: returns new JsonObject or JsonArray containing data of VaultEntry
    @Override
    public abstract JsonObject toJson();

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public int size() {
        return size;
    }

}
