package com.wkz.bannerlayout.annotation;

import androidx.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRTipsTitleSiteMode.LEFT,
        FRTipsTitleSiteMode.TOP,
        FRTipsTitleSiteMode.RIGHT,
        FRTipsTitleSiteMode.BOTTOM,
        FRTipsTitleSiteMode.CENTER,
        FRTipsTitleSiteMode.CENTER_HORIZONTAL,
        FRTipsTitleSiteMode.CENTER_VERTICAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRTipsTitleSiteMode {
    int LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
    int TOP = RelativeLayout.ALIGN_PARENT_TOP;
    int RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
    int BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
    int CENTER = RelativeLayout.CENTER_IN_PARENT;
    int CENTER_HORIZONTAL = RelativeLayout.CENTER_HORIZONTAL;
    int CENTER_VERTICAL = RelativeLayout.CENTER_VERTICAL;
}
