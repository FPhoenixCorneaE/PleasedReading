package com.wkz.bannerlayout.imagemanager;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wkz.bannerlayout.listener.FRBannerModelCallBack;
import com.wkz.bannerlayout.listener.FRImageDisplayManager;

public class FRImageLoaderManager implements FRImageDisplayManager {
    @NonNull
    public ImageView display(@NonNull ViewGroup container, @NonNull FRBannerModelCallBack model) {
        ImageView imageView = new ImageView(container.getContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage((String) model.getBannerUrl(), imageView);
        return imageView;
    }
}
