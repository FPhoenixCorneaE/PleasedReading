package com.wkz.pleasedreading.splash;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRRetrofitFactory;
import com.wkz.pleasedreading.constant.PRUrl;

import io.reactivex.Observer;

public class PRSplashApi extends FRRetrofitFactory {

    private static volatile PRSplashApi mInstance;

    private PRSplashApi() {
    }

    public static PRSplashApi getInstance() {
        if (mInstance == null) {
            synchronized (PRSplashApi.class) {
                if (mInstance == null) {
                    mInstance = new PRSplashApi();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBaseUrl() {
        return PRUrl.BaseUrl_BiYing;
    }

    /**
     * 获取每日一图
     * {@link PRSplash#getDailyImage()}
     */
    public void getDailyImage(LifecycleTransformer lifecycleTransformer, Observer<PRSplashBean> observer) {
        setObserver(createRetrofit(PRSplash.class).getDailyImage(), lifecycleTransformer, observer);
    }
}
