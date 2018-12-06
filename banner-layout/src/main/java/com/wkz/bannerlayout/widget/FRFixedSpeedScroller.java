package com.wkz.bannerlayout.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Scroller;

public class FRFixedSpeedScroller extends Scroller {

    private int fixDuration;

    public int getFixDuration() {
        return this.fixDuration;
    }

    private void setFixDuration(int duration) {
        this.fixDuration = duration;
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.fixDuration);
    }

    public void setDuration(int time) {
        this.fixDuration = time;
    }

    public FRFixedSpeedScroller(@NonNull Context context) {
        super(context);
    }
}
