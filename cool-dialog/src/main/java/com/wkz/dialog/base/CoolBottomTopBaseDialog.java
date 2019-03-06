package com.wkz.dialog.base;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public abstract class CoolBottomTopBaseDialog<T extends CoolBottomTopBaseDialog<T>> extends CoolBaseDialog<T> {

    protected View mAnimateView;
    private CoolBaseAnimatorSet mWindowEnterAnim;
    private CoolBaseAnimatorSet mWindowExitAnim;
    protected Animation mInnerShowAnim;
    protected Animation mInnerDismissAnim;
    protected long mInnerAnimDuration = 350;
    protected boolean mIsInnerShowAnim;
    protected boolean mIsInnerDismissAnim;
    protected int mLeft, mTop, mRight, mBottom;

    public CoolBottomTopBaseDialog(Context context) {
        super(context);
    }

    /**
     * set duration for inner com.flyco.dialog.animation of mAnimateView(设置animateView内置动画时长)
     */
    public T innerAnimDuration(long innerAnimDuration) {
        mInnerAnimDuration = innerAnimDuration;
        return (T) this;
    }

    public T padding(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
        return (T) this;
    }

    /**
     * show dialog and mAnimateView with inner show com.flyco.dialog.animation(设置dialog和animateView显示动画)
     */
    protected void showWithAnim() {
        if (mInnerShowAnim != null) {
            mInnerShowAnim.setDuration(mInnerAnimDuration);
            mInnerShowAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsInnerShowAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsInnerShowAnim = false;
                }
            });
            mLlControlHeight.startAnimation(mInnerShowAnim);
        }

        if (mAnimateView != null) {
            if (getWindowEnterAnim() != null) {
                mWindowEnterAnim = getWindowEnterAnim();
            }
            mWindowEnterAnim.duration(mInnerAnimDuration).playOn(mAnimateView);
        }
    }

    /**
     * dismiss dialog and mAnimateView with inner dismiss com.flyco.dialog.animation(设置dialog和animateView消失动画)
     */
    protected void dismissWithAnim() {
        if (mInnerDismissAnim != null) {
            mInnerDismissAnim.setDuration(mInnerAnimDuration);
            mInnerDismissAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsInnerDismissAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsInnerDismissAnim = false;
                    superDismiss();
                }
            });

            mLlControlHeight.startAnimation(mInnerDismissAnim);
        } else {
            superDismiss();
        }

        if (mAnimateView != null) {
            if (getWindowExitAnim() != null) {
                mWindowExitAnim = getWindowExitAnim();
            }
            mWindowExitAnim.duration(mInnerAnimDuration).playOn(mAnimateView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsInnerDismissAnim || mIsInnerShowAnim) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (mIsInnerDismissAnim || mIsInnerShowAnim) {
            return;
        }
        super.onBackPressed();
    }

    protected abstract CoolBaseAnimatorSet getWindowEnterAnim();

    protected abstract CoolBaseAnimatorSet getWindowExitAnim();
}
