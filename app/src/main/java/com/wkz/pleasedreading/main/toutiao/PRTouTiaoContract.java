package com.wkz.pleasedreading.main.toutiao;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.base.IFRBasePresenter;
import com.wkz.framework.base.IFRBaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface PRTouTiaoContract {
    interface ITouTiaoView extends IFRBaseView {

    }

    interface ITouTiaoPresenter extends IFRBasePresenter {
        void onRefreshData(String category);

        void onLoadMoreData(String category);

        void getVideoList(String category, String maxBehotTime);
    }

    interface ITouTiaoModel extends IFRBaseModel {
        void getVideoList(String category, String maxBehotTime, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRTouTiaoVideoBean> callback);
    }
}
