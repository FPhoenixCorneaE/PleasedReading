package com.wkz.pleasedreading.main.gank;

import android.support.annotation.NonNull;

import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRGankPresenter extends FRBasePresenter<PRGankContract.IGankView, PRGankContract.IGankModel> implements PRGankContract.IGankPresenter {

    private int mPageNum = 10;
    private int mPageCount = 1;

    public PRGankPresenter(@NonNull PRGankContract.IGankView view) {
        super(view);
    }

    /**
     * 刷新数据
     *
     * @param type
     */
    public void onRefreshDataByType(String type) {
        mPageCount = 1;
        getDataByType(type);
    }

    /**
     * 加载更多数据
     *
     * @param type
     */
    public void onLoadMoreDataByType(String type) {
        mPageCount++;
        getDataByType(type);
    }

    @Override
    public void getDataByType(String type) {
        mModel.getDataByType(type, mPageNum, mPageCount, mView.bindToLife(), new OnFRHttpCallback<PRGankBean>() {
            @Override
            public void onSuccess(PRGankBean data) {
                if (!data.isError()) {
                    mView.onSuccess(data.getResults());
                } else {
                    mView.onFailure(FRHttpError.ERROR_UNKNOWN, FRHttpError.MESSAGE_UNKNOWN);
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
            }
        });
    }

    @Override
    public void getVideoInfo(PRGankBean.ResultsBean prGankBean) {
        mModel.getVideoInfo(prGankBean, mView.bindToLife(), new OnFRHttpCallback<PRGankBean.ResultsBean>() {
            @Override
            public void onSuccess(PRGankBean.ResultsBean data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
            }
        });
    }
}
