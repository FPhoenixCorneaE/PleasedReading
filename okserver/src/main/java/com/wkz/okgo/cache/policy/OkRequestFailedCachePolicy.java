package com.wkz.okgo.cache.policy;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;

public class OkRequestFailedCachePolicy<T> extends OkBaseCachePolicy<T> {

    public OkRequestFailedCachePolicy(OkRequest<T, ? extends OkRequest> request) {
        super(request);
    }

    @Override
    public void onSuccess(final OkResponse<T> success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(success);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public void onError(final OkResponse<T> error) {

        if (cacheEntity != null) {
            final OkResponse<T> cacheSuccess = OkResponse.success(true, cacheEntity.getData(), error.getRawCall(), error.getRawResponse());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCacheSuccess(cacheSuccess);
                    mCallback.onFinish();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(error);
                    mCallback.onFinish();
                }
            });
        }
    }

    @Override
    public OkResponse<T> requestSync(OkCacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return OkResponse.error(false, rawCall, null, throwable);
        }
        OkResponse<T> response = requestNetworkSync();
        if (!response.isSuccessful() && cacheEntity != null) {
            response = OkResponse.success(true, cacheEntity.getData(), rawCall, response.getRawResponse());
        }
        return response;
    }

    @Override
    public void requestAsync(OkCacheEntity<T> cacheEntity, IOkCallback<T> callback) {
        mCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart(request);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    OkResponse<T> error = OkResponse.error(false, rawCall, null, throwable);
                    mCallback.onError(error);
                    return;
                }
                requestNetworkAsync();
            }
        });
    }
}
