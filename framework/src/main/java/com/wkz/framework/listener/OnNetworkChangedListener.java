package com.wkz.framework.listener;

public interface OnNetworkChangedListener {
    void onWifiActive(String message);

    void onMobileActive(String message);

    void onUnavailable(String message);
}
