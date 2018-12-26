package com.wkz.pleasedreading.main.toutiao;

import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRTouTiaoPresenter extends FRBasePresenter<PRTouTiaoContract.ITouTiaoView, PRTouTiaoContract.ITouTiaoModel>
        implements PRTouTiaoContract.ITouTiaoPresenter {

    public PRTouTiaoPresenter(PRTouTiaoContract.ITouTiaoView view) {
        super(view);
    }

    @Override
    public void getVideoList(String category, String maxBehotTime) {
        mModel.getVideoList(category, maxBehotTime, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoBean data) {
                if (mView != null) {
                    mView.onSuccess(data);
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
