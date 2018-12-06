package com.wkz.bannerlayout.listener;

import android.support.annotation.NonNull;
import android.view.View;

public interface OnBannerImageClickListener {

    void onBannerClick(@NonNull View view, int position, FRBannerModelCallBack model);

}
