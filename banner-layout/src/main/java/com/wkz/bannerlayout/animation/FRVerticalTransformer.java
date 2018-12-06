package com.wkz.bannerlayout.animation;

import android.view.View;

public class FRVerticalTransformer extends FRBannerTransformer {

    @Override
    public void transformPage(View view, float position) {
        float alpha = 0;
        if (0 <= position && position <= 1) {
            alpha = 1 - position;
        } else if (-1 < position && position < 0) {
            alpha = position + 1;
        }
        view.setAlpha(alpha);
        float transX = view.getWidth() * -position;
        view.setTranslationX(transX);
        float transY = position * view.getHeight();
        view.setTranslationY(transY);
    }
}
