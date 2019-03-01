package com.wkz.pleasedreading.myself.localvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivityLocalVideoBinding;

public class PRLocalVideoActivity extends FRBaseActivity {

    private PrActivityLocalVideoBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_local_video;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRLocalVideoPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return new PRLocalVideoModel();
    }

    @Override
    public void initView() {
        mDataBinding = (PrActivityLocalVideoBinding) mViewDataBinding;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}
