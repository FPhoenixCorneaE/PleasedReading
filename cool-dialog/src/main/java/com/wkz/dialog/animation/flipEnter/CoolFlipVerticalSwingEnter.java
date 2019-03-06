package com.wkz.dialog.animation.flipEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipVerticalSwingEnter extends CoolBaseAnimatorSet {

	public CoolFlipVerticalSwingEnter() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "rotationX", 90, -10, 10, 0),//
				ObjectAnimator.ofFloat(view, "alpha", 0.25f, 0.5f, 0.75f, 1)
		);
	}
}
