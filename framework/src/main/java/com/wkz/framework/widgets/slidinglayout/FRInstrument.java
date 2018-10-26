package com.wkz.framework.widgets.slidinglayout;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Linhh on 16/5/24.
 */
class FRInstrument {
    private static volatile FRInstrument mFRInstrument;

    private FRInstrument() {
    }

    public static FRInstrument getInstance() {
        if (mFRInstrument == null) {
            synchronized (FRInstrument.class) {
                if (mFRInstrument == null) {
                    mFRInstrument = new FRInstrument();
                }
            }
        }
        return mFRInstrument;
    }

    float getTranslationY(View view) {
        if (view == null) {
            return 0;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            return view.getTranslationY();
        } else {
            return ViewHelper.getTranslationY(view);
        }
    }

    void slidingByDelta(View view, float delta) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            view.setTranslationY(delta);
        } else {
            ViewHelper.setTranslationY(view, delta);
        }
    }

    public void slidingToY(View view, float y) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            view.setY(y);
        } else {
            ViewHelper.setY(view, y);
        }
    }

    void reset(View view, long duration) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.animation.ObjectAnimator.ofFloat(view, "translationY", 0F).setDuration(duration).start();
        } else {
            com.nineoldandroids.animation.ObjectAnimator.ofFloat(view, "translationY", 0F).setDuration(duration).start();
        }
    }

    void smoothTo(View view, float y, long duration) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.animation.ObjectAnimator.ofFloat(view, "translationY", y).setDuration(duration).start();
        } else {
            com.nineoldandroids.animation.ObjectAnimator.ofFloat(view, "translationY", y).setDuration(duration).start();
        }
    }
}
