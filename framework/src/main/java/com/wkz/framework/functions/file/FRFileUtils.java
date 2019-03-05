package com.wkz.framework.functions.file;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

public class FRFileUtils {

    public static final String[] VIDEO_EXTENSIONS = {
            "3gp", "wmv", "ts", "3gp2", "rmvb",
            "mp4", "mov", "m4v", "avi", "3gpp",
            "3gpp2", "mkv", "flv", "divx", "f4v",
            "rm", "avb", "asf", "ram", "avs",
            "mpg", "v8", "swf", "m2v", "asx",
            "ra", "ndivx", "box", "xvid"};
    private static final HashSet<String> mVideoHash;

    static {
        mVideoHash = new HashSet<>(Arrays.asList(VIDEO_EXTENSIONS));
    }

    /**
     * 文档类型
     */
    public static final int TYPE_DOC = 0;
    /**
     * apk类型
     */
    public static final int TYPE_APK = 1;
    /**
     * 压缩包类型
     */
    public static final int TYPE_ZIP = 2;


    /**
     * 判断文件是否存在
     *
     * @param path 文件的路径
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断文件的类型
     *
     * @param path 文件的路径
     */
    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc")
                || path.endsWith(".docx")
                || path.endsWith(".xls")
                || path.endsWith(".xlsx")
                || path.endsWith(".ppt")
                || path.endsWith(".pptx")) {
            return TYPE_DOC;
        } else if (path.endsWith(".apk")) {
            return TYPE_APK;
        } else if (path.endsWith(".zip")
                || path.endsWith(".rar")
                || path.endsWith(".tar")
                || path.endsWith(".gz")) {
            return TYPE_ZIP;
        } else {
            return -1;
        }
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        return path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".png")
                || path.endsWith(".webp")
                || path.endsWith(".gif");
    }

    /**
     * 是否是视频文件
     *
     * @param f
     */
    public static boolean isVideo(File f) {
        final String ext = getFileExtension(f);
        return mVideoHash.contains(ext);
    }

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取文件后缀
     */
    public static String getFileExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            return getExtFromFilename(filename);
        }
        return "";
    }

    /**
     * 从文件的全名得到文件的拓展名
     *
     * @param filename
     * @return
     */
    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 读取文件的修改时间
     *
     * @param f
     * @return
     */
    public static String getModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        cal.setTimeInMillis(time);
        // System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }
}
