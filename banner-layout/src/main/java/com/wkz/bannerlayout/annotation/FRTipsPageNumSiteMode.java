package com.wkz.bannerlayout.annotation;

import android.support.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRTipsPageNumSiteMode.TOP_LEFT,
        FRTipsPageNumSiteMode.TOP_RIGHT,
        FRTipsPageNumSiteMode.BOTTOM_LEFT,
        FRTipsPageNumSiteMode.BOTTOM_RIGHT,
        FRTipsPageNumSiteMode.CENTER_LEFT,
        FRTipsPageNumSiteMode.CENTER_RIGHT,
        FRTipsPageNumSiteMode.TOP_CENTER,
        FRTipsPageNumSiteMode.BOTTOM_CENTER
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRTipsPageNumSiteMode {
    int TOP_LEFT = RelativeLayout.LEFT_OF;
    int TOP_RIGHT = RelativeLayout.RIGHT_OF;
    int BOTTOM_LEFT = RelativeLayout.ABOVE;
    int BOTTOM_RIGHT = RelativeLayout.BELOW;
    int CENTER_LEFT = RelativeLayout.ALIGN_BASELINE;
    int CENTER_RIGHT = RelativeLayout.ALIGN_LEFT;
    int TOP_CENTER = RelativeLayout.ALIGN_TOP;
    int BOTTOM_CENTER = RelativeLayout.ALIGN_RIGHT;
}
