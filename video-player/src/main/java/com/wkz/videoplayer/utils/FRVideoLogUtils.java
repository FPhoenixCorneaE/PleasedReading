package com.wkz.videoplayer.utils;

import android.util.Log;

import com.wkz.videoplayer.BuildConfig;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : log工具
 *     revise:
 * </pre>
 */
public final class FRVideoLogUtils {

    private static final String TAG = "FRVideoPlayer";
    private static boolean isLog = BuildConfig.DEBUG;

    /**
     * 设置是否开启日志
     *
     * @param isLog 是否开启日志
     */
    public static void setIsLog(boolean isLog) {
        FRVideoLogUtils.isLog = isLog;
    }

    public static void d(String message) {
        if (isLog) {
            Log.d(TAG, message);
        }
    }

    public static void i(String message) {
        if (isLog) {
            Log.i(TAG, message);
        }
    }

    public static void w(String message) {
        if (isLog) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message) {
        if (isLog) {
            Log.e(TAG, message);
        }
    }

    public static void e(String message, Throwable throwable) {
        if (isLog) {
            Log.e(TAG, message, throwable);
        }
    }
}
