package com.wkz.framework.constants;


import android.annotation.SuppressLint;
import android.os.Environment;

import com.wkz.utils.AppUtils;
import com.wkz.utils.ContextUtils;

import java.io.File;

/**
 * 文件夹目录
 */

public class FRFilesDirectory {

    /**
     * 本应用内部缓存：/data/data/com.xxx.xxx/cache
     */
    public static final File DIR_INNER_CACHE = ContextUtils.getContext().getCacheDir();
    /**
     * 外部缓存：/mnt/sdcard/android/data/com.xxx.xxx/cache
     */
    public static final File DIR_EXTERNAL_CACHE = ContextUtils.getContext().getExternalCacheDir();
    /**
     * 本应用文件：/data/data/com.xxx.xxx/files
     */
    public static final File DIR_FILES = ContextUtils.getContext().getFilesDir();
    /**
     * 本应用数据库：/data/data/com.xxx.xxx/databases
     */
    @SuppressLint("SdCardPath")
    public static final File DIR_DATABASE = new File("/data/data/" + ContextUtils.getContext().getPackageName() + "/databases");
    /**
     * 图片下载：/mnt/sdcard/应用名/images
     */
    @SuppressLint("SdCardPath")
    public static final File DIR_DOWNLOAD_IMAGES = new File(Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName() + "/images");
    /**
     * 视频下载：/mnt/sdcard/应用名/videos
     */
    @SuppressLint("SdCardPath")
    public static final File DIR_DOWNLOAD_VIDEOS = new File(Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName() + "/videos");

}
