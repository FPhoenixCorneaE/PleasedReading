package com.wkz.framework.widgets;

import android.os.CountDownTimer;

/**
 * 倒计时器
 */
public class FRTimer extends CountDownTimer {

    private OnCountDownListener mOnCountDownListener;

    public FRTimer(long millisInFuture) {
        super(millisInFuture, 1000);
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public FRTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mOnCountDownListener != null) {
            mOnCountDownListener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (mOnCountDownListener != null) {
            mOnCountDownListener.onFinish();
        }
    }

    public interface OnCountDownListener {

        void onTick(long millisUntilFinished);

        void onFinish();

    }

    public FRTimer setOnCountDownListener(OnCountDownListener onCountDownListener) {
        this.mOnCountDownListener = onCountDownListener;
        return this;
    }
}
