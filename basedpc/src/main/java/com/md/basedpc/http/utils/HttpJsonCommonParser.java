package com.md.basedpc.http.utils;

import com.google.gson.Gson;
import com.md.basedpc.json.JsonUtils;

import java.lang.reflect.Type;

/**
 * Created by ryan
 * @date 2019/10/17.
 * description：gson二级封装
 */
public class HttpJsonCommonParser {

    public static Gson getGson() {
        return JsonUtils.getGson();
    }

    public static <T> Object getResponse(String mStr, Class<T> mClass) {
        return JsonUtils.getGson().fromJson(mStr, mClass);
    }

    public static Object getResponse(String mStr, Type type) {
        return JsonUtils.getGson().fromJson(mStr, type);
    }

    public static String getString(Object obj) {
        return JsonUtils.getGson().toJson(obj);
    }


}
