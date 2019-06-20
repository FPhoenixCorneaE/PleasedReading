package com.wkz.framework;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.wkz.framework.services.FRInitializeService;
import com.wkz.utils.ContextUtils;

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

        ContextUtils.init(getContext());

        //启动初始化服务，优化启动速度
        startInitializeService();
    }

    /**
     * 初始化服务,主要用于初始化一些影响应用启动速度的耗时任务,从而优化启动速度
     */
    private void startInitializeService() {
        startService(new Intent(getContext(), FRInitializeService.class));
    }
}
