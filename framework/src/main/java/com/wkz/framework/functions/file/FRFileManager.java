package com.wkz.framework.functions.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wkz.framework.functions.file.model.FRAppBean;
import com.wkz.framework.functions.file.model.FRFileBean;
import com.wkz.framework.functions.file.model.FRImgFolderBean;
import com.wkz.framework.functions.file.model.FRMusicBean;
import com.wkz.framework.functions.file.model.FRVideoBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理者, 可以获取本机的各种文件
 */
public class FRFileManager {

    private static volatile FRFileManager mInstance;
    private static final Object mLock = new Object();
    private ContentResolver mContentResolver;

    private FRFileManager(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public static FRFileManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new FRFileManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取本机音乐列表
     */
    public List<FRMusicBean> getMusics() {
        ArrayList<FRMusicBean> musicBeans = new ArrayList<>();
        try (Cursor c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)) {

            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径

                    if (!new File(path).exists()) {
                        continue;
                    }

                    String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                    String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                    String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                    int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                    int time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                    // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                    FRMusicBean musicBean = new FRMusicBean(name, path, album, artist, size, duration);
                    musicBeans.add(musicBean);
                }
            }

        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return musicBeans;
    }

    /**
     * 获取本机视频列表
     */
    public List<FRVideoBean> getVideos() {
        List<FRVideoBean> videoBeans = new ArrayList<>();
        try (Cursor c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER)) {
            // String[] mediaColumns = { "_id", "_data", "_display_name",
            // "_size", "date_modified", "duration", "resolution" };
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                    if (!new File(path).exists()) {
                        continue;
                    }

                    int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
                    String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
                    String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //分辨率
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
                    long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
                    long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));//修改时间

                    FRVideoBean videoBean = new FRVideoBean(id, path, name, resolution, size, date, duration);
                    videoBean.setThumbnail(getVideoThumbnail(id));
                    videoBeans.add(videoBean);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return videoBeans;
    }

    /**
     * 获取某个文件夹下的视频列表
     */
    public List<FRVideoBean> getVideos(String dir) {
        List<FRVideoBean> videoBeans = new ArrayList<>();
        if (TextUtils.isEmpty(dir)) {
            return videoBeans;
        }
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) {
            return videoBeans;
        }

        File[] files = file.listFiles();

        for (File temp : files) {
            if (temp.exists() && temp.canRead() && FRFileUtils.isVideo(temp)) {
                FRVideoBean videoBean = new FRVideoBean();
                if (!TextUtils.isEmpty(FRFileUtils.getFileExtension(temp))) {
                    videoBean.setName(temp.getName().substring(0, temp.getName().indexOf(FRFileUtils.getFileExtension(temp))));
                } else {
                    videoBean.setName(temp.getName());
                }
                videoBean.setPath(temp.getPath());
                videoBean.setThumbnail(getVideoThumbnail(temp.getPath()));
                videoBeans.add(videoBean);
            }
        }
        return videoBeans;
    }

    /**
     * 获取视频缩略图
     *
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            Logger.e(e.toString());
        } catch (RuntimeException e) {
            Logger.e(e.toString());

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                Logger.e(e.toString());
            }
        }
        return b;
    }

    /**
     * 获取视频缩略图
     *
     * @param id
     * @return
     */
    public Bitmap getVideoThumbnail(int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
    }

    /**
     * 通过文件类型得到相应文件的集合
     **/
    public List<FRFileBean> getFilesByType(int fileType) {
        List<FRFileBean> files = new ArrayList<>();
        // 扫描files文件库
        try (Cursor c = mContentResolver.query(MediaStore.Files.getContentUri("external"),
                new String[]{"_id", "_data", "_size"},
                null,
                null,
                null)) {
            int dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE);

            while (c.moveToNext()) {
                String path = c.getString(dataindex);

                if (FRFileUtils.getFileType(path) == fileType) {
                    if (!FRFileUtils.isExists(path)) {
                        continue;
                    }
                    long size = c.getLong(sizeindex);
                    FRFileBean fileBean = new FRFileBean(path, size);
                    files.add(fileBean);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return files;
    }

    /**
     * 得到图片文件夹集合
     */
    public List<FRImgFolderBean> getImageFolders() {
        List<FRImgFolderBean> folders = new ArrayList<>();
        // 扫描图片
        try (Cursor c = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                new String[]{"image/jpg", "image/jpeg", "image/png", "image/webp", "image/gif"},
                MediaStore.Images.Media.DATE_MODIFIED)) {
            //用于保存已经添加过的文件夹目录
            List<String> mDirs = new ArrayList<>();
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;

                    String dir = parentFile.getAbsolutePath();
                    if (mDirs.contains(dir))//如果已经添加过
                        continue;

                    mDirs.add(dir);//添加到保存目录的集合中
                    FRImgFolderBean folderBean = new FRImgFolderBean();
                    folderBean.setDir(dir);
                    folderBean.setFistImgPath(path);
                    if (parentFile.list() == null)
                        continue;
                    int count = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return FRFileUtils.isPicFile(filename);
                        }
                    }).length;

                    folderBean.setCount(count);
                    folders.add(folderBean);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return folders;
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (!directory.exists()) {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (FRFileUtils.isPicFile(path)) {
                imgPaths.add(path);
            }
        }
        return imgPaths;
    }

    /**
     * 获取已安装apk的列表
     *
     * @param mContext
     */
    public List<FRAppBean> getAppInfos(Context mContext) {
        ArrayList<FRAppBean> appBeans = new ArrayList<>();
        //获取到包的管理者
        PackageManager packageManager = mContext.getPackageManager();
        //获得所有的安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        //遍历每个安装包，获取对应的信息
        for (PackageInfo packageInfo : installedPackages) {
            FRAppBean appBean = new FRAppBean();

            appBean.setApplicationInfo(packageInfo.applicationInfo);
            appBean.setVersionCode(packageInfo.getLongVersionCode());

            //得到icon
            Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
            appBean.setIcon(drawable);

            //得到程序的名字
            String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appBean.setApkName(apkName);

            //得到程序的包名
            String packageName = packageInfo.packageName;
            appBean.setApkPackageName(packageName);

            //得到程序的资源文件夹
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file = new File(sourceDir);

            //得到apk的大小
            long size = file.length();
            appBean.setApkSize(size);

            //获取到安装应用程序的标记
            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //表示系统app
                appBean.setIsUserApp(false);
            } else {
                //表示用户app
                appBean.setIsUserApp(true);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //表示在sd卡
                appBean.setIsRom(false);
            } else {
                //表示内存
                appBean.setIsRom(true);
            }
            appBeans.add(appBean);
        }
        return appBeans;
    }
}
