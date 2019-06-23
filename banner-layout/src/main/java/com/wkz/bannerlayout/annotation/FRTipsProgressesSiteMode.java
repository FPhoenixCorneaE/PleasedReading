package com.wkz.bannerlayout.annotation;

import androidx.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRTipsProgressesSiteMode.LEFT,
        FRTipsProgressesSiteMode.TOP,
        FRTipsProgressesSiteMode.RIGHT,
        FRTipsProgressesSiteMode.BOTTOM,
        FRTipsProgressesSiteMode.CENTER,
        FRTipsProgressesSiteMode.CENTER_HORIZONTAL,
        FRTipsProgressesSiteMode.CENTER_VERTICAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRTipsProgressesSiteMode {
    int LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
    int TOP = RelativeLayout.ALIGN_PARENT_TOP;
    int RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
    int BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
    int CENTER = RelativeLayout.CENTER_IN_PARENT;
    int CENTER_HORIZONTAL = RelativeLayout.CENTER_HORIZONTAL;
    int CENTER_VERTICAL = RelativeLayout.CENTER_VERTICAL;
}
