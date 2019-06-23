package com.wkz.bannerlayout.utils;

import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;

import com.wkz.bannerlayout.listener.FRViewPagerCurrent;

public class FRBannerHandlerUtils extends Handler {

    public static final int MSG_UPDATE = 1;
    public static final int MSG_KEEP = 2;
    public static final int MSG_PAGE = 3;
    private int status;
    private long delayTime;
    private final FRViewPagerCurrent mCurrent;
    private int page;

    public final int getStatus() {
        return this.status;
    }

    private void setStatus(int status) {
        this.status = status;
    }

    public void setDelayTime(long time) {
        this.delayTime = time;
    }

    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (this.page != -1) {
            if (this.hasMessages(MSG_UPDATE)) {
                this.removeMessages(MSG_UPDATE);
            }

            int what = msg.what;
            switch (what) {
                case MSG_UPDATE:
                    ++this.page;
                    this.mCurrent.setCurrentItem(this.page);
                    this.sendEmptyMessageDelayed(MSG_UPDATE, this.delayTime);
                case MSG_KEEP:
                default:
                    break;
                case MSG_PAGE:
                    this.page = msg.arg1;
            }

            this.status = what;
        }
    }

    public FRBannerHandlerUtils(@NonNull FRViewPagerCurrent mCurrent, int page) {
        super();
        this.mCurrent = mCurrent;
        this.page = page;
    }
}
