package com.wkz.framework.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wkz.framework.listeners.OnFRHomeKeyListener;

public class FRHomeKeyBroadcastReceiver extends BroadcastReceiver {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";

    private OnFRHomeKeyListener mOnFRHomeKeyListener;

    public FRHomeKeyBroadcastReceiver(OnFRHomeKeyListener onFRHomeKeyListener) {
        this.mOnFRHomeKeyListener = onFRHomeKeyListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (null != mOnFRHomeKeyListener) {
                    switch (reason) {
                        case SYSTEM_DIALOG_REASON_HOME_KEY:
                            //按Home按键
                            mOnFRHomeKeyListener.onClickHome();
                            break;
                        case SYSTEM_DIALOG_REASON_RECENT_APPS:
                            //最近任务键也就是菜单键
                            mOnFRHomeKeyListener.onClickRecents();
                            break;
                        case SYSTEM_DIALOG_REASON_ASSIST:
                            //常按home键盘
                            mOnFRHomeKeyListener.onLongClickHome();
                            break;
                        case SYSTEM_DIALOG_REASON_LOCK:
                            //锁屏
                            mOnFRHomeKeyListener.onLockScreen();
                            break;
                    }
                }
            }
        }
    }
}
