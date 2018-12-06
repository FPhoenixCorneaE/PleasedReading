package com.wkz.bannerlayout.utils;

import android.support.annotation.NonNull;

import com.wkz.bannerlayout.animation.FRAccordionTransformer;
import com.wkz.bannerlayout.animation.FRBackgroundToForegroundTransformer;
import com.wkz.bannerlayout.animation.FRBannerTransformer;
import com.wkz.bannerlayout.animation.FRCubeInTransformer;
import com.wkz.bannerlayout.animation.FRCubeOutTransformer;
import com.wkz.bannerlayout.animation.FRDefaultTransformer;
import com.wkz.bannerlayout.animation.FRDepthPageTransformer;
import com.wkz.bannerlayout.animation.FRDrawerTransformer;
import com.wkz.bannerlayout.animation.FRFlipHorizontalTransformer;
import com.wkz.bannerlayout.animation.FRFlipVerticalTransformer;
import com.wkz.bannerlayout.animation.FRForegroundToBackgroundTransformer;
import com.wkz.bannerlayout.animation.FRRotateDownTransformer;
import com.wkz.bannerlayout.animation.FRRotateUpTransformer;
import com.wkz.bannerlayout.animation.FRScaleInOutTransformer;
import com.wkz.bannerlayout.animation.FRStackTransformer;
import com.wkz.bannerlayout.animation.FRTabletTransformer;
import com.wkz.bannerlayout.animation.FRVerticalTransformer;
import com.wkz.bannerlayout.animation.FRZoomInTransformer;
import com.wkz.bannerlayout.animation.FRZoomOutPageTransformer;
import com.wkz.bannerlayout.animation.FRZoomOutSlideTransformer;
import com.wkz.bannerlayout.animation.FRZoomOutTranformer;
import com.wkz.bannerlayout.annotation.FRPageTransformerMode;

public class FRTransformerUtils {

    @NonNull
    public static FRBannerTransformer getTransformer(int type) {
        @FRPageTransformerMode
        int transformerType = type;
        switch (transformerType) {
            case FRPageTransformerMode.ANIMATION_ACCORDION:
                return new FRAccordionTransformer();
            case FRPageTransformerMode.ANIMATION_BACKGROUND:
                return new FRBackgroundToForegroundTransformer();
            case FRPageTransformerMode.ANIMATION_CUBE_IN:
                return new FRCubeInTransformer();
            case FRPageTransformerMode.ANIMATION_CUBE_OUT:
                return new FRCubeOutTransformer();
            case FRPageTransformerMode.ANIMATION_DEFAULT:
                return new FRDefaultTransformer();
            case FRPageTransformerMode.ANIMATION_DEPTH_PAGE:
                return new FRDepthPageTransformer();
            case FRPageTransformerMode.ANIMATION_DRAWER:
                return new FRDrawerTransformer();
            case FRPageTransformerMode.ANIMATION_FLIPHORIZONTAL:
                return new FRFlipHorizontalTransformer();
            case FRPageTransformerMode.ANIMATION_FLIPVERTICAL:
                return new FRFlipVerticalTransformer();
            case FRPageTransformerMode.ANIMATION_FOREGROUND:
                return new FRForegroundToBackgroundTransformer();
            case FRPageTransformerMode.ANIMATION_ROTATEDOWN:
                return new FRRotateDownTransformer();
            case FRPageTransformerMode.ANIMATION_ROTATEUP:
                return new FRRotateUpTransformer();
            case FRPageTransformerMode.ANIMATION_SCALEINOUT:
                return new FRScaleInOutTransformer();
            case FRPageTransformerMode.ANIMATION_STACK:
                return new FRStackTransformer();
            case FRPageTransformerMode.ANIMATION_TABLET:
                return new FRTabletTransformer();
            case FRPageTransformerMode.ANIMATION_VERTICAL:
                return new FRVerticalTransformer();
            case FRPageTransformerMode.ANIMATION_ZOOMIN:
                return new FRZoomInTransformer();
            case FRPageTransformerMode.ANIMATION_ZOOMOUTPAGE:
                return new FRZoomOutPageTransformer();
            case FRPageTransformerMode.ANIMATION_ZOOMOUTSLIDE:
                return new FRZoomOutSlideTransformer();
            case FRPageTransformerMode.ANIMATION_ZOOMOUT:
                return new FRZoomOutTranformer();
            default:
                return new FRDefaultTransformer();
        }
    }
}
