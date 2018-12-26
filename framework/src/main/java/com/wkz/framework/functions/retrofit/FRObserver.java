package com.wkz.framework.functions.retrofit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FRObserver<T> implements Observer<T> {

    private OnFRHttpCallback<T> callback;

    public FRObserver(OnFRHttpCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (callback != null) {
            callback.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callback != null) {
            callback.onFailure(e.getLocalizedMessage());
        }
    }

    @Override
    public void onComplete() {

    }
}
