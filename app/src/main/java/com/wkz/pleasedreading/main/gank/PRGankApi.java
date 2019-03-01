package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRRetrofitFactory;
import com.wkz.pleasedreading.constants.PRUrl;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class PRGankApi extends FRRetrofitFactory {

    private static volatile PRGankApi mInstance;

    private PRGankApi() {
    }

    public static PRGankApi getInstance() {
        if (mInstance == null) {
            synchronized (PRGankApi.class) {
                if (mInstance == null) {
                    mInstance = new PRGankApi();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBaseUrl() {
        return PRUrl.BaseUrl_Gank;
    }

    /**
     * 根据数据类型获取干货数据
     * {@link PRGank#getDataByType(String, int, int)}
     */
    public void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, Observer<PRGankBean> observer) {
        setObserver(createRetrofit(PRGank.class).getDataByType(type, pageNum, pageCount), lifecycleTransformer, observer);
    }

    /**
     * 获取干货视频信息
     * {@link PRGankVideoSubscriber(PRGankBean.ResultsBean)}
     */
    public void getVideoInfo(PRGankBean.ResultsBean prGankBean, LifecycleTransformer lifecycleTransformer, Observer<PRGankBean.ResultsBean> observer) {
        setObserver(Observable.create(new PRGankVideoSubscriber<>(prGankBean)), lifecycleTransformer, observer);
    }
}
