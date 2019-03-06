package com.wkz.dialog.animation.attention;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolShakeHorizontal extends CoolBaseAnimatorSet {

	public CoolShakeHorizontal() {
		duration = 1000;
	}

	/**
	 * <pre>
	 *  另一种shake实现
	 * ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
	 * </pre>
	 */
	@Override
	public void setAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -10, 10);
		animator.setInterpolator(new CycleInterpolator(5));
		animatorSet.playTogether(animator);
	}
}
