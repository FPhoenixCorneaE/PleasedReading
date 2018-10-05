package com.wkz.pleasedreading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.wkz.framework.base.BaseActivity;
import com.wkz.framework.base.BasePresenter;

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
    public void initView() {
        showError();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onSuccess(@Nullable Object data) {

    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onEmptyChildClick(View view) {
        showContent();
    }

    @Override
    public void onErrorChildClick(View view) {

    }
}
