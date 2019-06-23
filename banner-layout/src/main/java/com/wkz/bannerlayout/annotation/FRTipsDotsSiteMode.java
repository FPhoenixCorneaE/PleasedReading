package com.wkz.bannerlayout.annotation;

import androidx.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRTipsDotsSiteMode.LEFT,
        FRTipsDotsSiteMode.TOP,
        FRTipsDotsSiteMode.RIGHT,
        FRTipsDotsSiteMode.BOTTOM,
        FRTipsDotsSiteMode.CENTER,
        FRTipsDotsSiteMode.CENTER_HORIZONTAL,
        FRTipsDotsSiteMode.CENTER_VERTICAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRTipsDotsSiteMode {
    int LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
    int TOP = RelativeLayout.ALIGN_PARENT_TOP;
    int RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
    int BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
    int CENTER = RelativeLayout.CENTER_IN_PARENT;
    int CENTER_HORIZONTAL = RelativeLayout.CENTER_HORIZONTAL;
    int CENTER_VERTICAL = RelativeLayout.CENTER_VERTICAL;
}
