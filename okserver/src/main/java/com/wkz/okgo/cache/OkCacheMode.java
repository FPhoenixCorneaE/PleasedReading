package com.wkz.okgo.cache;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 缓存模式
 */
@IntDef({
        OkCacheMode.DEFAULT,
        OkCacheMode.NO_CACHE,
        OkCacheMode.REQUEST_FAILED_READ_CACHE,
        OkCacheMode.IF_NONE_CACHE_REQUEST,
        OkCacheMode.FIRST_CACHE_THEN_REQUEST
})
@Retention(RetentionPolicy.SOURCE)
public @interface OkCacheMode {
    /**
     * 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
     */
    int DEFAULT = 0;

    /**
     * 不使用缓存
     */
    int NO_CACHE = 1;

    /**
     * 请求网络失败后，读取缓存
     */
    int REQUEST_FAILED_READ_CACHE = 2;

    /**
     * 如果缓存不存在才请求网络，否则使用缓存
     */
    int IF_NONE_CACHE_REQUEST = 3;

    /**
     * 先使用缓存，不管是否存在，仍然请求网络
     */
    int FIRST_CACHE_THEN_REQUEST = 4;
}
