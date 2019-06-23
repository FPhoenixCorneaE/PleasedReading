package com.wkz.pleasedreading.myself.localvideo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

import java.util.List;

public class PRLocalVideoPresenter extends FRBasePresenter<PRLocalVideoContract.ILocalVideoView, PRLocalVideoContract.ILocalVideoModel> implements PRLocalVideoContract.ILocalVideoPresenter {


    public PRLocalVideoPresenter(@NonNull PRLocalVideoContract.ILocalVideoView view) {
        super(view);
    }

    @Override
    public void getVideos(FragmentActivity activity, String dir) {
        mModel.getVideos(activity, dir, mView.bindToLife(), new OnFRHttpCallback<List<FRVideoBean>>() {
            @Override
            public void onSuccess(List<FRVideoBean> data) {
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
