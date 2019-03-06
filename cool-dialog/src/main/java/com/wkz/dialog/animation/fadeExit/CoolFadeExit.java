package com.wkz.dialog.animation.fadeExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFadeExit extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration)
		);
	}
}
