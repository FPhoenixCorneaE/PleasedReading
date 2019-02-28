package com.wkz.okgo.adapter;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.cache.OkCacheMode;
import com.wkz.okgo.cache.policy.IOkCachePolicy;
import com.wkz.okgo.cache.policy.OkDefaultCachePolicy;
import com.wkz.okgo.cache.policy.OkFirstCacheRequestPolicy;
import com.wkz.okgo.cache.policy.OkNoCachePolicy;
import com.wkz.okgo.cache.policy.OkNoneCacheRequestPolicy;
import com.wkz.okgo.cache.policy.OkRequestFailedCachePolicy;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkHttpUtils;

/**
 * 带缓存的请求
 */
public class OkCacheCall<T> implements IOkCall<T> {

    private IOkCachePolicy<T> policy;
    private OkRequest<T, ? extends OkRequest> request;

    public OkCacheCall(OkRequest<T, ? extends OkRequest> request) {
        this.request = request;
        this.policy = preparePolicy();
    }

    @Override
    public OkResponse<T> execute() {
        OkCacheEntity<T> cacheEntity = policy.prepareCache();
        return policy.requestSync(cacheEntity);
    }

    @Override
    public void execute(IOkCallback<T> callback) {
        OkHttpUtils.checkNotNull(callback, "callback == null");

        OkCacheEntity<T> cacheEntity = policy.prepareCache();
        policy.requestAsync(cacheEntity, callback);
    }

    private IOkCachePolicy<T> preparePolicy() {
        switch (request.getCacheMode()) {
            case OkCacheMode.DEFAULT:
                policy = new OkDefaultCachePolicy<>(request);
                break;
            case OkCacheMode.NO_CACHE:
                policy = new OkNoCachePolicy<>(request);
                break;
            case OkCacheMode.IF_NONE_CACHE_REQUEST:
                policy = new OkNoneCacheRequestPolicy<>(request);
                break;
            case OkCacheMode.FIRST_CACHE_THEN_REQUEST:
                policy = new OkFirstCacheRequestPolicy<>(request);
                break;
            case OkCacheMode.REQUEST_FAILED_READ_CACHE:
                policy = new OkRequestFailedCachePolicy<>(request);
                break;
        }
        if (request.getCachePolicy() != null) {
            policy = request.getCachePolicy();
        }
        OkHttpUtils.checkNotNull(policy, "policy == null");
        return policy;
    }

    @Override
    public boolean isExecuted() {
        return policy.isExecuted();
    }

    @Override
    public void cancel() {
        policy.cancel();
    }

    @Override
    public boolean isCanceled() {
        return policy.isCanceled();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public IOkCall<T> clone() {
        return new OkCacheCall<>(request);
    }

    public OkRequest getRequest() {
        return request;
    }
}
