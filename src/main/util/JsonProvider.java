package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Base64;

public class JsonProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    // EFFECTS: returns Base64 decoded version of JsonElement as string
    public static byte[] jsonB64ToByteArr(JsonElement jsonElement) {
        return Base64.getDecoder().decode(jsonElement.getAsString());
    }

    // EFFECTS: parses string for JSON
    public static JsonObject parseJson(String str) {
        return GSON.fromJson(str, JsonObject.class);
    }

    // EFFECTS: converts JsonObject to JSON string
    public static String saveJson(JsonObject obj) {
        return GSON.toJson(obj);
    }

    public static Gson getGson() {
        return GSON;
    }

}
