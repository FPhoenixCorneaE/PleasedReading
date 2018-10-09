package com.wkz.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wkz.framework.network.OnNetworkChangedListener;
import com.wkz.framework.network.NetworkManager;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.framework.widget.statuslayout.OnStatusLayoutClickListener;
import com.wkz.framework.widget.statuslayout.StatusLayoutManager;

public abstract class BaseActivity<P extends BasePresenter, M>
        extends RxAppCompatActivity
        implements BaseView<P, M>, OnStatusLayoutClickListener, OnNetworkChangedListener {

    private static final String NAME_ACTIVITY = BaseActivity.class.getName();
    protected BaseActivity mContext;
    private StatusLayoutManager mStatusLayoutManager;
    private P mPresenter;
    private M mData;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(NAME_ACTIVITY);
        mContext = this;

        //设置内容视图
        View contentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        setContentView(contentView);

        //设置状态布局
        mStatusLayoutManager = new StatusLayoutManager.Builder(contentView)
                .setOnStatusLayoutClickListener(this)
                .build();

        //构建Presenter
        mPresenter = createPresenter();

        //注册网络变化监听
        NetworkManager.getInstance().registerNetwork(mContext, this);

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
        //反注册网络变化监听
        NetworkManager.getInstance().unregisterNetwork(mContext);
    }

    @Override
    public void showLoading() {
        if (mStatusLayoutManager != null) {
            mStatusLayoutManager.showLoadingLayout();
        }
    }

    @Override
    public void hideLoading() {
        if (mStatusLayoutManager != null) {
            mStatusLayoutManager.showSuccessLayout();
        }
    }

    @Override
    public void showContent() {
        if (mStatusLayoutManager != null) {
            mStatusLayoutManager.showSuccessLayout();
        }
    }

    @Override
    public void showEmpty() {
        if (mStatusLayoutManager != null) {
            mStatusLayoutManager.showEmptyLayout();
        }
    }

    @Override
    public void showError() {
        if (mStatusLayoutManager != null) {
            mStatusLayoutManager.showErrorLayout();
        }
    }

    @Override
    public void onSuccess(@Nullable M data) {
        mData = data;
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
