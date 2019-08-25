package minibank.quickpay.util;

import com.google.gson.Gson;

/**
 * JSON Utility to perform operation like serialization and deserialization of object
 */
public final class JsonUtil {

    private JsonUtil() {

    }

    private static final Gson gson = new Gson();

    public static <T> T deserialize(String jsonData, Class<T> type) {
        return gson.fromJson(jsonData, type);
    }

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }
}
