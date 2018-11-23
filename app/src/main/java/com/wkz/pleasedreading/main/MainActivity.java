package com.wkz.pleasedreading.main;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.wkz.framework.base.BaseActivity;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.utils.FragmentUtils;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivityMainBinding;
import com.wkz.pleasedreading.main.gank.PRGankChildFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.IMainView, DrawerLayout.DrawerListener {

    @BindView(R.id.pr_fl_container)
    FrameLayout prFlContainer;
    @BindView(R.id.pr_dl_drawer)
    DrawerLayout prDlDrawer;
    @BindView(R.id.pr_fl_sidebar_content)
    FrameLayout prFlSidebarContent;

    private MainPresenter mMainPresenter;
    private PrActivityMainBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_main;
    }

    @NonNull
    @Override
    public BasePresenter createPresenter() {
        return mMainPresenter = new MainPresenter(this, this);
    }

    @Override
    public BaseModel createModel() {
        return ModelFactory.createModel(MainModel.class);
    }

    @Override
    public void initView() {

//        FragmentUtils.addFragment(mContext, R.id.pr_fl_container, new PRGankFragment(), null, false);
        FragmentUtils.addFragment(mContext, R.id.pr_fl_container, new PRGankChildFragment(), null, false);

        mDataBinding = (PrActivityMainBinding) mViewDataBinding;
        mDataBinding.setAvatarUrl("http://img0.imgtn.bdimg.com/it/u=1365742167,1660121271&fm=26&gp=0.jpg");
        mDataBinding.setTitle("Gank");
        mDataBinding.setNickname("烽火戏诸侯");
        mDataBinding.setMenuList(new ArrayList<String>() {
            private static final long serialVersionUID = 4548353330276131214L;

            {
                add("Gank");
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
        prDlDrawer.addDrawerListener(this);
        mDataBinding.setOnMainClickListener(new OnMainClickListener().setDrawerLayout(prDlDrawer));
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
        prFlContainer.setTranslationX(drawerViewWidth * slideOffset);

        //设置侧边栏内容控件最先出现的位置
        double translationX = drawerViewWidth * 0.618 * (1 - slideOffset);
        prFlSidebarContent.setTranslationX((float) translationX);
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
     *
     * @param prIvAvatar
     * @param avatarUrl
     */
    @BindingAdapter({"avatarUrl"})
    public static void loadAvatar(FRGlideImageView prIvAvatar, String avatarUrl) {
        prIvAvatar.loadCircleImage(avatarUrl, R.drawable.pr_shape_placeholder_avatar_circle);
    }
}
