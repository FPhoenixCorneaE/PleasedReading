package com.wkz.okgo.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 优先级的枚举类
 */
@IntDef({
        OkPriority.UI_TOP,
        OkPriority.UI_NORMAL,
        OkPriority.UI_LOW,
        OkPriority.DEFAULT,
        OkPriority.BG_TOP,
        OkPriority.BG_NORMAL,
        OkPriority.BG_LOW
})
@Retention(RetentionPolicy.SOURCE)
public @interface OkPriority {
    int UI_TOP = Integer.MAX_VALUE;
    int UI_NORMAL = 1000;
    int UI_LOW = 100;
    int DEFAULT = 0;
    int BG_TOP = -100;
    int BG_NORMAL = -1000;
    int BG_LOW = Integer.MIN_VALUE;
}
