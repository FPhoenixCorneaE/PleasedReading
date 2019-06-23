package com.wkz.bannerlayout.imagemanager;

import androidx.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wkz.bannerlayout.listener.FRBannerModelCallBack;
import com.wkz.bannerlayout.listener.FRImageDisplayManager;
import com.wkz.bannerlayout.widget.FRBannerDefaults;

public class FRGlideImageManager implements FRImageDisplayManager {

    private RequestOptions requestOptions;

    @NonNull
    public ImageView display(@NonNull ViewGroup container, @NonNull FRBannerModelCallBack model) {
        ImageView imageView = new ImageView(container.getContext());
        Glide.with(imageView.getContext())
                .applyDefaultRequestOptions(
                        this.requestOptions
                                .placeholder(FRBannerDefaults.GLIDE_PLACEHOLDER_DRAWABLE)
                                .error(FRBannerDefaults.GLIDE_ERROR_DRAWABLE)
                                .fallback(FRBannerDefaults.GLIDE_ERROR_DRAWABLE)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .load(model.getBannerUrl())
                .into(imageView);
        return imageView;
    }

    public FRGlideImageManager() {
        this.requestOptions = (new RequestOptions()).centerCrop();
    }
}
