package com.wkz.bannerlayout.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wkz.bannerlayout.animation.FRVerticalTransformer;

import java.lang.reflect.Field;

public class FRBannerViewPager extends ViewPager {
    private boolean mViewTouchMode;
    private boolean isVertical;
    private FRFixedSpeedScroller scroller;

    public final int getDuration() {
        return this.scroller.getFixDuration();
    }

    @NonNull
    public final FRBannerViewPager setViewTouchMode(boolean b) {
        if (b && !this.isFakeDragging()) {
            this.beginFakeDrag();
        } else if (!b && this.isFakeDragging()) {
            this.endFakeDrag();
        }

        this.mViewTouchMode = b;
        return this;
    }

    @NonNull
    public final FRBannerViewPager setVertical(boolean vertical) {
        this.isVertical = vertical;
        if (this.isVertical) {
            this.setPageTransformer(true, new FRVerticalTransformer());
        }

        return this;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    protected void onDetachedFromWindow() {
        Context context = this.getContext();
        if (context == null) {
            throw new ClassCastException("null cannot be cast to non-null type android.app.Activity");
        } else {
            if (((Activity)context).isFinishing()) {
                super.onDetachedFromWindow();
            }

        }
    }

    /**
     * When mViewTouchMode is true, ViewPager does not intercept the click event, and the click event will be handled by the childView
     */
    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        return this.isVertical ? !this.mViewTouchMode && super.onInterceptTouchEvent(this.swapEvent(event)) : !this.mViewTouchMode && super.onInterceptTouchEvent(event);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        return this.isVertical ? super.onTouchEvent(this.swapEvent(ev)) : super.onTouchEvent(ev);
    }

    /**
     * In the mViewTouchMode true or sliding direction is not about time, ViewPager will give up control of click events,
     * This is conducive to the ListView in the ListView can be added, such as sliding control, or sliding between the two will have a conflict
     */
    public boolean arrowScroll(int direction) {
        return !this.mViewTouchMode && (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT) && super.arrowScroll(direction);
    }

    /**
     * Set the switching speed
     */
    @NonNull
    public final FRBannerViewPager setDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            this.scroller = new FRFixedSpeedScroller(getContext());
            mScroller.set(this, this.scroller);
            this.scroller.setDuration(duration);
        } catch (Exception var3) {
            Log.i(this.getClass().getSimpleName(), var3.getMessage());
        }

        return this;
    }

    private MotionEvent swapEvent(MotionEvent event) {
        float width = (float)this.getWidth();
        float height = (float)this.getHeight();
        float swappedX = event.getY() / height * width;
        float swappedY = event.getX() / width * height;
        event.setLocation(swappedX, swappedY);
        return event;
    }

    public FRBannerViewPager(@NonNull Context context) {
        super(context);
    }

    public FRBannerViewPager(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }
}
