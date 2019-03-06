package com.wkz.dialog.animation.zoomExit;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.View.MeasureSpec;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolZoomOutLeftExit extends CoolBaseAnimatorSet {

	public CoolZoomOutLeftExit() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int w = view.getMeasuredWidth();

		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 1, 0),//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.475f, 0.1f),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.475f, 0.1f),//
				ObjectAnimator.ofFloat(view, "translationX", 0, 42, -w)
		);
	}
}
