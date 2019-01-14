package com.wkz.pleasedreading.main.toutiao;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.base.IFRBasePresenter;
import com.wkz.framework.base.IFRBaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface PRTouTiaoContract {
    interface ITouTiaoView extends IFRBaseView {
        //获取视频内容成功
        void onGetVideoContentSuccess(int position, String videoUrl);
        //获取视频内容失败
        void onGetVideoContentFailure(String errorMsg);
    }

    interface ITouTiaoPresenter extends IFRBasePresenter {
        //设置类别id
        void setCategoryId(String categoryId);
        //刷新数据
        void onRefreshData(String category);
        //加载更多数据
        void onLoadMoreData(String category);
        //获取视频列表
        void getVideoList(String category, String maxBehotTime);
        //获取视频内容
        void getVideoContent(int position, String url);
        //清空数据
        void clear();
    }

    interface ITouTiaoModel extends IFRBaseModel {
        //获取视频列表
        void getVideoList(String category, String maxBehotTime, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRTouTiaoVideoBean> callback);
        //获取视频内容
        void getVideoContent(String url, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRTouTiaoVideoContentBean> callback);
    }
}
