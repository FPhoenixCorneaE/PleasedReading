package com.wkz.pleasedreading.myself.localvideo;

import android.support.v4.app.FragmentActivity;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.bases.IFRBasePresenter;
import com.wkz.framework.bases.IFRBaseView;
import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

import java.util.List;

public interface PRLocalVideoContract {

    interface ILocalVideoView extends IFRBaseView {

    }

    interface ILocalVideoPresenter extends IFRBasePresenter {
        void getVideos(FragmentActivity activity, String dir);
    }

    interface ILocalVideoModel extends IFRBaseModel {
        void getVideos(FragmentActivity activity, String dir, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<List<FRVideoBean>> callback);
    }
}
