package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;


import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlash extends CoolBaseAnimatorSet {

    public CoolFlash() {
        duration = 1000;
    }

    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "alpha", 1, 0, 1, 0, 1)
        );
    }
}
