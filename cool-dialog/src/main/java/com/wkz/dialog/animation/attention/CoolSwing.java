package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolSwing extends CoolBaseAnimatorSet {

	public CoolSwing() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 1, 1, 1, 1, 1, 1, 1),//
				ObjectAnimator.ofFloat(view, "rotation", 0, 10, -10, 6, -6, 3, -3, 0)
		);
	}
}
