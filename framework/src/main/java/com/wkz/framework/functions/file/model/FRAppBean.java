package com.wkz.framework.functions.file.model;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class FRAppBean implements Serializable {

    private ApplicationInfo applicationInfo;
    private long versionCode;
    /**
     * 图片的icon
     */
    private Drawable icon;

    /**
     * 程序的名字
     */
    private String apkName;

    /**
     * 程序大小
     */
    private long apkSize;

    /**
     * 表示到底是用户app还是系统app
     * 如果表示为true 就是用户app
     * 如果是false表示系统app
     */
    private boolean isUserApp;

    /**
     * 放置的位置
     */
    private boolean isRom;

    /**
     * 包名
     */
    private String apkPackageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }
}
