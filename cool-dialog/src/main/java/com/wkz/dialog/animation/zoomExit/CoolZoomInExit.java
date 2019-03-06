package com.wkz.dialog.animation.zoomExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolZoomInExit extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.25f, 0),//
				ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.25f, 0),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 0)//
		);
	}
}
