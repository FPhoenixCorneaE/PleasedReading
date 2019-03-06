package com.wkz.dialog.animation.zoomEnter;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.View.MeasureSpec;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolZoomInRightEnter extends CoolBaseAnimatorSet {

	public CoolZoomInRightEnter() {
		duration = 750;
	}

	@Override
	public void setAnimation(View view) {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int w = view.getMeasuredWidth();

		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.475f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.475f, 1),//
				ObjectAnimator.ofFloat(view, "translationX", w, -48, 0),//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1, 1)
		);
	}
}
