package com.wkz.framework;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wkz.framework.services.FRInitializeService;

public class FRApplication extends Application {

    private static volatile FRApplication instance;

    public static FRApplication getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化图片框架
        initPictureFrame();

        initLogger();
    }

    /**
     * Glide初始化耗时分析：Glide的初始化会加载所有配置的Module，
     * 然后初始化RequestManager（包括网络层、工作线程等，比较耗时），
     * 最后还要应用一些解码的选项（Options）
     * 在Application的onCreate方法中，在工作线程调用一次GlideApp.get(this)可优化启动速度
     */
    private void initPictureFrame() {
        startService(new Intent(getContext(), FRInitializeService.class));
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("FRLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}
