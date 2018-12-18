package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface PRGankContract {

    interface IGankView extends BaseView {

    }

    interface IGankPresenter {
        void onRefreshDataByType(String type);

        void onLoadMoreDataByType(String type);

        void getDataByType(String type);

        void getVideoInfo(PRGankBean.ResultsBean prGankBean);
    }

    interface IGankModel extends BaseModel {
        void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean> callback);

        void getVideoInfo(PRGankBean.ResultsBean prGankBean, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean.ResultsBean> callback);
    }
}
