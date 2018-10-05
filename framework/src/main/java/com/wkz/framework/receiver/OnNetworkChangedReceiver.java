package com.wkz.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.wkz.framework.listener.OnNetworkChangedListener;
import com.wkz.framework.utils.ResourceUtils;

public class OnNetworkChangedReceiver extends BroadcastReceiver {

    private OnNetworkChangedListener mOnNetworkChangedListener;

    public OnNetworkChangedReceiver(OnNetworkChangedListener onNetworkChangedListener) {
        this.mOnNetworkChangedListener = onNetworkChangedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TextUtils.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) return;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    if (mOnNetworkChangedListener != null) {
                        mOnNetworkChangedListener.onWifiActive(
                                ResourceUtils.getString(
                                        ResourceUtils.getStringId("fr_status_net_wifi_active")
                                )
                        );
                    }
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    if (mOnNetworkChangedListener != null) {
                        mOnNetworkChangedListener.onMobileActive(
                                ResourceUtils.getString(
                                        ResourceUtils.getStringId("fr_status_net_mobile_active")
                                )
                        );
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (mOnNetworkChangedListener != null) {
                mOnNetworkChangedListener.onUnavailable(
                        ResourceUtils.getString(
                                ResourceUtils.getStringId("fr_status_net_unavailable")
                        )
                );
            }
        }
    }
}
