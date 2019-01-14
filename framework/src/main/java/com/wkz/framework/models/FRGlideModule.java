package com.wkz.framework.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 自定义GlideModule子类，设置内存缓存、Bitmap 池、磁盘缓存、、默认请求选项、解码格式等等
 */
@GlideModule
public class FRGlideModule extends AppGlideModule {

    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(
                new LruResourceCache(
                        (new MemorySizeCalculator.Builder(context))
                                .setMemoryCacheScreens(2.0F)
                                .build()
                                .getMemoryCacheSize()));
    }
}