package com.wkz.dialog.animation.slideExit;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolSlideBottomExit extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "translationY", 0, 250 * dm.density), //
				ObjectAnimator.ofFloat(view, "alpha", 1, 0)
		);
	}
}
