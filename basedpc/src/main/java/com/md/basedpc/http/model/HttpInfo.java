package com.md.basedpc.http.model;

import java.io.Serializable;

import okhttp3.Headers;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：http请求信息
 */

public class HttpInfo implements Serializable {

    Headers headers;
    String paramsEntity;
    String requestUrl;
    boolean isCache;

    public HttpInfo(Headers headers, String paramsEntity,String requestUrl) {
        this.headers = headers;
        this.paramsEntity = paramsEntity;
        this.requestUrl = requestUrl;
    }
    public HttpInfo(Headers headers, String paramsEntity,String requestUrl,boolean isCache) {
        this.headers = headers;
        this.paramsEntity = paramsEntity;
        this.requestUrl = requestUrl;
        this.isCache = isCache;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getParamsEntity() {
        return paramsEntity;
    }

    public void setParamsEntity(String paramsEntity) {
        this.paramsEntity = paramsEntity;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }
}
