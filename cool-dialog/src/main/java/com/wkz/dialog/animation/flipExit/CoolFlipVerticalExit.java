package com.wkz.dialog.animation.flipExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipVerticalExit extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(view, "rotationX", 0, 90),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0)
		);
	}
}
