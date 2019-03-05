package com.wkz.framework.functions.file.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class FRVideoBean implements Serializable {

    private int id;
    private String path;
    private String name;
    private String resolution;// 分辨率
    private long size;
    private long date;
    private long duration;
    private Bitmap thumbnail;

    public FRVideoBean(int id, String path, String name, String resolution, long size, long date, long duration) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    public FRVideoBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "FRVideoBean{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", resolution='" + resolution + '\'' +
                ", size=" + size +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }
}
