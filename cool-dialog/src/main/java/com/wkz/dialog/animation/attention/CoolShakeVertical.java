package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolShakeVertical extends CoolBaseAnimatorSet {

	public CoolShakeVertical() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10, 10);
		animator.setInterpolator(new CycleInterpolator(5));
		animatorSet.playTogether(animator);
	}
}
