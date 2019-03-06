package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolRubberBand extends CoolBaseAnimatorSet {

	public CoolRubberBand() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 1.25f, 0.75f, 1.15f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.75f, 1.25f, 0.85f, 1)
		);
	}
}
