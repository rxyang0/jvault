package util;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class JsonProviderTest {

    private JsonProvider json;

    @BeforeEach
    public void runBefore() {
        json = new JsonProvider();
    }

    @Test
    public void testJsonB64ToByteArr() {
        JsonObject testObj = new JsonObject();
        testObj.addProperty("salt", Base64.getEncoder().encodeToString("saltValue".getBytes()));

        assertArrayEquals("saltValue".getBytes(), JsonProvider.jsonB64ToByteArr(testObj.get("salt")));
    }

}
