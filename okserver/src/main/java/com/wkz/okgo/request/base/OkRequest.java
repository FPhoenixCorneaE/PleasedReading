package com.wkz.okgo.request.base;

import android.text.TextUtils;

import com.wkz.okgo.OkGo;
import com.wkz.okgo.adapter.OkAdapterParams;
import com.wkz.okgo.adapter.OkCacheCall;
import com.wkz.okgo.adapter.IOkCall;
import com.wkz.okgo.adapter.IOkCallAdapter;
import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.cache.OkCacheMode;
import com.wkz.okgo.cache.policy.IOkCachePolicy;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.converter.IOkConverter;
import com.wkz.okgo.model.OkHttpHeaders;
import com.wkz.okgo.model.OkHttpParams;
import com.wkz.okgo.utils.OkHttpUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 所有请求的基类，其中泛型 R 主要用于属性设置方法后，返回对应的子类型，以便于实现链式调用
 */
public abstract class OkRequest<T, R extends OkRequest> implements Serializable {

    private static final long serialVersionUID = -5573515313738638501L;

    protected String url;
    protected String baseUrl;
    protected transient OkHttpClient client;
    protected transient Object tag;
    protected int retryCount;
    protected int cacheMode;
    protected String cacheKey;
    protected long cacheTime;                           //默认缓存的超时时间
    protected OkHttpParams params = new OkHttpParams();     //添加的param
    protected OkHttpHeaders headers = new OkHttpHeaders();  //添加的header

    protected transient okhttp3.Request mRequest;
    protected transient IOkCall<T> call;
    protected transient IOkCallback<T> callback;
    protected transient IOkConverter<T> converter;
    protected transient IOkCachePolicy<T> cachePolicy;
    protected transient OkProgressRequestBody.UploadInterceptor uploadInterceptor;

    public OkRequest(String url) {
        this.url = url;
        baseUrl = url;
        OkGo go = OkGo.getInstance();
        //默认添加 Accept-Language
        String acceptLanguage = OkHttpHeaders.getAcceptLanguage();
        if (!TextUtils.isEmpty(acceptLanguage))
            headers(OkHttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage);
        //默认添加 User-Agent
        String userAgent = OkHttpHeaders.getUserAgent();
        if (!TextUtils.isEmpty(userAgent)) headers(OkHttpHeaders.HEAD_KEY_USER_AGENT, userAgent);
        //添加公共请求参数
        if (go.getCommonParams() != null) params(go.getCommonParams());
        if (go.getCommonHeaders() != null) headers(go.getCommonHeaders());
        //添加缓存模式
        retryCount = go.getRetryCount();
        cacheMode = go.getCacheMode();
        cacheTime = go.getCacheTime();
    }

