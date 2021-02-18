package com.md.basedpc.http.utils;

import com.md.basedpc.application.AppManager;
import com.md.basedpc.http.model.ACacheModel;

import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

/**
 * Created by ryan
 *
 * @date 2019/10/18.
 * description：网络框架缓存单例
 */
public class HttpCacheUtil {
    private static HttpCacheUtil instance;
    private ACache cache = null;

    public HttpCacheUtil() {
        cache = ACache.get(AppManager.getAppManager().getAppContext());
    }

    public static HttpCacheUtil getInstance() {
        if (instance == null) {
            synchronized (HttpCacheUtil.class) {
                if (instance == null) {
                    instance = new HttpCacheUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 文件缓存
     *
     * @param response
     * @param code
     * @param responseBody
     */
    public void putACache(String clientKey, Response response, int code, String responseBody) {
        String requestMethod = response.request().method();
        if (HttpMethod.invalidatesCache(response.request().method())) {
            return;
        }
        if (!"GET".equals(requestMethod)) {
            return;
        }
        if (cache != null) {
            String url = response.request().url().toString();
            cache.put(clientKey + url, new ACacheModel(code, responseBody));
        }
    }


    /***
     * 根据url 取出缓存
     *
     * @param url
     * @return
     */
    public ACacheModel getACache(String url) {
        if (cache != null) {
            Object object = cache.getAsObject(url);
            if (object instanceof ACacheModel) {
                return (ACacheModel) object;
            }
        }
        return null;
    }

    /**
     * 通过url地址清楚缓存
     * @param url
     */
    public void cleanCacheByUrl(String url) {
        if (cache != null) {
            cache.remove(url);
        }
    }

    /**
     * 清楚所有缓存
     */
    public void cleanAllCache() {
        if (cache != null) {
            cache.clear();
        }
    }
}
