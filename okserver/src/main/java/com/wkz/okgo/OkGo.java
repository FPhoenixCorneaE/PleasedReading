package com.wkz.okgo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.cache.OkCacheMode;
import com.wkz.okgo.cookie.OkCookieJarImpl;
import com.wkz.okgo.https.OkHttpsUtils;
import com.wkz.okgo.interceptor.OkHttpLoggingInterceptor;
import com.wkz.okgo.model.OkHttpHeaders;
import com.wkz.okgo.model.OkHttpParams;
import com.wkz.okgo.request.OkDeleteRequest;
import com.wkz.okgo.request.OkGetRequest;
import com.wkz.okgo.request.OkHeadRequest;
import com.wkz.okgo.request.OkOptionsRequest;
import com.wkz.okgo.request.OkPatchRequest;
import com.wkz.okgo.request.OkPostRequest;
import com.wkz.okgo.request.OkPutRequest;
import com.wkz.okgo.request.OkTraceRequest;
import com.wkz.okgo.utils.OkHttpUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 网络请求的入口类
 */
public class OkGo {
    private static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private OkHttpParams mCommonParams;       //全局公共请求参数
    private OkHttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private int mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    private OkGo() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = OkCacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = OkCacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(OkHttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        OkHttpsUtils.SSLParams sslParams = OkHttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(OkHttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    public static OkGo getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static OkGo holder = new OkGo();
    }

    /**
     * get请求
     */
    public static <T> OkGetRequest<T> get(String url) {
        return new OkGetRequest<>(url);
    }

    /**
     * post请求
     */
    public static <T> OkPostRequest<T> post(String url) {
        return new OkPostRequest<>(url);
    }

    /**
     * put请求
     */
    public static <T> OkPutRequest<T> put(String url) {
        return new OkPutRequest<>(url);
    }

    /**
     * head请求
     */
    public static <T> OkHeadRequest<T> head(String url) {
        return new OkHeadRequest<>(url);
    }

    /**
     * delete请求
     */
    public static <T> OkDeleteRequest<T> delete(String url) {
        return new OkDeleteRequest<>(url);
    }

    /**
     * options请求
     */
    public static <T> OkOptionsRequest<T> options(String url) {
        return new OkOptionsRequest<>(url);
    }

    /**
     * patch请求
     */
    public static <T> OkPatchRequest<T> patch(String url) {
        return new OkPatchRequest<>(url);
    }

    /**
     * trace请求
     */
    public static <T> OkTraceRequest<T> trace(String url) {
        return new OkTraceRequest<>(url);
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public OkGo init(Application app) {
        context = app;
        return this;
    }

    /**
     * 获取全局上下文
     */
    public Context getContext() {
        OkHttpUtils.checkNotNull(context, "please call OkGo.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpUtils.checkNotNull(okHttpClient, "please call OkGo.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /**
     * 必须设置
     */
    public OkGo setOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * 获取全局的cookie实例
     */
    public OkCookieJarImpl getCookieJar() {
        return (OkCookieJarImpl) okHttpClient.cookieJar();
    }

    /**
     * 超时重试次数
     */
    public OkGo setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * 超时重试次数
     */
    public int getRetryCount() {
        return mRetryCount;
    }

    /**
     * 全局的缓存模式
     */
    public OkGo setCacheMode(@OkCacheMode int cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public int getCacheMode() {
        return mCacheMode;
    }

    /**
     * 全局的缓存过期时间
     */
    public OkGo setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = OkCacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public long getCacheTime() {
        return mCacheTime;
    }

    /**
     * 获取全局公共请求参数
     */
    public OkHttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 添加全局公共请求参数
     */
    public OkGo addCommonParams(OkHttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new OkHttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 获取全局公共请求头
     */
    public OkHttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public OkGo addCommonHeaders(OkHttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new OkHttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
