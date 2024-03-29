package com.wkz.dialog.animation.slideEnter;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolSlideBottomEnter extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "translationY", 250 * dm.density, 0), //
				ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1)
		);
	}
}
