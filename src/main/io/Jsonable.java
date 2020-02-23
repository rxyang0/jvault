package io;

import com.google.gson.JsonObject;

// Interface for any object whose data should be saved in JSON
public interface Jsonable {

    // EFFECTS: returns JsonObject containing data of this object
    JsonObject toJson();

}
