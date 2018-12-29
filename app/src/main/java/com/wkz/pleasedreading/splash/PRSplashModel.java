package com.wkz.pleasedreading.splash;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRObserver;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRSplashModel implements PRSplashContract.ISplashModel {
    @Override
    public void getDailyImage(LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRSplashBean> callback) {
        PRSplashApi.getInstance().getDailyImage(lifecycleTransformer, new FRObserver<>(callback));
    }
}
