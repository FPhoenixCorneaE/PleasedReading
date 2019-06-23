package com.wkz.bannerlayout.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.NonNull;

public class FRBannerSelectorUtils {

    @NonNull
    public static StateListDrawable getDrawableSelector(Context context, float enabledRadius, int enabledColor, float normalRadius, int normalColor) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_enabled}, getShape(context, enabledRadius, enabledColor));
        drawable.addState(new int[]{-android.R.attr.state_enabled}, getShape(context, normalRadius, normalColor));
        return drawable;
    }

    @NonNull
    public static GradientDrawable getShape(Context context, float radius, int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(FRDisplayUtils.dp2px(context, radius));
        gd.setColor(color);
        return gd;
    }
}
