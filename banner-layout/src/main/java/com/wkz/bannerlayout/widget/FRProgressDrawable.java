package com.wkz.bannerlayout.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

import com.wkz.bannerlayout.annotation.FRProgressShapeMode;

public class FRProgressDrawable extends Drawable implements Animatable {

    private float mWidth;
    private float mHeight;
    private long mDuration;
    @ColorInt
    private int mBackgroundColor;
    @ColorInt
    private int mProgressColor;
    private float mRadius;
    @FRProgressShapeMode
    private int mShapeMode;
    private float mRingThickness;
    @ColorInt
    private int mInnerCircleColor;

    private Paint mPaint;
    private ValueAnimator mAnimator;
    private Animator.AnimatorListener mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private Context mContext;
    private boolean mIsAnimated;
    private float mProgressWidth;
    private float mProgress;

    private FRProgressDrawable(Builder builder) {
        this.mContext = builder.mContext;
        this.mAnimatorListener = builder.mAnimatorListener;
        this.mIsAnimated = builder.mIsAnimated;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mDuration = builder.mDuration;
        this.mBackgroundColor = builder.mBackgroundColor;
        this.mProgressColor = builder.mProgressColor;
        this.mRadius = builder.mRadius;
        this.mShapeMode = builder.mShapeMode;
        this.mRingThickness = builder.mRingThickness;
        this.mInnerCircleColor = builder.mInnerCircleColor;

        init();
    }

