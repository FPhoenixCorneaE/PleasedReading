package com.wkz.framework.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wkz.framework.functions.network.FRNetworkManager;
import com.wkz.framework.functions.network.OnNetworkChangedListener;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.framework.widgets.statuslayout.FRStatusLayoutManager;
import com.wkz.framework.widgets.statuslayout.OnStatusLayoutClickListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity
        extends RxAppCompatActivity
        implements BaseView, OnStatusLayoutClickListener, OnNetworkChangedListener {

    private static final String NAME_ACTIVITY = BaseActivity.class.getName();
    protected BaseActivity mContext;
    protected ViewDataBinding mViewDataBinding;
    private Unbinder mUnbinder;
    private FRStatusLayoutManager mFRStatusLayoutManager;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(NAME_ACTIVITY);
        mContext = this;

        //设置内容视图
        View contentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        mViewDataBinding = DataBindingUtil.setContentView(mContext, getLayoutId());

        mUnbinder = ButterKnife.bind(this);

        //设置状态布局
        mFRStatusLayoutManager = new FRStatusLayoutManager.Builder(contentView)
                .setOnStatusLayoutClickListener(this)
                .build();

        //构建Presenter
        createPresenter();

        //注册网络变化监听
        FRNetworkManager.getInstance().registerNetwork(mContext, this);

        //初始化视图
        initView();
        //初始化监听
        initListener();
        //初始化数据
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        //反注册网络变化监听
        if (mContext != null) {
            FRNetworkManager.getInstance().unregisterNetwork(mContext);
        }
    }

    @Override
    public void showLoading() {
        if (mFRStatusLayoutManager != null) {
            mFRStatusLayoutManager.showLoadingLayout();
        }
    }

    @Override
    public void hideLoading() {
        if (mFRStatusLayoutManager != null) {
            mFRStatusLayoutManager.showSuccessLayout();
        }
    }

    @Override
    public void showContent() {
        if (mFRStatusLayoutManager != null) {
            mFRStatusLayoutManager.showSuccessLayout();
        }
    }

    @Override
    public void showEmpty() {
        if (mFRStatusLayoutManager != null) {
            mFRStatusLayoutManager.showEmptyLayout();
        }
    }

    @Override
    public void showError() {
        if (mFRStatusLayoutManager != null) {
            mFRStatusLayoutManager.showErrorLayout();
        }
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        showContent();
    }

    @Override
    public void onFailure(int code, String msg) {
        showError();
        ToastUtils.showShortSafe(msg);
    }

    @Override
    public void onEmptyChildClick(View view) {
        initData(null);
    }

    @Override
    public void onErrorChildClick(View view) {
        initData(null);
    }

    @Override
    public void onCustomerChildClick(View view) {

    }

    @Override
    public void onWifiActive(String message) {
        Logger.i(message);
    }

    @Override
    public void onMobileActive(String message) {
        Logger.i(message);
        ToastUtils.showShortSafe(message);
    }

    @Override
    public void onUnavailable(String message) {
        Logger.i(message);
        ToastUtils.showShortSafe(message);
    }
}
