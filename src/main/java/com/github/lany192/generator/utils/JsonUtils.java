package com.github.lany192.generator.utils;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class JsonUtils {
    /**
     * json转对象
     */
    public static <T> T json2object(String json, Class<T> cls) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        T t = null;
        try {
            t = new Gson().fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 对象转json
     */
    public static String object2json(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> List<T> json2list(String json, Class<T[]> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }
}