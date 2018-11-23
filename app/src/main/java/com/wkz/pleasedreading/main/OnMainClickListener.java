package com.wkz.pleasedreading.main;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.wkz.framework.base.BaseActivity;

public class OnMainClickListener {

    private DrawerLayout drawerLayout;

    /**
     * 必须带着View参数
     *
     * @param view
     */
    public void clickGank(View view) {
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }

    public void clickGank(View view, BaseActivity context,DrawerLayout drawerLayout) {
        context.materialRipple(view, v -> {
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
