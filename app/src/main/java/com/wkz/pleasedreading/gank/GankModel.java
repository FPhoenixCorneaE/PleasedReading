package com.wkz.pleasedreading.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GankModel implements GankContract.IGankModel {
    @Override
    public void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<GankBean> callback) {
        GankApi.getInstance().getDataByType(type, pageNum, pageCount, lifecycleTransformer, new Observer<GankBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GankBean gankBean) {
                if (callback != null) {
                    callback.onSuccess(gankBean);
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
        });
    }
}
