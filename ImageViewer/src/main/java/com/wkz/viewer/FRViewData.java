package com.wkz.viewer;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FRViewData implements Serializable,Parcelable {
    // 目标 view 的 x 轴坐标
    private float targetX;
    // 目标 view 的 y 轴坐标
    private float targetY;
    // 目标 view 的宽度
    private float targetWidth;
    // 目标 view 的高度
    private float targetHeight;
    // 图片的原始宽度
    private float imageWidth;
    // 图片的原始高度
    private float imageHeight;

    public FRViewData() {

    }

    public FRViewData(float targetX, float targetY, float targetWidth, float targetHeight) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    protected FRViewData(Parcel in) {
        targetX = in.readFloat();
        targetY = in.readFloat();
        targetWidth = in.readFloat();
        targetHeight = in.readFloat();
        imageWidth = in.readFloat();
        imageHeight = in.readFloat();
    }

    public static final Creator<FRViewData> CREATOR = new Creator<FRViewData>() {
        @Override
        public FRViewData createFromParcel(Parcel in) {
            return new FRViewData(in);
        }

        @Override
        public FRViewData[] newArray(int size) {
            return new FRViewData[size];
        }
    };

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
    }

    public float getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(float targetHeight) {
        this.targetHeight = targetHeight;
    }

    public float getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(float imageWidth) {
        this.imageWidth = imageWidth;
    }

    public float getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(float imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(targetX);
        dest.writeFloat(targetY);
        dest.writeFloat(targetWidth);
        dest.writeFloat(targetHeight);
        dest.writeFloat(imageWidth);
        dest.writeFloat(imageHeight);
    }

    @Override
    public String toString() {
        return "FRViewData{" +
                "targetX=" + targetX +
                ", targetY=" + targetY +
                ", targetWidth=" + targetWidth +
                ", targetHeight=" + targetHeight +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                '}';
    }
}
