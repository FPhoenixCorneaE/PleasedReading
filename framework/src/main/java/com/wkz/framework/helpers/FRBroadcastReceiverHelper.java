package com.wkz.framework.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wkz.framework.annotations.FRReceiverType;
import com.wkz.framework.listeners.OnFRHomeKeyListener;
import com.wkz.framework.receivers.FRHomeKeyBroadcastReceiver;

import java.util.HashMap;

public class FRBroadcastReceiverHelper {

    private static volatile FRBroadcastReceiverHelper instance;

    private HashMap<String, BroadcastReceiver> mReceiverMap;

    private FRBroadcastReceiverHelper() {
        mReceiverMap = new HashMap<>();
    }

    public static FRBroadcastReceiverHelper getInstance() {
        if (instance == null) {
            synchronized (FRBroadcastReceiverHelper.class) {
                if (instance == null) {
                    instance = new FRBroadcastReceiverHelper();
                }
            }
        }
        return instance;
    }

    /**
     * Home键监听广播注册
     */
    public void registerHomeKeyReceiver(Context context, OnFRHomeKeyListener onHomeKeyListener) {
        if (context != null && onHomeKeyListener != null) {
            mReceiverMap.put(FRReceiverType.HOME, new FRHomeKeyBroadcastReceiver(onHomeKeyListener));
            context.registerReceiver(mReceiverMap.get(FRReceiverType.HOME), new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }

    /**
     * Home键监听广播反注册
     */
    public void unRegisterHomeKeyReceiver(Context context) {
        if (context != null && mReceiverMap.get(FRReceiverType.HOME) != null) {
            context.unregisterReceiver(mReceiverMap.get(FRReceiverType.HOME));
        }
    }
}
