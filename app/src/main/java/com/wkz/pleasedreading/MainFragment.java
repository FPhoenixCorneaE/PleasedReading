package com.wkz.pleasedreading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;

public class MainFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_main;
    }

    @NonNull
    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseModel createModel() {
        return null;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showEmpty();
            }
        },4000);
    }
}
