package com.wkz.framework.functions.file.model;

import java.io.Serializable;

/**
 * 图片文件夹的bean类
 */
public class FRImgFolderBean implements Serializable {
    /**
     * 当前文件夹的路径
     */
    private String dir;
    /**
     * 第一张图片的路径
     */
    private String fistImgPath;
    /**
     * 文件夹名
     */
    private String name;
    /**
     * 文件夹中图片的数量
     */
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex = dir.lastIndexOf("/");
        this.name = dir.substring(lastIndex + 1);
    }

    public String getFistImgPath() {
        return fistImgPath;
    }

    public void setFistImgPath(String fistImgPath) {
        this.fistImgPath = fistImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public FRImgFolderBean() {
    }

    public FRImgFolderBean(String dir, String fistImgPath, String name, int count) {
        this.dir = dir;
        this.fistImgPath = fistImgPath;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "FRImgFolderBean{" +
                "dir='" + dir + '\'' +
                ", fistImgPath='" + fistImgPath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
