package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PRGankModel implements PRGankContract.IGankModel {
    @Override
    public void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean> callback) {
        PRGankApi.getInstance().getDataByType(type, pageNum, pageCount, lifecycleTransformer, new Observer<PRGankBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PRGankBean gankBean) {
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
