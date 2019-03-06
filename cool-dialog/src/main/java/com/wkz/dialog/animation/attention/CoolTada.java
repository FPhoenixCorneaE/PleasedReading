package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolTada extends CoolBaseAnimatorSet {

	public CoolTada() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),//
				ObjectAnimator.ofFloat(view, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0)//
		);
	}
}
