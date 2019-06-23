package com.wkz.bannerlayout.listener;

import androidx.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

public interface FRImageDisplayManager {

    @NonNull
    ImageView display(@NonNull ViewGroup container, FRBannerModelCallBack model);

}
