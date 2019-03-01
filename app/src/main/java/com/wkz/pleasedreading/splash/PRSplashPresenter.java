package com.wkz.pleasedreading.splash;

import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;
import com.wkz.pleasedreading.constants.PRUrl;

public class PRSplashPresenter extends FRBasePresenter<PRSplashContract.ISplashView, PRSplashContract.ISplashModel> implements PRSplashContract.ISplashPresenter {

    public PRSplashPresenter(PRSplashContract.ISplashView view) {
        super(view);
    }

    @Override
    public void getDailyImage() {
        mModel.getDailyImage(mView.bindToLife(), new OnFRHttpCallback<PRSplashBean>() {
            @Override
            public void onSuccess(PRSplashBean data) {
                if (mView != null) {
                    mView.onSuccess(PRUrl.BaseUrl_BiYing_Img + data.getImages().get(0).getUrlbase() + "_1080x1920.jpg");
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
