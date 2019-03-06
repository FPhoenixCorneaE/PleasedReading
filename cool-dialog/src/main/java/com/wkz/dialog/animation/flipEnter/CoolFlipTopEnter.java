package com.wkz.dialog.animation.flipEnter;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolFlipTopEnter extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "rotationX", 90, 0),//
				ObjectAnimator.ofFloat(view, "translationY", -200 * dm.density, 0), //
				ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1)
		);
	}
}
