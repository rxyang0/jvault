package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Base64;

public class JsonProvider {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    // EFFECTS: returns Base64 decoded version of JsonElement as string
    public static byte[] jsonB64ToByteArr(JsonElement jsonElement) {
        return Base64.getDecoder().decode(jsonElement.getAsString());
    }

}
