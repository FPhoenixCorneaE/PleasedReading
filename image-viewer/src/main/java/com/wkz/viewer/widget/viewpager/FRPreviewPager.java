package com.wkz.viewer.widget.viewpager;


import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 图片缩放时java.lang.IllegalArgumentException: pointerIndex out of range解决方案
 * 捕获IllegalArgumentException（非法参数异常）异常
 */
public class FRPreviewPager extends ViewPager {
    // 是否可滑动
    private boolean isScrollable;

    public FRPreviewPager(Context context) {
        super(context);
    }

    public FRPreviewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScrollable) {
            super.scrollTo(x, y);
        }
    }

    /**
     * 设置是否可滑动
     *
     * @param isScrollable
     */
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }
}
