package com.wkz.framework.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wkz.framework.BuildConfig;
import com.wkz.framework.models.GlideApp;
import com.wkz.okgo.OkGo;

/**
 * 初始化服务,主要用于初始化一些影响应用启动速度的耗时任务,从而优化启动速度
 */
public class FRInitializeService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FRInitializeService() {
        super("FRInitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //初始化图片框架
        initPictureFrame();

        //初始化打印日志
        initLogger();

        //初始化下载、上传框架
        OkGo.getInstance().init(getApplication());
    }

    /**
     * Glide初始化耗时分析：Glide的初始化会加载所有配置的Module，
     * 然后初始化RequestManager（包括网络层、工作线程等，比较耗时），
     * 最后还要应用一些解码的选项（Options）
     * 在Application的onCreate方法中，在工作线程调用一次GlideApp.get(this)可优化启动速度
     */
    private void initPictureFrame() {
        //获取一个Glide对象，Glide内部会进行初始化操作
        GlideApp.get(this);
    }

    /**
     * 初始化打印日志
     */
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
