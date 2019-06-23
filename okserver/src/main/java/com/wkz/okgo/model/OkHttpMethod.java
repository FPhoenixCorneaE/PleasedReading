package com.wkz.okgo.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求方式
 */
@StringDef({
        OkHttpMethod.GET,
        OkHttpMethod.POST,
        OkHttpMethod.PUT,
        OkHttpMethod.DELETE,
        OkHttpMethod.HEAD,
        OkHttpMethod.PATCH,
        OkHttpMethod.OPTIONS,
        OkHttpMethod.TRACE
})
@Retention(RetentionPolicy.SOURCE)
public @interface OkHttpMethod {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String HEAD = "HEAD";
    String PATCH = "PATCH";
    String OPTIONS = "OPTIONS";
    String TRACE = "TRACE";
}
