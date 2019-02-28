package com.wkz.okgo.cache.policy;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.exception.OkCacheException;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;

import okhttp3.Call;

public class OkDefaultCachePolicy<T> extends OkBaseCachePolicy<T> {

    public OkDefaultCachePolicy(OkRequest<T, ? extends OkRequest> request) {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(error);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public boolean onAnalysisResponse(final Call call, final okhttp3.Response response) {
        if (response.code() != 304) return false;

        if (cacheEntity == null) {
            final OkResponse<T> error = OkResponse.error(true, call, response, OkCacheException.NON_AND_304(request.getCacheKey()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(error);
                    mCallback.onFinish();
                }
            });
        } else {
            final OkResponse<T> success = OkResponse.success(true, cacheEntity.getData(), call, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCacheSuccess(success);
                    mCallback.onFinish();
                }
            });
        }
        return true;
    }

    @Override
    public OkResponse<T> requestSync(OkCacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return OkResponse.error(false, rawCall, null, throwable);
        }
        OkResponse<T> response = requestNetworkSync();
        //HTTP cache protocol
        if (response.isSuccessful() && response.code() == 304) {
            if (cacheEntity == null) {
                response = OkResponse.error(true, rawCall, response.getRawResponse(), OkCacheException.NON_AND_304(request.getCacheKey()));
            } else {
                response = OkResponse.success(true, cacheEntity.getData(), rawCall, response.getRawResponse());
            }
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
