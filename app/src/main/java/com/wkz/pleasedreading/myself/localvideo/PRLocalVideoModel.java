package com.wkz.pleasedreading.myself.localvideo;

import androidx.fragment.app.FragmentActivity;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.framework.functions.retrofit.FRObserver;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

import java.util.List;

public class PRLocalVideoModel implements PRLocalVideoContract.ILocalVideoModel {
    @Override
    public void getVideos(FragmentActivity activity, String dir, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<List<FRVideoBean>> callback) {
        PRLocalVideoApi.getInstance().getVideos(activity, dir, lifecycleTransformer, new FRObserver<>(callback));
    }
}
