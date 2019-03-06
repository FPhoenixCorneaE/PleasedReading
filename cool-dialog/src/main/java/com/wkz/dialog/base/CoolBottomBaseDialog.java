package com.wkz.dialog.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;
import com.wkz.dialog.animation.window.CoolWindowEnter;
import com.wkz.dialog.animation.window.CoolWindowExit;

public abstract class CoolBottomBaseDialog<T extends CoolBottomBaseDialog<T>> extends CoolBottomTopBaseDialog<T> {

    public CoolBottomBaseDialog(Context context, View animateView) {
        super(context);
        mAnimateView = animateView;

        mInnerShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);

        mInnerDismissAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
    }

    public CoolBottomBaseDialog(Context context) {
        this(context, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLlTop.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mLlTop.setGravity(Gravity.BOTTOM);
        if (getWindow() != null) {
            getWindow().setGravity(Gravity.BOTTOM);
        }
        mLlTop.setPadding(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showWithAnim();
    }

    @Override
    public void dismiss() {
        dismissWithAnim();
    }

    private CoolBaseAnimatorSet mWindowEnterAnim;
    private CoolBaseAnimatorSet mWindowExitAnim;

    @Override
    protected CoolBaseAnimatorSet getWindowEnterAnim() {
        if (mWindowEnterAnim == null) {
            mWindowEnterAnim = new CoolWindowEnter();
        }
        return mWindowEnterAnim;
    }

    @Override
    protected CoolBaseAnimatorSet getWindowExitAnim() {
        if (mWindowExitAnim == null) {
            mWindowExitAnim = new CoolWindowExit();
        }
        return mWindowExitAnim;
    }
}
