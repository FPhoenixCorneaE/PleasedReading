package com.wkz.pleasedreading.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.retrofit.FRRetrofitFactory;
import com.wkz.pleasedreading.constant.PRUrl;

import io.reactivex.Observer;

public class GankApi extends FRRetrofitFactory {

    private static volatile GankApi mInstance;

    private GankApi() {
    }

    public static GankApi getInstance() {
        if (mInstance == null) {
            synchronized (GankApi.class) {
                if (mInstance == null) {
                    mInstance = new GankApi();
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
     * {@link Gank#getDataByType(String, int, int)}
     */
    public void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, Observer<GankBean> observer) {
        setObserver(createRetrofit(Gank.class).getDataByType(type, pageNum, pageCount), lifecycleTransformer, observer);
    }
}
