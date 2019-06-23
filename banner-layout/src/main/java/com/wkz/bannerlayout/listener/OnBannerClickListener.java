package com.wkz.bannerlayout.listener;

import androidx.annotation.NonNull;
import android.view.View;

/**
 * Banner Click event, object for the return of data
 */
public interface OnBannerClickListener {

    void onBannerClick(@NonNull View view, int position, FRBannerModelCallBack model);

}
