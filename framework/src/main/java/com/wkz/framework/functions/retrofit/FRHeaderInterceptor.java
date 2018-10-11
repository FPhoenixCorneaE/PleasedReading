package com.wkz.framework.functions.retrofit;

import com.wkz.framework.utils.DeviceIdUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 云端响应头拦截器
 * 用于添加统一请求头和共同参数  请按照自己的需求添加
 */
public class FRHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl httpUrl = originalRequest
                .url()
                .newBuilder()
                // add common parameter
                .addQueryParameter("deviceId", DeviceIdUtils.getUniqueID())
                .build();
        Request authorised = originalRequest
                .newBuilder()
                // add common header
                .addHeader("userAgent", "android")
                .url(httpUrl)
                .build();
        return chain.proceed(authorised);
    }
}
