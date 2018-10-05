package com.wkz.framework.manager;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import com.wkz.framework.PRApplication;
import com.wkz.framework.listener.OnNetworkChangedListener;
import com.wkz.framework.receiver.OnNetworkCallback;
import com.wkz.framework.receiver.OnNetworkChangedReceiver;
import com.wkz.framework.utils.NetworkUtils;
import com.wkz.framework.utils.ResourceUtils;

public class NetworkManager {

    private static volatile NetworkManager instance;
    private OnNetworkCallback mOnNetworkCallback;
    private OnNetworkChangedReceiver mOnNetworkChangedReceiver;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册网络状态变化
     *
     * @param activity
     * @param onNetworkChangedListener
     */
    public void registerNetwork(Activity activity, OnNetworkChangedListener onNetworkChangedListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) PRApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                mOnNetworkCallback = new OnNetworkCallback(connectivityManager, onNetworkChangedListener);
                NetworkRequest request = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .build();
                connectivityManager.registerNetworkCallback(request, mOnNetworkCallback);
            }
        } else {
            if (activity != null) {
                if (NetworkUtils.isNetworkAvailable()) {
                    if (NetworkUtils.isWifiConnected()) {
                        if (onNetworkChangedListener != null) {
                            onNetworkChangedListener.onWifiActive(
                                    ResourceUtils.getString(
                                            ResourceUtils.getStringId("fr_status_net_wifi_active")
                                    )
                            );
                        }
                    } else if (NetworkUtils.isMobileConnected()) {
                        if (onNetworkChangedListener != null) {
                            onNetworkChangedListener.onMobileActive(
                                    ResourceUtils.getString(
                                            ResourceUtils.getStringId("fr_status_net_mobile_active")
                                    )
                            );
                        }
                    }
                } else {
                    if (onNetworkChangedListener != null) {
                        onNetworkChangedListener.onUnavailable(
                                ResourceUtils.getString(
                                        ResourceUtils.getStringId("fr_status_net_unavailable")
                                )
                        );
                    }
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络连接过滤器
                mOnNetworkChangedReceiver = new OnNetworkChangedReceiver(onNetworkChangedListener);
                activity.registerReceiver(mOnNetworkChangedReceiver, intentFilter);
            }
        }
    }

    /**
     * 反注册网络状态变化
     *
     * @param activity
     */
    public void unregisterNetwork(Activity activity) {
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ConnectivityManager connectivityManager = (ConnectivityManager) PRApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    connectivityManager.unregisterNetworkCallback(mOnNetworkCallback);
                }
            } else {
                if (mOnNetworkChangedReceiver != null) {
                    activity.unregisterReceiver(mOnNetworkChangedReceiver);
                }
            }
        }
    }
}
