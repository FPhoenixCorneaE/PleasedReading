package com.wkz.dialog.animation.flipEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipHorizontalEnter extends CoolBaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				// ObjectAnimator.ofFloat(view, "rotationY", -90, 0));
				ObjectAnimator.ofFloat(view, "rotationY", 90, 0)
		);
	}
}
