package com.wkz.pleasedreading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wkz.framework.base.BaseActivity;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.utils.FragmentUtils;
import com.wkz.pleasedreading.gank.GankFragment;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_main;
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

        FragmentUtils.addFragment(mContext, R.id.pr_fl_container, new GankFragment(), null, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showError();
            }
        },1000);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
    }
}
