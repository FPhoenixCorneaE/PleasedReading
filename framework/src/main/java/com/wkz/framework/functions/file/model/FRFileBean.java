package com.wkz.framework.functions.file.model;

import java.io.Serializable;

public class FRFileBean implements Serializable {
    /**
     * 文件的路径
     */
    private String path;
    /**
     * 文件大小
     */
    private long size;

    public FRFileBean(String path, long size) {
        this.path = path;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FRFileBean{" +
                "path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
