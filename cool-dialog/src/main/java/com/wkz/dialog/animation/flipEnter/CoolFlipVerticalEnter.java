package com.wkz.dialog.animation.flipEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipVerticalEnter extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				// ObjectAnimator.ofFloat(view, "rotationX", -90, 0));
				ObjectAnimator.ofFloat(view, "rotationX", 90, 0)
		);
	}
}
