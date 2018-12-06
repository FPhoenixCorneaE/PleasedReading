package com.wkz.bannerlayout.imagemanager;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wkz.bannerlayout.listener.FRBannerModelCallBack;
import com.wkz.bannerlayout.listener.FRImageDisplayManager;

public class FRFrescoImageManager implements FRImageDisplayManager {
    @NonNull
    public ImageView display(@NonNull ViewGroup container, @NonNull FRBannerModelCallBack model) {
        SimpleDraweeView draweeView = new SimpleDraweeView(container.getContext());
        draweeView.setImageURI((String) model.getBannerUrl());
        return draweeView;
    }
}
