package com.wkz.okserver.upload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface OkUploadType {
    String Video = "video";//视频
    String Image = "image";//图片
    String Music = "music";//音乐
    String Document = "document";//文档
    String RAR = "rar";//压缩包
    String APK = "apk";//安装包
    String LargeFile = "largefile";//大文件
}
