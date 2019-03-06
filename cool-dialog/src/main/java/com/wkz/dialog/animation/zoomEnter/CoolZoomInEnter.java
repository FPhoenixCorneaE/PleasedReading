package com.wkz.dialog.animation.zoomEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.wkz.dialog.animation.CoolBaseAnimatorSet;

public class CoolZoomInEnter extends CoolBaseAnimatorSet {

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1),//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1)//
		);
	}
}
