package com.wkz.bannerlayout.utils;

import android.content.Context;

public class FRDisplayUtils {

    /**
     * Convert dp to px by the density of phone
     */
    public static int dp2px(Context mContext,float dp) {
        return (int) (dpToPx(mContext,dp) + 0.5f);
    }

    /**
     * Convert dp to px
     */
    public static float dpToPx(Context mContext, float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    /**
     * Convert px to dp by the density of phone
     */
    public static int px2dp(Context mContext,float px) {
        return (int) (pxToDp(mContext,px) + 0.5f);
    }

    /**
     * Convert px to dp
     */
    public static float pxToDp(Context mContext,float px) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    /**
     * Convert px to sp
     */
    public static int px2sp(Context mContext,float px) {
        return (int) (pxToSp(mContext,px) + 0.5f);
    }

    /**
     * Convert px to sp
     */
    public static float pxToSp(Context mContext,float px) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    /**
     * Convert sp to px
     */
    public static int sp2px(Context mContext,float sp) {
        return (int) (spToPx(mContext,sp) + 0.5f);
    }

    /**
     * Convert sp to px
     */
    public static float spToPx(Context mContext,float sp) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale;
    }
}
