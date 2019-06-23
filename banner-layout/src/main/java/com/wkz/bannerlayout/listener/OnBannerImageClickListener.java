package com.wkz.bannerlayout.listener;

import androidx.annotation.NonNull;
import android.view.View;

public interface OnBannerImageClickListener {

    void onBannerClick(@NonNull View view, int position, FRBannerModelCallBack model);

}