    @SuppressWarnings("unchecked")
    public R tag(Object tag) {
        this.tag = tag;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R retryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        this.retryCount = retryCount;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R client(OkHttpClient client) {
        OkHttpUtils.checkNotNull(client, "OkHttpClient == null");

        this.client = client;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R call(IOkCall<T> call) {
        OkHttpUtils.checkNotNull(call, "call == null");

        this.call = call;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R converter(IOkConverter<T> converter) {
        OkHttpUtils.checkNotNull(converter, "converter == null");

        this.converter = converter;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R cacheMode(@OkCacheMode int cacheMode) {
        this.cacheMode = cacheMode;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R cachePolicy(IOkCachePolicy<T> cachePolicy) {
        OkHttpUtils.checkNotNull(cachePolicy, "cachePolicy == null");

        this.cachePolicy = cachePolicy;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R cacheKey(String cacheKey) {
        OkHttpUtils.checkNotNull(cacheKey, "cacheKey == null");

        this.cacheKey = cacheKey;
        return (R) this;
    }

    /**
     * 传入 -1 表示永久有效,默认值即为 -1
     */
    @SuppressWarnings("unchecked")
    public R cacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = OkCacheEntity.CACHE_NEVER_EXPIRE;
        this.cacheTime = cacheTime;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R headers(OkHttpHeaders headers) {
        this.headers.put(headers);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R headers(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R removeHeader(String key) {
        headers.remove(key);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R removeAllHeaders() {
        headers.clear();
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(OkHttpParams params) {
        this.params.put(params);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(Map<String, String> params, boolean... isReplace) {
        this.params.put(params, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, String value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, int value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, float value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, double value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, long value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, char value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R params(String key, boolean value, boolean... isReplace) {
        params.put(key, value, isReplace);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R addUrlParams(String key, List<String> values) {
        params.putUrlParams(key, values);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R removeParam(String key) {
        params.remove(key);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R removeAllParams() {
        params.clear();
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R uploadInterceptor(OkProgressRequestBody.UploadInterceptor uploadInterceptor) {
        this.uploadInterceptor = uploadInterceptor;
        return (R) this;
    }

    /**
     * 默认返回第一个参数
     */
    public String getUrlParam(String key) {
        List<String> values = params.urlParamsMap.get(key);
        if (values != null && values.size() > 0) return values.get(0);
        return null;
    }

    /**
     * 默认返回第一个参数
     */
    public OkHttpParams.FileWrapper getFileParam(String key) {
        List<OkHttpParams.FileWrapper> values = params.fileParamsMap.get(key);
        if (values != null && values.size() > 0) return values.get(0);
        return null;
    }

    public OkHttpParams getParams() {
        return params;
    }

    public OkHttpHeaders getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Object getTag() {
        return tag;
    }

    public int getCacheMode() {
        return cacheMode;
    }

    public IOkCachePolicy<T> getCachePolicy() {
        return cachePolicy;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public okhttp3.Request getRequest() {
        return mRequest;
    }

    public void setCallback(IOkCallback<T> callback) {
        this.callback = callback;
    }

    public IOkConverter<T> getConverter() {
        // converter 优先级高于 callback
        if (converter == null) converter = callback;
        OkHttpUtils.checkNotNull(converter, "converter == null, do you forget to call OkRequest#converter(IOkConverter<T>) ?");
        return converter;
    }

    public abstract String getMethod();

    public abstract boolean hasBody();

    /**
     * 根据不同的请求方式和参数，生成不同的RequestBody
     */
    protected abstract RequestBody generateRequestBody();

    /**
     * 根据不同的请求方式，将RequestBody转换成Request对象
     */
    public abstract okhttp3.Request generateRequest(RequestBody requestBody);

    /**
     * 获取okhttp的同步call对象
     */
    public okhttp3.Call getRawCall() {
        //构建请求体，返回call对象
        RequestBody requestBody = generateRequestBody();
        if (requestBody != null) {
            OkProgressRequestBody<T> progressRequestBody = new OkProgressRequestBody<>(requestBody, callback);
            progressRequestBody.setInterceptor(uploadInterceptor);
            mRequest = generateRequest(progressRequestBody);
        } else {
            mRequest = generateRequest(null);
        }
        if (client == null) client = OkGo.getInstance().getOkHttpClient();
        return client.newCall(mRequest);
    }

    /**
     * Rx支持，获取同步call对象
     */
    public IOkCall<T> adapt() {
        if (call == null) {
            return new OkCacheCall<>(this);
        } else {
            return call;
        }
    }

    /**
     * Rx支持,获取同步call对象
     */
    public <E> E adapt(IOkCallAdapter<T, E> adapter) {
        IOkCall<T> innerCall = call;
        if (innerCall == null) {
            innerCall = new OkCacheCall<>(this);
        }
        return adapter.adapt(innerCall, null);
    }

    /**
     * Rx支持,获取同步call对象
     */
    public <E> E adapt(OkAdapterParams param, IOkCallAdapter<T, E> adapter) {
        IOkCall<T> innerCall = call;
        if (innerCall == null) {
            innerCall = new OkCacheCall<>(this);
        }
        return adapter.adapt(innerCall, param);
    }

    /**
     * 普通调用，阻塞方法，同步请求执行
     */
    public Response execute() throws IOException {
        return getRawCall().execute();
    }

    /**
     * 非阻塞方法，异步请求，但是回调在子线程中执行
     */
    public void execute(IOkCallback<T> callback) {
        OkHttpUtils.checkNotNull(callback, "callback == null");

        this.callback = callback;
        IOkCall<T> call = adapt();
        call.execute(callback);
    }
}
