package com.wkz.framework.widgets.glideimageview.progress;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * 自定义GlideModule子类，设置内存缓存、Bitmap 池、磁盘缓存、、默认请求选项、解码格式等等
 */
@GlideModule
public class FRProgressAppGlideModule extends AppGlideModule {

    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(
                new LruResourceCache(
                        (new MemorySizeCalculator.Builder(context))
                                .setMemoryCacheScreens(2.0F)
                                .build()
                                .getMemoryCacheSize()));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(FRProgressManager.getOkHttpClient()));
    }
}