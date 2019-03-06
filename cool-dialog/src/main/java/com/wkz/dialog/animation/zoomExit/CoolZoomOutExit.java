package com.wkz.dialog.animation.zoomExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolZoomOutExit extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 0),//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.3f, 0),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.3f, 0)//
		);
	}
}
