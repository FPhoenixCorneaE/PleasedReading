package com.wkz.dialog.animation.fallEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFallEnter extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(view, "scaleX", 2f, 1.5f, 1f).setDuration(duration),//
				ObjectAnimator.ofFloat(view, "scaleY", 2f, 1.5f, 1f).setDuration(duration),//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1f).setDuration(duration)
		);
	}
}
