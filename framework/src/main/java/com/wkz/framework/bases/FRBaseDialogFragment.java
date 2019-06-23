package com.wkz.framework.bases;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;
import com.wkz.framework.R;
import com.wkz.framework.annotations.FRNetworkState;
import com.wkz.framework.functions.network.OnNetworkChangedListener;
import com.wkz.utils.ToastUtils;
import com.wkz.framework.widgets.ripple.FRMaterialRippleLayout;
import com.wkz.framework.widgets.statuslayout.FRStatusLayoutManager;
import com.wkz.framework.widgets.statuslayout.OnStatusLayoutClickListener;

public abstract class FRBaseDialogFragment<P extends IFRBasePresenter>
        extends RxAppCompatDialogFragment
        implements IFRBaseView, OnStatusLayoutClickListener, OnNetworkChangedListener {

    private static final String NAME_FRAGMENT = FRBaseDialogFragment.class.getName();
    protected FRBaseActivity mContext;
    protected ViewDataBinding mViewDataBinding;
    private View mContentView;
    private FRStatusLayoutManager mFRStatusLayoutManager;
    protected P mPresenter;
    /**
     * 标识fragment视图已经初始化完毕
     */
    private boolean mIsViewPrepared;
    /**
     * 标识已经触发过懒加载数据
     */
    private boolean mHasFetchData;
    private IFRSelectedFragment mISelectedFragment;
    /**
     * 窗口
     */
    protected Window mWindow;
    /**
     * 屏幕宽度
     */
    protected int mWidth;
    /**
     * 屏幕高度
     */
    protected int mHeight;
    /**
     * 网络状态
     */
    protected int mNetworkState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(NAME_FRAGMENT);
        if (getActivity() instanceof IFRSelectedFragment) {
            this.mISelectedFragment = (IFRSelectedFragment) getActivity();
        }

        //全屏透明主题
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FRTheme_Dialog_Normal);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mISelectedFragment != null) {
            mISelectedFragment.onSelectedFragment(this);
        }

        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getActivity() != null) {
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
                mWidth = dm.widthPixels;
                mHeight = dm.heightPixels;
            }
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }

        setGravity();
        setAnimation();
        setLayout();
    }

    /**
     * 若要修改重力方向可重写该方法
     */
    protected void setGravity() {
        mWindow.setGravity(Gravity.TOP);
    }

    /**
     * 若要修改动画可重写该方法
     */
    protected void setAnimation() {
        mWindow.setWindowAnimations(R.style.FRAnimation_TopPopup);
    }

    /**
     * 若要修改宽高可重写该方法
     */
    protected void setLayout() {
        mWindow.setLayout(mWidth, mHeight / 2);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mContext = (FRBaseActivity) context;
        } else {
            mContext = (FRBaseActivity) getActivity();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(mContext).destroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
        mNetworkState = FRNetworkState.WifiActive;
    }

    @Override
    public void onMobileActive(String message) {
        mNetworkState = FRNetworkState.MobileActive;
    }

    @Override
    public void onUnavailable(String message) {
        mNetworkState = FRNetworkState.Unavailable;
    }

    /**
     * 初始化沉浸式状态栏
     */
    public void initImmersionBar() {
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

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    public boolean isImmersionBarEnabled() {
        return true;
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
