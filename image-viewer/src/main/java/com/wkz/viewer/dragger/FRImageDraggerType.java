package com.wkz.viewer.dragger;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({FRImageDraggerType.DRAG_TYPE_DEFAULT,
        FRImageDraggerType.DRAG_TYPE_WX,})
@Retention(RetentionPolicy.SOURCE)
public @interface FRImageDraggerType {
    // 默认的拖拽模式
    int DRAG_TYPE_DEFAULT = 1;
    // 微信朋友圈的拖拽模式
    int DRAG_TYPE_WX = 2;
}
