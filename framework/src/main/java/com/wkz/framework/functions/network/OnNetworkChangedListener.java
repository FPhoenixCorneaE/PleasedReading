package com.wkz.framework.functions.network;

public interface OnNetworkChangedListener {
    void onWifiActive(String message);

    void onMobileActive(String message);

    void onUnavailable(String message);
}
