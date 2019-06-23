package com.wkz.framework.widgets.glideimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.wkz.framework.widgets.glideimageview.progress.OnGlideImageViewListener;
import com.wkz.framework.widgets.glideimageview.progress.OnProgressListener;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class FRGlideImageView extends FRShapeImageView {

    private FRGlideImageLoader mImageLoader;

    public FRGlideImageView(Context context) {
        this(context, null);
    }

    public FRGlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FRGlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mImageLoader = FRGlideImageLoader.create(this);
    }

    public FRGlideImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = FRGlideImageLoader.create(this);
        }
        return mImageLoader;
    }

    public String getImageUrl() {
        return getImageLoader().getImageUrl();
    }

    public RequestOptions requestOptions(int placeholderResId) {
        return getImageLoader().requestOptions(placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return getImageLoader().circleRequestOptions(placeholderResId);
    }

    public FRGlideImageView load(int resId, RequestOptions options) {
        getImageLoader().load(resId, options);
        return this;
    }

    public FRGlideImageView load(Uri uri, RequestOptions options) {
        getImageLoader().load(uri, options);
        return this;
    }

    public FRGlideImageView load(String url, RequestOptions options) {
        getImageLoader().load(url, options);
        return this;
    }

    public FRGlideImageView loadImage(String url, int placeholderResId) {
        getImageLoader().loadImage(url, placeholderResId);
        return this;
    }

    public FRGlideImageView loadImage(String url, int placeholderResId, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        getImageLoader().loadImage(url, placeholderResId, transitionOptions);
        return this;
    }

    public FRGlideImageView loadImage(Object obj, int placeholderResId, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        getImageLoader().loadImage(obj, placeholderResId, transitionOptions);
        return this;
    }

    public FRGlideImageView loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        getImageLoader().loadLocalImage(resId, placeholderResId);
        return this;
    }

    public FRGlideImageView loadLocalImage(String localPath, int placeholderResId) {
        getImageLoader().loadLocalImage(localPath, placeholderResId);
        return this;
    }

    public FRGlideImageView loadCircleImage(String url, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadCircleImage(url, placeholderResId);
        return this;
    }

    public FRGlideImageView loadLocalCircleImage(int resId, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(resId, placeholderResId);
        return this;
    }

    public FRGlideImageView loadLocalCircleImage(String localPath, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(localPath, placeholderResId);
        return this;
    }

    public FRGlideImageView listener(OnGlideImageViewListener listener) {
        getImageLoader().setOnGlideImageViewListener(getImageUrl(), listener);
        return this;
    }

    public FRGlideImageView listener(OnProgressListener listener) {
        getImageLoader().setOnProgressListener(getImageUrl(), listener);
        return this;
    }
}
