package com.wkz.framework.widgets.glideimageview;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.wkz.framework.widgets.glideimageview.progress.FRProgressManager;
import com.wkz.framework.widgets.glideimageview.progress.OnGlideImageViewListener;
import com.wkz.framework.widgets.glideimageview.progress.OnProgressListener;
import com.wkz.framework.widgets.glideimageview.transformation.FRGlideCircleTransformation;

import java.lang.ref.WeakReference;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class FRGlideImageLoader {

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FILE = "file://";
    private static final String SEPARATOR = "/";
    private static final String HTTP = "http";

    private WeakReference<ImageView> mImageView;
    private Object mImageUrlObj;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private boolean mLastStatus = false;
    private Handler mMainThreadHandler;

    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;

    public static FRGlideImageLoader create(ImageView imageView) {
        return new FRGlideImageLoader(imageView);
    }

    private FRGlideImageLoader(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public ImageView getImageView() {
        if (mImageView != null) {
            return mImageView.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public String getImageUrl() {
        if (mImageUrlObj == null) {
            return null;
        }
        if (!(mImageUrlObj instanceof String)) {
            return null;
        }
        return (String) mImageUrlObj;
    }

    public Uri resId2Uri(int resourceId) {
        if (getContext() == null) {
            return null;
        }
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resourceId);
    }

    public void load(int resId, RequestOptions options) {
        load(resId2Uri(resId), options);
    }

    public void load(Uri uri, RequestOptions options) {
        if (uri == null || getContext() == null) {
            return;
        }
        requestBuilder(uri, options).into(getImageView());
    }

    public void load(String url, RequestOptions options) {
        if (url == null || getContext() == null) {
            return;
        }
        requestBuilder(url, options).into(getImageView());
    }

    public void load(String url, RequestOptions options, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        if (url == null || getContext() == null) {
            return;
        }
        load((Object) url, options, transitionOptions);
    }

    public void load(Object obj, RequestOptions options, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        if (transitionOptions == null) {
            //默认加载动画
            ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
                @Override
                public void animate(View view) {
                    PropertyValuesHolder scaleXAnim = PropertyValuesHolder.ofFloat("scaleX", 1.382f, 1f);
                    PropertyValuesHolder scaleYAnim = PropertyValuesHolder.ofFloat("scaleY", 1.382f, 1f);

                    android.animation.ObjectAnimator.ofPropertyValuesHolder(view, scaleXAnim, scaleYAnim)
                            .setDuration(400)
                            .start();
                }
            };
            transitionOptions = GenericTransitionOptions.with(animationObject);
        }
        requestBuilder(obj, options).transition(transitionOptions).into(getImageView());
    }

    public RequestBuilder<Drawable> requestBuilder(Object obj, RequestOptions options) {
        this.mImageUrlObj = obj;
        return Glide.with(getContext())
                .load(obj)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(mLastBytesRead, mTotalBytes, true, e);
                        FRProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(mLastBytesRead, mTotalBytes, true, null);
                        FRProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                });
    }

    public RequestOptions requestOptions(int placeholderResId) {
        return requestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .centerCrop()
                .placeholder(placeholderResId)
                .error(errorResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return circleRequestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new FRGlideCircleTransformation());
    }

    public void loadImage(String url, int placeholderResId) {
        load(url, requestOptions(placeholderResId));
    }

    public void loadImage(String url, int placeholderResId, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        load(url, requestOptions(placeholderResId), transitionOptions);
    }

    public void loadImage(Object obj, int placeholderResId, @Nullable TransitionOptions<?, ? super Drawable> transitionOptions) {
        load(obj, requestOptions(placeholderResId), transitionOptions);
    }

    public void loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        load(resId, requestOptions(placeholderResId));
    }

    public void loadLocalImage(String localPath, int placeholderResId) {
        load(FILE + localPath, requestOptions(placeholderResId));
    }

    public void loadCircleImage(String url, int placeholderResId) {
        load(url, circleRequestOptions(placeholderResId));
    }

    public void loadLocalCircleImage(int resId, int placeholderResId) {
        load(resId, circleRequestOptions(placeholderResId));
    }

    public void loadLocalCircleImage(String localPath, int placeholderResId) {
        load(FILE + localPath, circleRequestOptions(placeholderResId));
    }

    private void addProgressListener() {
        if (getImageUrl() == null) {
            return;
        }
        final String url = getImageUrl();
        if (!url.startsWith(HTTP)) {
            return;
        }

        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
                if (totalBytes == 0) {
                    return;
                }
                if (!url.equals(imageUrl)) {
                    return;
                }
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) {
                    return;
                }

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    FRProgressManager.removeProgressListener(this);
                }
            }
        };
        FRProgressManager.addProgressListener(internalProgressListener);
    }

    private void mainThreadCallback(final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress((String) mImageUrlObj, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
            }
        });
    }

    public void setOnGlideImageViewListener(String imageUrl, OnGlideImageViewListener onGlideImageViewListener) {
        this.mImageUrlObj = imageUrl;
        this.onGlideImageViewListener = onGlideImageViewListener;
        addProgressListener();
    }

    public void setOnProgressListener(String imageUrl, OnProgressListener onProgressListener) {
        this.mImageUrlObj = imageUrl;
        this.onProgressListener = onProgressListener;
        addProgressListener();
    }
}
