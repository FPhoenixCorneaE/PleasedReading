package com.wkz.okgo.cache.policy;

import android.graphics.Bitmap;

import com.wkz.okgo.OkGo;
import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.cache.OkCacheMode;
import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.db.dao.OkCacheDao;
import com.wkz.okgo.exception.OkHttpException;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkHeaderParser;
import com.wkz.okgo.utils.OkHttpUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;

public abstract class OkBaseCachePolicy<T> implements IOkCachePolicy<T> {

    protected OkRequest<T, ? extends OkRequest> request;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected okhttp3.Call rawCall;
    protected IOkCallback<T> mCallback;
    protected OkCacheEntity<T> cacheEntity;

    public OkBaseCachePolicy(OkRequest<T, ? extends OkRequest> request) {
        this.request = request;
    }

    @Override
    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        return false;
    }

    @Override
    public OkCacheEntity<T> prepareCache() {
        //check the config of cache
        if (request.getCacheKey() == null) {
            request.cacheKey(OkHttpUtils.createUrlFromParams(request.getBaseUrl(), request.getParams().urlParamsMap));
        }

        int cacheMode = request.getCacheMode();
        if (cacheMode != OkCacheMode.NO_CACHE) {
            //noinspection unchecked
            cacheEntity = (OkCacheEntity<T>) OkCacheDao.getInstance().get(request.getCacheKey());
            OkHeaderParser.addCacheHeaders(request, cacheEntity, cacheMode);
            if (cacheEntity != null && cacheEntity.checkExpire(cacheMode, request.getCacheTime(), System.currentTimeMillis())) {
                cacheEntity.setExpire(true);
            }
        }

        if (cacheEntity == null || cacheEntity.isExpire() || cacheEntity.getData() == null || cacheEntity.getResponseHeaders() == null) {
            cacheEntity = null;
        }
        return cacheEntity;
    }

    @Override
    public synchronized okhttp3.Call prepareRawCall() throws Throwable {
        if (executed) throw OkHttpException.COMMON("Already executed!");
        executed = true;
        rawCall = request.getRawCall();
        if (canceled) rawCall.cancel();
        return rawCall;
    }

    protected OkResponse<T> requestNetworkSync() {
        try {
            okhttp3.Response response = rawCall.execute();
            int responseCode = response.code();

            //network error
            if (responseCode == 404 || responseCode >= 500) {
                return OkResponse.error(false, rawCall, response, OkHttpException.NET_ERROR());
            }

            T body = request.getConverter().convertResponse(response);
            //save cache when request is successful
            saveCache(response.headers(), body);
            return OkResponse.success(false, body, rawCall, response);
        } catch (Throwable throwable) {
            if (throwable instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                currentRetryCount++;
                rawCall = request.getRawCall();
                if (canceled) {
                    rawCall.cancel();
                } else {
                    requestNetworkSync();
                }
            }
            return OkResponse.error(false, rawCall, null, throwable);
        }
    }

    protected void requestNetworkAsync() {
        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (e instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                    //retry when timeout
                    currentRetryCount++;
                    rawCall = request.getRawCall();
                    if (canceled) {
                        rawCall.cancel();
                    } else {
                        rawCall.enqueue(this);
                    }
                } else {
                    if (!call.isCanceled()) {
                        OkResponse<T> error = OkResponse.error(false, call, null, e);
                        onError(error);
                    }
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                int responseCode = response.code();

                //network error
                if (responseCode == 404 || responseCode >= 500) {
                    OkResponse<T> error = OkResponse.error(false, call, response, OkHttpException.NET_ERROR());
                    onError(error);
                    return;
                }

                if (onAnalysisResponse(call, response)) return;

                try {
                    T body = request.getConverter().convertResponse(response);
                    //save cache when request is successful
                    saveCache(response.headers(), body);
                    OkResponse<T> success = OkResponse.success(false, body, call, response);
                    onSuccess(success);
                } catch (Throwable throwable) {
                    OkResponse<T> error = OkResponse.error(false, call, response, throwable);
                    onError(error);
                }
            }
        });
    }

    /**
     * 请求成功后根据缓存模式，更新缓存数据
     *
     * @param headers 响应头
     * @param data    响应数据
     */
    private void saveCache(Headers headers, T data) {
        if (request.getCacheMode() == OkCacheMode.NO_CACHE) return;    //不需要缓存,直接返回
        if (data instanceof Bitmap) return;             //Bitmap没有实现Serializable,不能缓存

        OkCacheEntity<T> cache = OkHeaderParser.createCacheEntity(headers, data, request.getCacheMode(), request.getCacheKey());
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            OkCacheDao.getInstance().remove(request.getCacheKey());
        } else {
            //缓存命中，更新缓存
            OkCacheDao.getInstance().replace(request.getCacheKey(), cache);
        }
    }

    protected void runOnUiThread(Runnable run) {
        OkGo.getInstance().getDelivery().post(run);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
        canceled = true;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        if (canceled) return true;
        synchronized (this) {
            return rawCall != null && rawCall.isCanceled();
        }
    }
}
