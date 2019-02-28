package com.wkz.okgo.cache.policy;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkResponse;

public interface IOkCachePolicy<T> {

    /**
     * 获取数据成功的回调
     *
     * @param success 获取的数据，可是是缓存或者网络
     */
    void onSuccess(OkResponse<T> success);

    /**
     * 获取数据失败的回调
     *
     * @param error 失败的信息，可是是缓存或者网络
     */
    void onError(OkResponse<T> error);

    /**
     * 控制是否执行后续的回调动作
     *
     * @param call     请求的对象
     * @param response 响应的对象
     * @return true，不执行回调， false 执行回调
     */
    boolean onAnalysisResponse(okhttp3.Call call, okhttp3.Response response);

    /**
     * 构建缓存
     *
     * @return 获取的缓存
     */
    OkCacheEntity<T> prepareCache();

    /**
     * 构建请求对象
     *
     * @return 准备请求的对象
     */
    okhttp3.Call prepareRawCall() throws Throwable;

    /**
     * 同步请求获取数据
     *
     * @param cacheEntity 本地的缓存
     * @return 从缓存或本地获取的数据
     */
    OkResponse<T> requestSync(OkCacheEntity<T> cacheEntity);

    /**
     * 异步请求获取数据
     *
     * @param cacheEntity 本地的缓存
     * @param callback    异步回调
     */
    void requestAsync(OkCacheEntity<T> cacheEntity, IOkCallback<T> callback);

    /**
     * 当前请求是否已经执行
     *
     * @return true，已经执行， false，没有执行
     */
    boolean isExecuted();

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 是否已经取消
     *
     * @return true，已经取消，false，没有取消
     */
    boolean isCanceled();
}
