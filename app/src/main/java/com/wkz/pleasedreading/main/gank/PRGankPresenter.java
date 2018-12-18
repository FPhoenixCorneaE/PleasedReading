package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;
import com.wkz.videoplayer.inter.IFRInterVideoPlayer;

public class PRGankPresenter extends BasePresenter implements PRGankContract.IGankPresenter {

    private PRGankContract.IGankView mView;
    private PRGankContract.IGankModel mModel;

    private int mPageNum = 10;
    private int mPageCount = 1;

    public PRGankPresenter(BaseView view, LifecycleProvider provider) {
        super(view, provider);
        mView = (PRGankContract.IGankView) getView();
        mModel = (PRGankContract.IGankModel) getModel();
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
        mModel.getDataByType(type, mPageNum, mPageCount, bindUntilFragmentEvent(), new OnFRHttpCallback<PRGankBean>() {
            @Override
            public void onSuccess(PRGankBean data) {
                if (mView != null) {
                    if (!data.isError()) {
                        mView.onSuccess(data.getResults());
                        if ("休息视频".equals(type)) {
                            for (int i = 0; i < data.getResults().size(); i++) {
                                getVideoInfo(data.getResults().get(i));
                            }
                        }
                    } else {
                        mView.onFailure(FRHttpError.ERROR_UNKNOWN, FRHttpError.MESSAGE_UNKNOWN);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (mView != null) {
                    mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
                }
            }
        });
    }

    @Override
    public void getVideoInfo(PRGankBean.ResultsBean prGankBean) {
        mModel.getVideoInfo(prGankBean, bindUntilFragmentEvent(), new OnFRHttpCallback<PRGankBean.ResultsBean>() {
            @Override
            public void onSuccess(PRGankBean.ResultsBean data) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
