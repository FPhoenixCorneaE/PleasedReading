package com.wkz.videoplayer.window;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class FRLinearLayout extends LinearLayout {
    public FRLinearLayout(Context context) {
        super(context);
    }

    public FRLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FRLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
