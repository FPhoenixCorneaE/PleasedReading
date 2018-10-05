package com.wkz.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wkz.framework.listener.OnNetworkChangedListener;
import com.wkz.framework.manager.NetworkManager;
import com.wkz.framework.widget.statuslayout.OnStatusLayoutClickListener;
import com.wkz.framework.widget.statuslayout.StatusLayoutManager;

public abstract class BaseActivity<P extends BasePresenter, M> extends RxAppCompatActivity implements BaseView<P, M>, OnStatusLayoutClickListener, OnNetworkChangedListener {

    private BaseActivity mContext;
    private StatusLayoutManager mStatusLayoutManager;
    private P mPresenter;
    private M mData;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        View contentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);

        //设置内容视图
        setContentView(contentView);

        //设置状态布局
        mStatusLayoutManager = new StatusLayoutManager.Builder(contentView)
                .setOnStatusLayoutClickListener(this)
                .build();

        //构建Presenter
        mPresenter = createPresenter();

        NetworkManager.getInstance().registerNetwork(mContext, this);


        //初始化视图
        initView();
        //初始化监听
        initListener();
        //初始化数据
        initData(savedInstanceState);

        showLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().unregisterNetwork(mContext);
    }

    @Override
    public void showLoading() {
        mStatusLayoutManager.showLoadingLayout();
    }

    @Override
    public void hideLoading() {
        mStatusLayoutManager.showSuccessLayout();
    }

    @Override
    public void showContent() {
        mStatusLayoutManager.showSuccessLayout();
    }

    @Override
    public void showEmpty() {
        mStatusLayoutManager.showEmptyLayout();
    }

    @Override
    public void showError() {
        mStatusLayoutManager.showErrorLayout();
    }

    @Override
    public void onSuccess(@Nullable M data) {
        mData = data;
        hideLoading();
    }

    @Override
    public void onFailure(int code, String msg) {
        showError();
    }

    @Override
    public void onEmptyChildClick(View view) {

    }

    @Override
    public void onErrorChildClick(View view) {

    }

    @Override
    public void onCustomerChildClick(View view) {

    }

    @Override
    public void onWifiActive(String message) {

    }

    @Override
    public void onMobileActive(String message) {

    }

    @Override
    public void onUnavailable(String message) {

    }
}
