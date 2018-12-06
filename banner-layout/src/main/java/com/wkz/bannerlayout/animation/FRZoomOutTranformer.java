package com.wkz.bannerlayout.animation;

import android.view.View;

@SuppressWarnings("ALL")
public class FRZoomOutTranformer extends FRBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        float scale = 1f + Math.abs(position);
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        if (position == -1) {
            view.setTranslationX(view.getWidth() * -1);
        }
    }

}
