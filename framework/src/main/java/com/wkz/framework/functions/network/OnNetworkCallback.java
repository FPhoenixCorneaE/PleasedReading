package com.wkz.framework.functions.network;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.wkz.framework.R;
import com.wkz.utils.ResourceUtils;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OnNetworkCallback extends ConnectivityManager.NetworkCallback {

    private OnNetworkChangedListener mOnNetworkChangedListener;
    private ConnectivityManager mConnectivityManager;

    public OnNetworkCallback(ConnectivityManager connectivityManager, OnNetworkChangedListener onNetworkChangedListener) {
        this.mConnectivityManager = connectivityManager;
        this.mOnNetworkChangedListener = onNetworkChangedListener;
    }

    /**
     * 网络可用的回调
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Logger.i("Network", "onAvailable");
        if (mConnectivityManager != null) {
            NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.isAvailable()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        if (mOnNetworkChangedListener != null) {
                            mOnNetworkChangedListener.onWifiActive(
                                    ResourceUtils.getString(R.string.fr_status_net_wifi_active)
                            );
                        }
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        if (mOnNetworkChangedListener != null) {
                            mOnNetworkChangedListener.onMobileActive(
                                    ResourceUtils.getString(R.string.fr_status_net_mobile_active)
                            );
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 网络丢失的回调
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Logger.i("Network", "onLost");

        if (mOnNetworkChangedListener != null) {
            mOnNetworkChangedListener.onUnavailable(
                    ResourceUtils.getString(R.string.fr_status_net_unavailable)
            );
        }
    }

    /**
     * 当建立网络连接时，回调连接的属性
     */
    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        Logger.i("Network", "onLinkPropertiesChanged");
    }

    /**
     * 按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
     */
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Logger.i("Network", "onCapabilitiesChanged");
    }

    /**
     * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
     */
    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Logger.i("Network", "onLosing");
    }

    /**
     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
     */
    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Logger.i("Network", "onUnavailable");
    }
}
