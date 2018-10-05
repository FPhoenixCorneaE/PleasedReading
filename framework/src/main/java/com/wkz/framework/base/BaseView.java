package com.wkz.framework.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface BaseView<P extends BasePresenter, M> {
    int getLayoutId();

    @NonNull
    P createPresenter();

    void initView();

    void initListener();

    void initData(@Nullable Bundle savedInstanceState);

    void showLoading();

    void hideLoading();

    void showContent();

    void showEmpty();

    void showError();

    void onSuccess(@Nullable M data);

    void onFailure(int code, String msg);
}
