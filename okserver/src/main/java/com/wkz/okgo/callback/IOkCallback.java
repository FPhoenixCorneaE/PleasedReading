package com.wkz.okgo.callback;


import com.wkz.okgo.cache.OkCacheMode;
import com.wkz.okgo.converter.IOkConverter;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;

/**
 * 抽象的回调接口
 * <p>该类的回调具有如下顺序,虽然顺序写的很复杂,但是理解后,是很简单,并且合情合理的
 * <p>1.无缓存模式{@link OkCacheMode#NO_CACHE}<br>
 * ---网络请求成功 onStart -> convertResponse -> onSuccess -> onFinish<br>
 * ---网络请求失败 onStart -> onError -> onFinish<br>
 * <p>2.默认缓存模式,遵循304头{@link OkCacheMode#DEFAULT}<br>
 * ---网络请求成功,服务端返回非304 onStart -> convertResponse -> onSuccess -> onFinish<br>
 * ---网络请求成功服务端返回304 onStart -> onCacheSuccess -> onFinish<br>
 * ---网络请求失败 onStart -> onError -> onFinish<br>
 * <p>3.请求网络失败后读取缓存{@link OkCacheMode#REQUEST_FAILED_READ_CACHE}<br>
 * ---网络请求成功,不读取缓存 onStart -> convertResponse -> onSuccess -> onFinish<br>
 * ---网络请求失败,读取缓存成功 onStart -> onCacheSuccess -> onFinish<br>
 * ---网络请求失败,读取缓存失败 onStart -> onError -> onFinish<br>
 * <p>4.如果缓存不存在才请求网络，否则使用缓存{@link OkCacheMode#IF_NONE_CACHE_REQUEST}<br>
 * ---已经有缓存,不请求网络 onStart -> onCacheSuccess -> onFinish<br>
 * ---没有缓存请求网络成功 onStart -> convertResponse -> onSuccess -> onFinish<br>
 * ---没有缓存请求网络失败 onStart -> onError -> onFinish<br>
 * <p>5.先使用缓存，不管是否存在，仍然请求网络{@link OkCacheMode#FIRST_CACHE_THEN_REQUEST}<br>
 * ---无缓存时,网络请求成功 onStart -> convertResponse -> onSuccess -> onFinish<br>
 * ---无缓存时,网络请求失败 onStart -> onError -> onFinish<br>
 * ---有缓存时,网络请求成功 onStart -> onCacheSuccess -> convertResponse -> onSuccess -> onFinish<br>
 * ---有缓存时,网络请求失败 onStart -> onCacheSuccess -> onError -> onFinish<br>
 */
public interface IOkCallback<T> extends IOkConverter<T> {

    /**
     * 请求网络开始前，UI线程
     */
    void onStart(OkRequest<T, ? extends OkRequest> request);

    /**
     * 对返回数据进行操作的回调， UI线程
     */
    void onSuccess(OkResponse<T> response);

    /**
     * 缓存成功的回调,UI线程
     */
    void onCacheSuccess(OkResponse<T> response);

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    void onError(OkResponse<T> response);

    /**
     * 请求网络结束后，UI线程
     */
    void onFinish();

    /**
     * 上传过程中的进度回调，get请求不回调，UI线程
     */
    void uploadProgress(OkProgress progress);

    /**
     * 下载过程中的进度回调，UI线程
     */
    void downloadProgress(OkProgress progress);
}
