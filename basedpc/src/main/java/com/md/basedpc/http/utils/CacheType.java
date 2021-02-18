package com.md.basedpc.http.utils;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：网络缓存状态枚举
 */
public enum CacheType {

    CACHED_ELSE_NETWORK("一次请求先从缓存读取，再从网络读取", 1),
    ONLY_NETWORK("一次请求只从网络读取", 0);

    private String decription;
    private int cacheType;

    CacheType(String decription, int cacheType) {
        this.decription = decription;
        this.cacheType = cacheType;
    }

    public String getDecription() {
        return decription;
    }

    public int getCacheType() {
        return cacheType;
    }
}
