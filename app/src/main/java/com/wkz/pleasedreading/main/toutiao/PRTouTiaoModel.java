package com.wkz.pleasedreading.main.toutiao;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRObserver;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRTouTiaoModel implements PRTouTiaoContract.ITouTiaoModel {
    @Override
    public void getVideoList(String category, String maxBehotTime, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRTouTiaoVideoBean> callback) {
        PRTouTiaoApi.getInstance().getVideoList(category, maxBehotTime, lifecycleTransformer, new FRObserver<>(callback));
    }
}
