package com.wkz.videoplayer.window;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FRMoveType {

    public static final int fixed = 0;
    public static final int free = 1;
    public static final int active = 2;
    public static final int slide = 3;
    public static final int back = 4;

    @IntDef({fixed, free, active, slide, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface MOVE_TYPE {}
}
