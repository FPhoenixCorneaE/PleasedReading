package com.wkz.dialog.animation.flipEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipHorizontalSwingEnter extends CoolBaseAnimatorSet {

	public CoolFlipHorizontalSwingEnter() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "rotationY", 90, -10, 10, 0),//
				ObjectAnimator.ofFloat(view, "alpha", 0.25f, 0.5f, 0.75f, 1)
		);
	}
}
