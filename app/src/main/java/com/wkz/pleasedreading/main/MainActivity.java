package com.wkz.pleasedreading.main;

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
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.main.gank.GankFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.IMainView, DrawerLayout.DrawerListener {

    @BindView(R.id.pr_fl_container)
    FrameLayout prFlContainer;
    @BindView(R.id.pr_dl_drawer)
    DrawerLayout prDlDrawer;
    @BindView(R.id.pr_fl_menu)
    FrameLayout prFlMenu;

    private MainPresenter mMainPresenter;

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

        FragmentUtils.addFragment(mContext, R.id.pr_fl_container, new GankFragment(), null, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        prDlDrawer.addDrawerListener(this);
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

        //设置控件最先出现的位置
        double paddingLeft = drawerViewWidth * (1 - 0.618) * (1 - slideOffset);
        prFlMenu.setPadding((int) paddingLeft, 0, 0, 0);
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
}