    private void init() {
        // 设置画笔参数
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        //是否动画
        if (mIsAnimated) {
            // 设置属性动画参数
            mAnimator = new ValueAnimator();
            mAnimator = ValueAnimator.ofFloat(0f, 1f);
            mAnimator.setRepeatCount(0);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setDuration(mDuration);
            // 设置动画的回调
            mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mProgress = (float) animation.getAnimatedValue();
                    calculateProgressWidth();
                    invalidateSelf();
                }
            };
        }
    }

    @Override
    public void start() {
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.removeAllUpdateListeners();
            if (mAnimatorListener != null) {
                mAnimator.addListener(mAnimatorListener);
            }
            if (mAnimatorUpdateListener != null) {
                mAnimator.addUpdateListener(mAnimatorUpdateListener);
            }
            if (mAnimator.isPaused()) {
                mAnimator.resume();
            } else {
                mAnimator.start();
            }
        }
    }

    @Override
    public void stop() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.pause();
        }
    }

    public void end() {
        if (mAnimator != null) {
            mAnimator.end();
            mProgress = 0;
            mProgressWidth = 0;
            invalidateSelf();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimator != null && mAnimator.isRunning();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (FRProgressShapeMode.RECTANGLE == mShapeMode) {
            // 绘制背景
            mPaint.setColor(mBackgroundColor);
            canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight),
                    mRadius, mRadius, mPaint);

            // 绘制进度
            if (mIsAnimated) {
                mPaint.setColor(mProgressColor);
                canvas.drawRoundRect(new RectF(0, 0, mProgressWidth, mHeight),
                        mRadius, mRadius, mPaint);
            }
        } else if (FRProgressShapeMode.RING == mShapeMode) {
            // 绘制背景
            mPaint.setColor(mBackgroundColor);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

            // 绘制进度
            mPaint.setColor(mProgressColor);
            canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2),
                    -90, 360 * mProgress, true, mPaint);

            // 绘制空心圆
            mPaint.setColor(mInnerCircleColor);
            canvas.drawCircle(mRadius, mRadius, mRadius - mRingThickness, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 获取默认的高度
     */
    @Override
    public int getIntrinsicHeight() {
        return (int) this.mHeight;
    }

    /**
     * 获取默认的宽度
     */
    @Override
    public int getIntrinsicWidth() {
        return (int) this.mWidth;
    }

    private void calculateProgressWidth() {
        this.mProgressWidth = mWidth * mProgress;
    }

    public static class Builder {

        private static final boolean DEFAULT_IS_ANIMATED = true;
        private static final float DEFAULT_WIDTH = 50.0F;
        private static final float DEFAULT_HEIGHT = 2.5F;
        private static final long DEFAULT_DURATION = 3000;
        private static final int DEFAULT_BACKGROUND_COLOR = Color.RED;
        private static final int DEFAULT_PROGRESS_COLOR = Color.WHITE;
        private static final float DEFAULT_RADIUS = 10F;
        private static final int DEFAULT_SHAPE_MODE = FRProgressShapeMode.RECTANGLE;
        private static final float DEFAULT_RING_THICKNESS = 2.5F;
        private static final int DEFAULT_INNER_CIRCLE_COLOR = Color.LTGRAY;

        private Context mContext;
        private Animator.AnimatorListener mAnimatorListener;
        private boolean mIsAnimated;
        private float mWidth;
        private float mHeight;
        private long mDuration;
        @ColorInt
        private int mBackgroundColor;
        @ColorInt
        private int mProgressColor;
        private float mRadius;
        @FRProgressShapeMode
        private int mShapeMode;
        private float mRingThickness;
        @ColorInt
        private int mInnerCircleColor;

        public Builder(Context mContext) {
            this.mContext = mContext;
            this.mIsAnimated = DEFAULT_IS_ANIMATED;
            this.mWidth = dp2px(DEFAULT_WIDTH);
            this.mHeight = dp2px(DEFAULT_HEIGHT);
            this.mDuration = DEFAULT_DURATION;
            this.mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            this.mProgressColor = DEFAULT_PROGRESS_COLOR;
            this.mRadius = dp2px(DEFAULT_RADIUS);
            this.mShapeMode = DEFAULT_SHAPE_MODE;
            this.mRingThickness = dp2px(DEFAULT_RING_THICKNESS);
            this.mInnerCircleColor = DEFAULT_INNER_CIRCLE_COLOR;
        }

        public boolean isAnimated() {
            return mIsAnimated;
        }

        public Builder setAnimated(boolean mIsAnimated) {
            this.mIsAnimated = mIsAnimated;
            return this;
        }

        public Animator.AnimatorListener getAnimatorListener() {
            return mAnimatorListener;
        }

        public Builder setAnimatorListener(Animator.AnimatorListener mAnimatorListener) {
            this.mAnimatorListener = mAnimatorListener;
            return this;
        }

        public float getWidth() {
            return mWidth;
        }

        public Builder setWidth(float dpValue) {
            this.mWidth = dp2px(dpValue);
            return this;
        }

        public float getHeight() {
            return mHeight;
        }

        public Builder setHeight(float dpValue) {
            this.mHeight = dp2px(dpValue);
            return this;
        }

        public long getDuration() {
            return mDuration;
        }

        public Builder setDuration(long mDuration) {
            this.mDuration = mDuration;
            return this;
        }

        public int getBackgroundColor() {
            return mBackgroundColor;
        }

        public Builder setBackgroundColor(@ColorInt int mBackgroundColor) {
            this.mBackgroundColor = mBackgroundColor;
            return this;
        }

        public int getProgressColor() {
            return mProgressColor;
        }

        public Builder setProgressColor(@ColorInt int mProgressColor) {
            this.mProgressColor = mProgressColor;
            return this;
        }

        public float getRadius() {
            return mRadius;
        }

        public Builder setRadius(float dpValue) {
            this.mRadius = dp2px(dpValue);
            return this;
        }

        public int getShapeMode() {
            return mShapeMode;
        }

        public Builder setShapeMode(@FRProgressShapeMode int mShapeMode) {
            this.mShapeMode = mShapeMode;
            return this;
        }

        public float getRingThickness() {
            return mRingThickness;
        }

        public Builder setRingThickness(float dpValue) {
            this.mRingThickness = dp2px(dpValue);
            return this;
        }

        public int getInnerCircleColor() {
            return mInnerCircleColor;
        }

        public Builder setInnerCircleColor(int mInnerCircleColor) {
            this.mInnerCircleColor = mInnerCircleColor;
            return this;
        }

        private float dp2px(float dpValue) {
            float scale = mContext.getResources().getDisplayMetrics().density;
            return dpValue * scale;
        }

        public FRProgressDrawable build() {
            return new FRProgressDrawable(this);
        }
    }
}
