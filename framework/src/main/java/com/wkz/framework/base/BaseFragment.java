package com.wkz.framework.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.wkz.framework.functions.network.FRNetworkManager;
import com.wkz.framework.functions.network.OnNetworkChangedListener;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.framework.widgets.ripple.FRMaterialRippleLayout;
import com.wkz.framework.widgets.statuslayout.FRStatusLayoutManager;
import com.wkz.framework.widgets.statuslayout.OnStatusLayoutClickListener;

public abstract class BaseFragment
        extends RxFragment
        implements BaseView, OnStatusLayoutClickListener, OnNetworkChangedListener {

    private static final String NAME_FRAGMENT = BaseFragment.class.getName();
    protected BaseActivity mContext;
    protected ViewDataBinding mViewDataBinding;
    private View mContentView;
    private FRStatusLayoutManager mFRStatusLayoutManager;
    /**
     * 标识fragment视图已经初始化完毕
     */
    private boolean mIsViewPrepared;
    /**
     * 标识已经触发过懒加载数据
     */
    private boolean mHasFetchData;
    private OnSelectedInterface mOnSelectedInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(NAME_FRAGMENT);
        if (getActivity() instanceof OnSelectedInterface) {
            this.mOnSelectedInterface = (OnSelectedInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mOnSelectedInterface != null) {
            mOnSelectedInterface.onSelectedFragment(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mContext = (BaseActivity) context;
        } else {
            mContext = (BaseActivity) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置内容视图
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mContentView = mViewDataBinding.getRoot();
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
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    /**
     * 用户可见fragment && 没有加载过数据 && 视图已经准备完毕
     */
    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !mHasFetchData && mIsViewPrepared) {
            mHasFetchData = true;
            lazyFetchData();
        }
    }

    /**
     * 懒加载的方式获取数据，仅在满足fragment可见和视图已经准备好的时候调用一次
     */
    public void lazyFetchData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // view被销毁后，将可以重新触发数据懒加载，因为在viewpager下，fragment不会再次新建并走onCreate的生命周期流程，将从onCreateView开始
        mHasFetchData = false;
        mIsViewPrepared = false;
        if (mContentView != null) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
    }

    /**
     * 所有继承BaseFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     * 返回true消费该事件,返回false不消费
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY);
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
        Logger.e("code-->" + code + "; errorMsg-->" + msg);
        showError();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showShortSafe(msg);
        }
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
    }

    @Override
    public void onMobileActive(String message) {
    }

    @Override
    public void onUnavailable(String message) {
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

    public interface OnSelectedInterface {
        void onSelectedFragment(BaseFragment selectedFragment);
    }
}
