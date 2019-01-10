package com.wkz.pleasedreading.main;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.utils.FragmentUtils;
import com.wkz.framework.utils.GlideUtils;
import com.wkz.framework.widgets.FRInsLoadingView;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivityMainBinding;
import com.wkz.pleasedreading.main.gank.PRGankFragment;
import com.wkz.pleasedreading.main.toutiao.PRTouTiaoFragment;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;
import com.wkz.videoplayer.window.FRFloatWindow;

import java.util.ArrayList;

public class PRMainActivity extends FRBaseActivity<PRMainContract.IMainPresenter> implements PRMainContract.IMainView, DrawerLayout.DrawerListener, OnMainClickListener {

    private PrActivityMainBinding mDataBinding;
    private ArrayList<String> mMenuList;
    private PRGankFragment mPrGankFragment;
    private PRTouTiaoFragment mPrTouTiaoFragment;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_main;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRMainPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRMainModel.class);
    }

    @Override
    public void initView() {
        mDataBinding = (PrActivityMainBinding) mViewDataBinding;

        //layout:pr_content_main添加id后会导致id:pr_fl_container找不到
        FragmentUtils.addFragment(mContext, R.id.pr_fl_container, mPrGankFragment = new PRGankFragment(), null, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mDataBinding.setContext(mContext);
        mDataBinding.setAvatarUrl("http://img0.imgtn.bdimg.com/it/u=1365742167,1660121271&fm=26&gp=0.jpg");
        mDataBinding.setMenuList(mMenuList = new ArrayList<String>() {
            private static final long serialVersionUID = 4548353330276131214L;

            {
                add("Gank");
                add("头条视频");
            }
        });
        mDataBinding.setTitle(mMenuList.get(0));
        mDataBinding.setNickname("烽火戏诸侯");
    }

    @Override
    public void initListener() {
        mDataBinding.prDlDrawer.addDrawerListener(this);
        mDataBinding.setOnMainClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FRVideoPlayerManager.instance().releaseVideoPlayer();
        if (FRFloatWindow.get() != null) {
            FRFloatWindow.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (FRVideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        //设置主布局随菜单滑动而滑动
        int drawerViewWidth = drawerView.getWidth();
        mDataBinding.prDlDrawer.findViewById(R.id.pr_fl_container).setTranslationX(drawerViewWidth * slideOffset);

        //设置侧边栏内容控件最先出现的位置
        double translationX = drawerViewWidth * 0.618 * (1 - slideOffset);
        mDataBinding.prSidebarLayout.prFlSidebarContent.setTranslationX((float) translationX);
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {

    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    /**
     * 加载头像背景
     * 方法必须用static修饰
     *
     * @param prIvAvatarBg
     * @param avatarUrl
     */
    @BindingAdapter({"avatarBgUrl"})
    public static void loadAvatarBg(FRGlideImageView prIvAvatarBg, String avatarUrl) {
        prIvAvatarBg.loadImage(avatarUrl, R.drawable.pr_shape_placeholder_grey);
    }

    /**
     * 加载头像
     * 方法必须用static修饰
     *
     * @param prIvAvatar
     * @param avatarUrl
     */
    @BindingAdapter({"avatarUrl"})
    public static void loadAvatar(FRGlideImageView prIvAvatar, String avatarUrl) {
        prIvAvatar.loadCircleImage(avatarUrl, R.drawable.pr_shape_placeholder_avatar_circle);
    }

    /**
     * 加载头像Loading
     * 方法必须用static修饰
     *
     * @param prIlvAvatar
     * @param avatarUrl
     */
    @BindingAdapter({"avatarLoadingUrl"})
    public static void loadAvatarLoading(FRInsLoadingView prIlvAvatar, String avatarUrl) {
        GlideUtils.setupCircleImagePlaceDrawableRes(prIlvAvatar, avatarUrl, R.drawable.pr_shape_placeholder_avatar_circle);
    }

    @Override
    public void openDrawer(View view) {
        mDataBinding.prDlDrawer.openDrawer(Gravity.START);
    }

    @Override
    public void clickGank(View view, FRBaseActivity context, DrawerLayout drawerLayout) {
        if (mPrGankFragment == null) {
            mPrGankFragment = new PRGankFragment();
        }
        FragmentUtils.hideAndShowFragment(context, R.id.pr_fl_container, mPrTouTiaoFragment, mPrGankFragment, null, false);
        mDataBinding.setTitle(mMenuList.get(0));
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void clickTouTiaoVideo(View view, FRBaseActivity context, DrawerLayout drawerLayout) {
        if (mPrTouTiaoFragment == null) {
            mPrTouTiaoFragment = new PRTouTiaoFragment();
        }
        FragmentUtils.hideAndShowFragment(context, R.id.pr_fl_container, mPrGankFragment, mPrTouTiaoFragment, null, false);
        mDataBinding.setTitle(mMenuList.get(1));
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }
}
