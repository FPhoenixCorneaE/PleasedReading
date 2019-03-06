package com.wkz.dialog.animation.window;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolWindowExit extends CoolBaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1f);
        animatorSet.playTogether(oa1, oa2);
    }
}
