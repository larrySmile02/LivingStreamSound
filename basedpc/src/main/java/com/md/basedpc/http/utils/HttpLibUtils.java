package com.md.basedpc.http.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.md.basedpc.http.Interface.YXTHttpResponseHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import okhttp3.Headers;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：参数拼接工具类
 */

public class HttpLibUtils {


    /***
     * get 请求 url 拼接
     *
     * @param url
     * @param params
     * @return
     */
    public static String appendParams(String url, Map<String, Object> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, String.valueOf(params.get(key)));
        }
        return builder.build().toString();
    }

    /**
     * headers 转化
     *
     * @param headermap
     * @return
     */
    public static Headers TranslateMapToHeaders(Map<String, String> headermap) {
        if (headermap == null)
            headermap = new HashMap<>();
        Headers headers = null;
        try {
            headers = Headers.of(headermap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (headers == null) {
            headermap = new HashMap<>();
            headers = Headers.of(headermap);
        }
        return headers;
    }

    /***
     * 验证 url 的合法性 ， 以 http://  https:// 开头  非空
     * @param url
     * @return
     */
    public static boolean isUrlWrong(String url, YXTHttpResponseHandler httpResponseHandler) {
        if (TextUtils.isEmpty(url)) {
            Log.e("TAG", "url is null " + url);
            Log.e("TAG", "url is null : " + url);
            return true;
        }
        if (url.startsWith("http"))
            return false;
        if (httpResponseHandler != null) {
            httpResponseHandler.onFailure(0, null, " 非法url : " + url, null);
        }
        Log.e("TAG", " 非法url : " + url);
        Log.e("TAG", " 非法url : " + url);
        return true;
    }
}
