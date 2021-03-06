package com.wkz.framework.functions.retrofit.interceptor;

import com.orhanobut.logger.Logger;
import com.wkz.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FRNetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtils.isConnected()) {
            Logger.i("有网络");
            request = request.newBuilder()
                    //走网络
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            Logger.i("无网络");
            request = request.newBuilder()
                    //只走缓存
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);

        Response response;
        if (NetworkUtils.isConnected()) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            //在线缓存不可读取
            int maxAge = 0;
            response = originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else {
            //无网络时
            //缓存保存时间为365天
            int maxStale = 60 * 60 * 24 * 365;
            response = originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
