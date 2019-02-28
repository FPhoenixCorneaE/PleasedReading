package com.wkz.okgo.callback;


import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkLogger;

/**
 * 抽象的回调接口
 */
public abstract class OkAbsCallback<T> implements IOkCallback<T> {

    @Override
    public void onStart(OkRequest<T, ? extends OkRequest> request) {
    }

    @Override
    public void onCacheSuccess(OkResponse<T> response) {
    }

    @Override
    public void onError(OkResponse<T> response) {
        OkLogger.printStackTrace(response.getException());
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void uploadProgress(OkProgress progress) {
    }

    @Override
    public void downloadProgress(OkProgress progress) {
    }
}
