package com.wkz.pleasedreading.main;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.utils.FragmentUtils;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.main.gank.PRGankFragment;
import com.wkz.pleasedreading.main.toutiao.PRTouTiaoFragment;

public class OnMainClickListener {

    private PRGankFragment mPrGankFragment;
    private PRTouTiaoFragment mPrTouTiaoFragment;
    private DrawerLayout drawerLayout;

    public OnMainClickListener(PRGankFragment mPrGankFragment) {
        this.mPrGankFragment = mPrGankFragment;
    }

    /**
     * 必须带着View参数
     *
     * @param view
     */
    public void openDrawer(View view) {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    /**
     * 可以不必带着View参数
     *
     * @param view
     * @param context
     * @param drawerLayout
     */
    public void clickGank(View view, FRBaseActivity context, DrawerLayout drawerLayout) {
        context.materialRipple(view, v -> {
            if (mPrGankFragment == null) {
                mPrGankFragment = new PRGankFragment();
            }
            FragmentUtils.hideAndShowFragment(context, R.id.pr_fl_container, mPrTouTiaoFragment, mPrGankFragment, null, false);
            if (drawerLayout != null) {
                drawerLayout.closeDrawers();
            }
        });
    }

    /**
     * 可以不必带着View参数
     *
     * @param view
     * @param context
     * @param drawerLayout
     */
    public void clickTouTiaoVideo(View view, FRBaseActivity context, DrawerLayout drawerLayout) {
        context.materialRipple(view, v -> {
            if (mPrTouTiaoFragment == null) {
                mPrTouTiaoFragment = new PRTouTiaoFragment();
            }
            FragmentUtils.hideAndShowFragment(context, R.id.pr_fl_container, mPrGankFragment, mPrTouTiaoFragment, null, false);
            if (drawerLayout != null) {
                drawerLayout.closeDrawers();
            }
        });
    }

    public OnMainClickListener setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
        return this;
    }
}
