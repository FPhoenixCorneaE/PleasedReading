package com.wkz.pleasedreading.main.toutiao;

import com.google.gson.Gson;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;
import com.wkz.framework.utils.TimeUtils;

public class PRTouTiaoPresenter extends FRBasePresenter<PRTouTiaoContract.ITouTiaoView, PRTouTiaoContract.ITouTiaoModel>
        implements PRTouTiaoContract.ITouTiaoPresenter {

    private String mTime;

    public PRTouTiaoPresenter(PRTouTiaoContract.ITouTiaoView view) {
        super(view);
    }

    /**
     * 刷新数据
     *
     * @param category
     */
    public void onRefreshData(String category) {
        mTime = TimeUtils.getNowString();
        getVideoList(category, mTime);
    }

    /**
     * 加载更多数据
     *
     * @param category
     */
    public void onLoadMoreData(String category) {
        getVideoList(category, mTime);
    }

    @Override
    public void getVideoList(String category, String maxBehotTime) {
        mModel.getVideoList(category, maxBehotTime, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoBean data) {
                if (mView != null) {
                    PRTouTiaoVideoBean.DataBean.ContentBean contentBean = new Gson().fromJson(data.getData().get(data.getData().size() - 1).getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
                    mTime = contentBean.getBehot_time() + "";
                    mView.onSuccess(data.getData());
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
}
