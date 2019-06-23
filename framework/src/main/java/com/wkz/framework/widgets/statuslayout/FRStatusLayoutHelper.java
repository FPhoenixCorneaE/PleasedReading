package com.wkz.framework.widgets.statuslayout;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * 替换布局帮助类
 */
class FRStatusLayoutHelper {

    /**
     * 需要替换的 View
     */
    private View contentLayout;
    /**
     * contentLayout 的父 ViewGroup
     */
    private ViewGroup parentLayout;
    /**
     * 当前显示的 View
     */
    private View currentLayout;

    FRStatusLayoutHelper(@NonNull View contentLayout) {
        this.contentLayout = contentLayout;
        getContentLayoutParams();
    }

    /**
     * 获取 contentLayout 的参数信息 LayoutParams、Parent
     */
    private void getContentLayoutParams() {
        if (contentLayout.getParent() != null) {
            // 有直接的父控件
            this.parentLayout = (ViewGroup) contentLayout.getParent();
        } else {
            // 认为 contentLayout 是 activity 的根布局
            // 所以它的父控件就是 android.R.id.content
            this.parentLayout = contentLayout.getRootView().findViewById(android.R.id.content);
        }
        if (parentLayout == null) {
            // 以上两种方法还没有获取到父控件
            // contentLayout 非 activity 的根布局
            // 父控件就是自己
            if (contentLayout instanceof ViewGroup) {
                parentLayout = (ViewGroup) contentLayout;
            } else {
                // 否则，contentLayout 是一个非 ViewGroup 的根布局
                // 该情况，没有办法替换布局，因此不支持
                throw new IllegalStateException("参数错误：FRStatusLayoutManager#Build#with() 方法，不能传如一个非 ViewGroup 的根布局");
            }
        } else {
            int count = parentLayout.getChildCount();
            if (count > 1) {
                throw new IllegalStateException("父控件只能有contentLayout这一个子控件！");
            }
        }
        this.currentLayout = this.contentLayout;
    }

    void showStatusLayout(View view) {
        if (view == null) {
            return;
        }
        if (currentLayout != view) {
            if (parentLayout.getChildCount() > 1) {
                parentLayout.removeViewsInLayout(1, parentLayout.getChildCount() - 1);
            }
            if (view != contentLayout) {
                parentLayout.addView(view);
            }
            currentLayout = view;
        }
    }

    void restoreLayout() {
        showStatusLayout(contentLayout);
    }

}
