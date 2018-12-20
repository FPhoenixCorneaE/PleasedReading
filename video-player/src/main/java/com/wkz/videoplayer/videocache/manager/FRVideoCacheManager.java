package com.wkz.videoplayer.videocache.manager;

import android.content.Context;

import com.wkz.videoplayer.videocache.file.FRMd5FileNameGenerator;
import com.wkz.videoplayer.videocache.proxy.FRHttpProxyCacheServer;

/**
 * 视频离线缓存管理器
 */
public class FRVideoCacheManager {


    private static FRHttpProxyCacheServer mHttpProxyCacheServer;

    public static FRHttpProxyCacheServer getProxy(Context context) {
        if (mHttpProxyCacheServer == null) {
            synchronized (FRVideoCacheManager.class) {
                if (mHttpProxyCacheServer == null) {
                    mHttpProxyCacheServer = newProxy(context);
                }
            }
        }
        return mHttpProxyCacheServer;
    }

    private static FRHttpProxyCacheServer newProxy(Context context) {
        return new FRHttpProxyCacheServer.Builder(context.getApplicationContext())
                .maxCacheSize((long) (Math.pow(1024, 3) * 2))       // 2 Gb for cache
                .maxCacheFilesCount(30)
                .fileNameGenerator(new FRMd5FileNameGenerator())
                .build();
    }
}
