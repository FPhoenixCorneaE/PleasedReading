package com.wkz.framework.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wkz.framework.functions.network.FRNetworkManager;
import com.wkz.framework.functions.network.OnNetworkChangedListener;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.framework.widgets.ripple.FRMaterialRippleLayout;
import com.wkz.framework.widgets.statuslayout.FRStatusLayoutManager;
import com.wkz.framework.widgets.statuslayout.OnStatusLayoutClickListener;

public abstract class FRBaseActivity
        extends RxAppCompatActivity
        implements IFRBaseView, OnStatusLayoutClickListener, OnNetworkChangedListener, FRBaseFragment.OnSelectedInterface {

    private static final String NAME_ACTIVITY = FRBaseActivity.class.getName();
    protected FRBaseActivity mContext;
    protected ViewDataBinding mViewDataBinding;
    private FRStatusLayoutManager mFRStatusLayoutManager;
    private View mContentView;
    private FRBaseFragment mBaseFragment;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(NAME_ACTIVITY);
        mContext = this;

        //设置内容视图
        mContentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        mViewDataBinding = DataBindingUtil.setContentView(mContext, getLayoutId());
        //设置状态布局
        mFRStatusLayoutManager = new FRStatusLayoutManager.Builder(mContentView)
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

    /**
     * DataBinding结合BaseActivity和多状态布局使用的解决方案
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        ViewGroup frParent = new FrameLayout(mContext);
        frParent.addView(mContentView);
        setContentView(frParent);

        ViewGroup androidContentView = findViewById(android.R.id.content);
        frParent.setId(android.R.id.content);
        androidContentView.setId(View.NO_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册网络变化监听
        if (mContext != null) {
            FRNetworkManager.getInstance().unregisterNetwork(mContext);
        }
    }

    @Override
    public void onBackPressed() {
        //如果Fragment的onBackPressed()返回false，就会进入条件内进行默认的操作
        if (mBaseFragment == null || !mBaseFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            return;
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindUntilEvent(ActivityEvent.DESTROY);
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

    @Override
    public void onSelectedFragment(FRBaseFragment selectedFragment) {
        this.mBaseFragment = selectedFragment;
    }

    /**
     * 将View构建成Ripple风格
     *
     * @param targetView 需要添加Ripple的View
     * @param listener   点击事件
     */
    public FRMaterialRippleLayout materialRipple(View targetView, View.OnClickListener listener) {
        return materialRipple(targetView, false, listener);
    }

    /**
     * 将View构建成Ripple风格
     *
     * @param targetView 需要添加Ripple的View
     * @param listener   点击事件
     */
    public FRMaterialRippleLayout materialRipple(View targetView, boolean rippleInAdapter, View.OnClickListener listener) {
        FRMaterialRippleLayout layout = FRMaterialRippleLayout.with(targetView)
                .rippleOverlay(true)
                .rippleHover(true)
                .rippleAlpha(0.2f)
                .rippleInAdapter(rippleInAdapter)
                .create();
        layout.setClickable(true);
        layout.setOnClickListener(listener);
        return layout;
    }
}
