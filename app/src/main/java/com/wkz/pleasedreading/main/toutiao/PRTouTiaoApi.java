package com.wkz.pleasedreading.main.toutiao;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRRetrofitFactory;
import com.wkz.pleasedreading.constants.PRUrl;

import io.reactivex.Observer;

public class PRTouTiaoApi extends FRRetrofitFactory {

    private static volatile PRTouTiaoApi mInstance;

    private PRTouTiaoApi() {
    }

    public static PRTouTiaoApi getInstance() {
        if (mInstance == null) {
            synchronized (PRTouTiaoApi.class) {
                if (mInstance == null) {
                    mInstance = new PRTouTiaoApi();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBaseUrl() {
        return PRUrl.BaseUrl_TouTiao;
    }

    /**
     * 获取视频列表
     * {@link PRTouTiao#getVideoList(String, String)}
     */
    public void getVideoList(String category, String maxBehotTime, LifecycleTransformer lifecycleTransformer, Observer<PRTouTiaoVideoBean> observer) {
        setObserver(createRetrofit(PRTouTiao.class).getVideoList(category, maxBehotTime), lifecycleTransformer, observer);
    }

    /**
     * 获取视频内容
     * {@link PRTouTiao#getVideoContent(String)}
     */
    public void getVideoContent(String url, LifecycleTransformer lifecycleTransformer, Observer<PRTouTiaoVideoContentBean> observer) {
        setObserver(createRetrofit(PRTouTiao.class).getVideoContent(url), lifecycleTransformer, observer);
    }
}
