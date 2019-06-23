package com.wkz.bannerlayout.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRProgressShapeMode.RECTANGLE,
        FRProgressShapeMode.RING
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRProgressShapeMode {
    int RECTANGLE = 0;
    int RING = 1;
}
