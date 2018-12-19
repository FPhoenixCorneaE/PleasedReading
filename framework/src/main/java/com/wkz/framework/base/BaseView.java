package com.wkz.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface BaseView {
    int getLayoutId();

    BasePresenter createPresenter();

    BaseModel createModel();

    void initView();

    void initListener();

    void initData(@Nullable Bundle savedInstanceState);

    void showLoading();

    void hideLoading();

    void showContent();

    void showEmpty();

    void showError();

    void onSuccess(@Nullable Object data);

    void onFailure(int code, String msg);
}
