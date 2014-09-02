package org.mikezerosix.util;

import com.google.gson.Gson;
import spark.Request;

public class JsonUtil {
    public static final Gson gson =  new Gson();
    public static String toJson(Object o) {
        return gson.toJson(o);
    }
    public static <T> T fromJson(Request request, Class<T> clazz) {
        return gson.fromJson(request.body(), clazz);
    }

}
