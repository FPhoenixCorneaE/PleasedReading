package com.wkz.pleasedreading.splash;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.bases.IFRBasePresenter;
import com.wkz.framework.bases.IFRBaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface PRSplashContract {

    interface ISplashView extends IFRBaseView {

    }

    interface ISplashPresenter extends IFRBasePresenter {
        void getDailyImage();
    }

    interface ISplashModel extends IFRBaseModel {
        void getDailyImage(LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<PRSplashBean> callback);
    }
}
