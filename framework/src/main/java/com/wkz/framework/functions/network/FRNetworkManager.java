package com.wkz.framework.functions.network;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import com.orhanobut.logger.Logger;
import com.wkz.framework.R;
import com.wkz.utils.ContextUtils;
import com.wkz.utils.NetworkUtils;
import com.wkz.utils.ResourceUtils;

/**
 * @author wkz
 */
public class FRNetworkManager {

    private static volatile FRNetworkManager instance;
    private OnNetworkCallback mOnNetworkCallback;
    private OnNetworkChangedReceiver mOnNetworkChangedReceiver;
    /**
     * 为了避免BroadcastReceiver多次unregisterReceiver 导致 Receiver not registered问题，增加是否已注册广播接收器标识
     */
    private boolean mReceiverIsRegistered;

    private FRNetworkManager() {
    }

    public static FRNetworkManager getInstance() {
        if (instance == null) {
            synchronized (FRNetworkManager.class) {
                if (instance == null) {
                    instance = new FRNetworkManager();
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
            ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                mOnNetworkCallback = new OnNetworkCallback(connectivityManager, onNetworkChangedListener);
                NetworkRequest request = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .build();
                // 在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                if (!mReceiverIsRegistered) {
                    mReceiverIsRegistered = true;
                    connectivityManager.registerNetworkCallback(request, mOnNetworkCallback);
                }
            }
        } else {
            if (activity != null) {
                if (NetworkUtils.isConnected()) {
                    if (NetworkUtils.isWifiConnected()) {
                        if (onNetworkChangedListener != null) {
                            onNetworkChangedListener.onWifiActive(
                                    ResourceUtils.getString(R.string.fr_status_net_wifi_active)
                            );
                        }
                    } else if (NetworkUtils.isMobileConnected()) {
                        if (onNetworkChangedListener != null) {
                            onNetworkChangedListener.onMobileActive(
                                    ResourceUtils.getString(R.string.fr_status_net_mobile_active)
                            );
                        }
                    }
                } else {
                    if (onNetworkChangedListener != null) {
                        onNetworkChangedListener.onUnavailable(
                                ResourceUtils.getString(R.string.fr_status_net_unavailable)
                        );
                    }
                }
                mOnNetworkChangedReceiver = new OnNetworkChangedReceiver(onNetworkChangedListener);
                // 在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                if (!mReceiverIsRegistered) {
                    mReceiverIsRegistered = true;
                    Logger.i(activity.getLocalClassName() + "网络广播接收器已注册");
                    IntentFilter intentFilter = new IntentFilter();
                    // 网络连接过滤器
                    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                    activity.registerReceiver(mOnNetworkChangedReceiver, intentFilter);
                }
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
                ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null && mReceiverIsRegistered) {
                    //判断广播是否注册
                    mReceiverIsRegistered = false;
                    connectivityManager.unregisterNetworkCallback(mOnNetworkCallback);
                }
            } else {
                if (mOnNetworkChangedReceiver != null && mReceiverIsRegistered) {
                    //判断广播是否注册
                    try {
                        mReceiverIsRegistered = false;
                        Logger.i(activity.getLocalClassName() + "网络广播接收器已反注册");
                        activity.unregisterReceiver(mOnNetworkChangedReceiver);
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                }
            }
        }
    }
}
