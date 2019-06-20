package com.wkz.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 上下文工具类,在Application的onCreate()中进行初始化
 *
 * @author wkz
 * @date 2019/06/20 21:33
 */
public class ContextUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    private ContextUtils() {
        throw new UnsupportedOperationException("U can't initialize me...");
    }

    /**
     * 初始化上下文
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (sContext == null) {
            throw new NullPointerException("U should call init method first!");
        }
        return sContext;
    }

    /**
     * 销毁
     */
    public static void dispose() {
        sContext = null;
    }
}
