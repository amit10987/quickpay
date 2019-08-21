package com.minibank.quickpay.util;

import com.google.gson.Gson;

public class JsonUtil {

    private static Gson gson = new Gson();

    public static <T> T deserialize(String jsonData, Class<T> type) {
        return gson.fromJson(jsonData, type);
    }

    public static String serialize(Object obj){
        return gson.toJson(obj);
    }
}
