package com.wkz.bannerlayout.annotation;

import android.support.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRTipsLayoutSiteMode.LEFT,
        FRTipsLayoutSiteMode.TOP,
        FRTipsLayoutSiteMode.RIGHT,
        FRTipsLayoutSiteMode.BOTTOM,
        FRTipsLayoutSiteMode.CENTER
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRTipsLayoutSiteMode {
    int LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
    int TOP = RelativeLayout.ALIGN_PARENT_TOP;
    int RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
    int BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
    int CENTER = RelativeLayout.CENTER_IN_PARENT;
}
