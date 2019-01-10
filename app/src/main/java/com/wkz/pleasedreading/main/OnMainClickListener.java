package com.wkz.pleasedreading.main;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.wkz.framework.base.FRBaseActivity;

public interface OnMainClickListener {

    /**
     * 必须带着View参数
     *
     * @param view
     */
    void openDrawer(View view);

    /**
     * 可以不必带着View参数
     *
     * @param view
     * @param context
     * @param drawerLayout
     */
    void clickGank(View view, FRBaseActivity context, DrawerLayout drawerLayout);

    /**
     * 可以不必带着View参数
     *
     * @param view
     * @param context
     * @param drawerLayout
     */
    void clickTouTiaoVideo(View view, FRBaseActivity context, DrawerLayout drawerLayout);

}
