package com.wkz.framework.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.wkz.framework.model.GlideApp;

/**
 * 初始化服务,主要用于初始化一些影响应用启动速度的耗时任务,从而优化启动速度
 */
public class InitializeService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public InitializeService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //获取一个Glide对象，Glide内部会进行初始化操作
        GlideApp.get(this);
    }
}
