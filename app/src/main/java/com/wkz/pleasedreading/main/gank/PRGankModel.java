package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRObserver;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRGankModel implements PRGankContract.IGankModel {
    @Override
    public void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean> callback) {
        PRGankApi.getInstance().getDataByType(type, pageNum, pageCount, lifecycleTransformer, new FRObserver<>(callback));
    }

    @Override
    public void getVideoInfo(PRGankBean.ResultsBean prGankBean, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean.ResultsBean> callback) {
        PRGankApi.getInstance().getVideoInfo(prGankBean, lifecycleTransformer, new FRObserver<>(callback));
    }
}
