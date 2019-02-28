package com.wkz.okgo.exception;

public class OkCacheException extends Exception {

    private static final long serialVersionUID = -4533345444784668805L;

    public static OkCacheException NON_OR_EXPIRE(String cacheKey) {
        return new OkCacheException("cacheKey = " + cacheKey + " ,can't find cache by cacheKey, or cache has expired!");
    }

    public static OkCacheException NON_AND_304(String cacheKey) {
        return new OkCacheException("the http response code is 304, but the cache with cacheKey = " + cacheKey + " is null or expired!");
    }

    public OkCacheException(String detailMessage) {
        super(detailMessage);
    }
}
