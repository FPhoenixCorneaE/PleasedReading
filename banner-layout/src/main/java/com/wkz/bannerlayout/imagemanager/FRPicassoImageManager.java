package com.wkz.bannerlayout.imagemanager;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wkz.bannerlayout.listener.FRBannerModelCallBack;
import com.wkz.bannerlayout.listener.FRImageDisplayManager;
import com.wkz.bannerlayout.widget.FRBannerDefaults;

public class FRPicassoImageManager implements FRImageDisplayManager {
    @NonNull
    public ImageView display(@NonNull ViewGroup container, @NonNull FRBannerModelCallBack model) {
        ImageView imageView = new ImageView(container.getContext());
        Picasso.get()
                .load((String) model.getBannerUrl())
                .placeholder(FRBannerDefaults.GLIDE_PLACEHOLDER_DRAWABLE)
                .error(FRBannerDefaults.GLIDE_ERROR_DRAWABLE)
                .into(imageView);
        return imageView;
    }
}
