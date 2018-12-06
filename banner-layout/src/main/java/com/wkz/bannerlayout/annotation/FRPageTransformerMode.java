package com.wkz.bannerlayout.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRPageTransformerMode.ANIMATION_ACCORDION,
        FRPageTransformerMode.ANIMATION_BACKGROUND,
        FRPageTransformerMode.ANIMATION_CUBE_IN,
        FRPageTransformerMode.ANIMATION_CUBE_OUT,
        FRPageTransformerMode.ANIMATION_DEFAULT,
        FRPageTransformerMode.ANIMATION_DEPTH_PAGE,
        FRPageTransformerMode.ANIMATION_DRAWER,
        FRPageTransformerMode.ANIMATION_FLIPHORIZONTAL,
        FRPageTransformerMode.ANIMATION_FLIPVERTICAL,
        FRPageTransformerMode.ANIMATION_FOREGROUND,
        FRPageTransformerMode.ANIMATION_ROTATEDOWN,
        FRPageTransformerMode.ANIMATION_ROTATEUP,
        FRPageTransformerMode.ANIMATION_SCALEINOUT,
        FRPageTransformerMode.ANIMATION_STACK,
        FRPageTransformerMode.ANIMATION_TABLET,
        FRPageTransformerMode.ANIMATION_VERTICAL,
        FRPageTransformerMode.ANIMATION_ZOOMIN,
        FRPageTransformerMode.ANIMATION_ZOOMOUTPAGE,
        FRPageTransformerMode.ANIMATION_ZOOMOUTSLIDE,
        FRPageTransformerMode.ANIMATION_ZOOMOUT
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRPageTransformerMode {
     int ANIMATION_ACCORDION = 0;
     int ANIMATION_BACKGROUND = 1;
     int ANIMATION_CUBE_IN = 2;
     int ANIMATION_CUBE_OUT = 3;
     int ANIMATION_DEFAULT = 4;
     int ANIMATION_DEPTH_PAGE = 5;
     int ANIMATION_FLIPHORIZONTAL = 6;
     int ANIMATION_FLIPVERTICAL = 7;
     int ANIMATION_FOREGROUND = 8;
     int ANIMATION_ROTATEDOWN = 9;
     int ANIMATION_ROTATEUP = 10;
     int ANIMATION_SCALEINOUT = 11;
     int ANIMATION_STACK = 12;
     int ANIMATION_TABLET = 13;
     int ANIMATION_VERTICAL = 14;
     int ANIMATION_ZOOMIN = 15;
     int ANIMATION_ZOOMOUTPAGE = 16;
     int ANIMATION_ZOOMOUTSLIDE = 17;
     int ANIMATION_ZOOMOUT = 18;
     int ANIMATION_DRAWER = 19;
}

