package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.bases.IFRBasePresenter;
import com.wkz.framework.bases.IFRBaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface PRGankContract {

    interface IGankView extends IFRBaseView {

    }

    interface IGankPresenter extends IFRBasePresenter {
        //根据类型刷新数据
        void onRefreshDataByType(String type);
        //根据类型加载更多数据
        void onLoadMoreDataByType(String type);
        //根据数据类型获取干货数据
        void getDataByType(String type);
        //获取干货视频信息
        void getVideoInfo(PRGankBean.ResultsBean prGankBean);
    }

    interface IGankModel extends IFRBaseModel {
        //根据数据类型获取干货数据
        void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean> callback);
        //获取干货视频信息
        void getVideoInfo(PRGankBean.ResultsBean prGankBean, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRGankBean.ResultsBean> callback);
    }
}
