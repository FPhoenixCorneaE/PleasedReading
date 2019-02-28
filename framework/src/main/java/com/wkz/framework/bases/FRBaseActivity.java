package com.wkz.framework.bases;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.gyf.barlibrary.OnKeyboardListener;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wkz.framework.R;
import com.wkz.framework.annotations.FRNetworkState;
import com.wkz.framework.constants.FRConstant;
import com.wkz.framework.functions.network.FRNetworkManager;
import com.wkz.framework.functions.network.OnNetworkChangedListener;
import com.wkz.framework.models.FRActivityAnimator;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.framework.widgets.ripple.FRMaterialRippleLayout;
import com.wkz.framework.widgets.statuslayout.FRStatusLayoutManager;
import com.wkz.framework.widgets.statuslayout.OnStatusLayoutClickListener;

import java.lang.reflect.InvocationTargetException;

public abstract class FRBaseActivity<P extends IFRBasePresenter>
        extends RxAppCompatActivity
        implements IFRBaseView, OnStatusLayoutClickListener, OnNetworkChangedListener, IFRSelectedFragment {

    private static final String NAME_ACTIVITY = FRBaseActivity.class.getName();
    protected FRBaseActivity mContext;
    protected ViewDataBinding mViewDataBinding;
    private FRStatusLayoutManager mFRStatusLayoutManager;
    private View mContentView;
    protected P mPresenter;
    private Fragment mCurrentFragment;
    private InputMethodManager mInputMethodManager;
    /**
     * 网络状态
     */
    protected int mNetworkState;

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
        mPresenter = (P) createPresenter();

        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

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

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式状态栏
     */
    protected void initImmersionBar() {
        ImmersionBar.with(mContext)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.fr_colorPrimary)     //状态栏颜色，不写默认透明色
                .navigationBarColor(R.color.fr_colorPrimary) //导航栏颜色，不写默认黑色
                .barColor(R.color.fr_colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.fr_colorAccent)  //修改flyme OS状态栏字体颜色
                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//                .titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
//                .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .supportActionBar(true) //支持ActionBar使用
                .statusBarColorTransform(R.color.fr_color_white)  //状态栏变色后的颜色
                .navigationBarColorTransform(R.color.fr_color_white) //导航栏变色后的颜色
                .barColorTransform(R.color.fr_color_white)  //状态栏和导航栏变色后的颜色
//                .removeSupportView(toolbar)  //移除指定view支持
                .removeSupportAllView() //移除全部view支持
                .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                .fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .reset()  //重置所以沉浸式参数
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        Logger.e("软键盘是否弹出" + isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                })
                .init();  //必须调用方可沉浸式
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册网络变化监听
        FRNetworkManager.getInstance().registerNetwork(mContext, this);
        // 非必加
        // 如果你的app可以横竖屏切换，适配了华为emui3系列系统手机，并且navigationBarWithEMUI3Enable为true，
        // 请在onResume方法里添加这句代码（同时满足这三个条件才需要加上代码哦：1、横竖屏可以切换；2、华为emui3系列系统手机；3、navigationBarWithEMUI3Enable为true）
        // 否则请忽略
//        if (OSUtils.isEMUI3_x() && isImmersionBarEnabled()) {
//            ImmersionBar.with(mContext).init();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //反注册网络变化监听
        if (mContext != null) {
            FRNetworkManager.getInstance().unregisterNetwork(mContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInputMethodManager = null;
        if (isImmersionBarEnabled()) {
            // 必须调用该方法，防止内存泄漏
            ImmersionBar.with(mContext).destroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_x()) && isImmersionBarEnabled()) {
            // 非必加
            // 如果你的app可以横竖屏切换，适配了4.4或者华为emui3.1系统手机，并且navigationBarWithKitkatEnable为true，
            // 请务必在onConfigurationChanged方法里添加如下代码（同时满足这三个条件才需要加上代码哦：1、横竖屏可以切换；2、android4.4或者华为emui3.1系统手机；3、navigationBarWithKitkatEnable为true）
            // 否则请忽略
            ImmersionBar.with(mContext).init();
        }
    }

    @Override
    public void onBackPressed() {
        //如果Fragment的onBackPressed()返回false，就会进入条件内进行默认的操作
        if (mCurrentFragment == null ||
                (mCurrentFragment instanceof FRBaseFragment && !((FRBaseFragment) mCurrentFragment).onBackPressed()) ||
                (mCurrentFragment instanceof FRBaseDialogFragment && !((FRBaseDialogFragment) mCurrentFragment).onBackPressed())) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        //隐藏软键盘
        hideSoftKeyBoard();
        //执行退出动画
        doExitAnimation();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mInputMethodManager != null)) {
            this.mInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    /**
     * 退出动画
     */
    private void doExitAnimation() {
        try {
            //退出动画,如果界面切换动画无效果则可以去手机系统设置-->开发者选项-->过渡动画比例，设置比例
            FRActivityAnimator anim = new FRActivityAnimator();
            @FRActivityAnimator.Animator
            String activityAnimator = getIntent().getStringExtra(FRConstant.ACTIVITY_ANIMATION);
            switch (activityAnimator) {
                case FRActivityAnimator.Animator.PULL_RIGHT_PUSH_LEFT:
                    anim.getClass().getMethod(FRActivityAnimator.Animator.PULL_LEFT_PUSH_RIGHT, Activity.class).invoke(anim, mContext);
                    break;
                case FRActivityAnimator.Animator.SCALE_IN_SCALE_OUT:
                    anim.getClass().getMethod(FRActivityAnimator.Animator.SCALE_IN_SCALE_OUT, Activity.class).invoke(anim, mContext);
                    break;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            Logger.e(e.toString());
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
        mNetworkState = FRNetworkState.WifiActive;
        if (mCurrentFragment instanceof OnNetworkChangedListener) {
            ((OnNetworkChangedListener) mCurrentFragment).onWifiActive(message);
        }
    }

    @Override
    public void onMobileActive(String message) {
        Logger.i(message);
        mNetworkState = FRNetworkState.MobileActive;
        ToastUtils.showShortSafe(message);
        if (mCurrentFragment instanceof OnNetworkChangedListener) {
            ((OnNetworkChangedListener) mCurrentFragment).onMobileActive(message);
        }
    }

    @Override
    public void onUnavailable(String message) {
        Logger.i(message);
        mNetworkState = FRNetworkState.Unavailable;
        ToastUtils.showShortSafe(message);
        if (mCurrentFragment instanceof OnNetworkChangedListener) {
            ((OnNetworkChangedListener) mCurrentFragment).onUnavailable(message);
        }
    }

    @Override
    public void onSelectedFragment(Fragment selectedFragment) {
        this.mCurrentFragment = selectedFragment;
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
