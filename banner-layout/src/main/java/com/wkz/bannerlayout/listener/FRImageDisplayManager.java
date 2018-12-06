package com.wkz.bannerlayout.listener;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

public interface FRImageDisplayManager {

    @NonNull
    ImageView display(@NonNull ViewGroup container, FRBannerModelCallBack model);

}
