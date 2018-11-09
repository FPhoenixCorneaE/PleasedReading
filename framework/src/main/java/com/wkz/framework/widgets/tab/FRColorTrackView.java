package com.wkz.framework.widgets.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wkz.framework.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 仿今日头条之字体颜色随ViewPager的滑动步伐而改变
 */
public class FRColorTrackView extends View {
    private int mTextStartX;
    private int mTextStartY;

    @IntDef({
            Direction.LEFT,
            Direction.RIGHT,
            Direction.TOP,
            Direction.BOTTOM
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
        int LEFT = 0;
        int RIGHT = 1;
        int TOP = 2;
        int BOTTOM = 3;
    }

    @Direction
    private int mDirection = Direction.LEFT;

    private String mText = "xx";
    private Paint mPaint;
    private int mTextSize = sp2px(30);//字体大小

    private int mTextOriginColor = 0xff333000;//字体改变前的颜色
    private int mTextChangeColor = 0xffd84040;//字体改变中的颜色

    private Rect mTextBound = new Rect();
    private int mTextWidth;
    private int mTextHeight;

    private float mProgress;// 进度，从0到1之间取值

    public FRColorTrackView(Context context) {
        super(context, null);
    }

    public FRColorTrackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray ta = context.obtainStyledAttributes(
                attrs,
                R.styleable.FRColorTrackView
        );
        mText = ta.getString(
                R.styleable.FRColorTrackView_fr_ctv_text
        );
        mTextSize = ta.getDimensionPixelSize(
                R.styleable.FRColorTrackView_fr_ctv_text_size, mTextSize
        );
        mTextOriginColor = ta.getColor(
                R.styleable.FRColorTrackView_fr_ctv_text_origin_color, mTextOriginColor
        );
        mTextChangeColor = ta.getColor(
                R.styleable.FRColorTrackView_fr_ctv_text_change_color, mTextChangeColor
        );
        mProgress = ta.getFloat(
                R.styleable.FRColorTrackView_fr_ctv_progress, 0
        );
        mDirection = ta.getInt(
                R.styleable.FRColorTrackView_fr_ctv_direction, mDirection
        );

        ta.recycle();

        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureText();

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mTextStartX = getMeasuredWidth() / 2 - mTextWidth / 2;
        mTextStartY = getMeasuredHeight() / 2 - mTextHeight / 2;
    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.height();
                result += getPaddingTop() + getPaddingBottom();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextWidth;
                result += getPaddingLeft() + getPaddingRight();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.top);
        //这个函数可以获取最小的包裹文字矩形，赋值到mTextBound
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = mTextBound.height();
    }

    public void reverseColor() {
        int tmp = mTextOriginColor;
        mTextOriginColor = mTextChangeColor;
        mTextChangeColor = tmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int r = (int) (mProgress * mTextWidth + mTextStartX);
        int t = (int) (mProgress * mTextHeight + mTextStartY);

        if (mDirection == Direction.LEFT) {
            drawChangeLeft(canvas, r);
            drawOriginLeft(canvas, r);
        } else if (mDirection == Direction.RIGHT) {
            drawOriginRight(canvas, r);
            drawChangeRight(canvas, r);
        } else if (mDirection == Direction.TOP) {
            drawOriginTop(canvas, t);
            drawChangeTop(canvas, t);
        } else {
            drawOriginBottom(canvas, t);
            drawChangeBottom(canvas, t);
        }

    }

    private boolean debug = false;

    private void drawText_h(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        if (debug) {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(startX, 0, endX, getMeasuredHeight(), mPaint);
        }
        canvas.save();
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());// left, top,
        // right, bottom
        canvas.drawText(mText, mTextStartX,
                getMeasuredHeight() / 2
                        - ((mPaint.descent() + mPaint.ascent()) / 2), mPaint);
        canvas.restore();
    }

    private void drawText_v(Canvas canvas, int color, int startY, int endY) {
        mPaint.setColor(color);
        if (debug) {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, startY, getMeasuredWidth(), endY, mPaint);
        }

        canvas.save();
        canvas.clipRect(0, startY, getMeasuredWidth(), endY);// left, top,
        canvas.drawText(mText, mTextStartX,
                getMeasuredHeight() / 2
                        - ((mPaint.descent() + mPaint.ascent()) / 2), mPaint);
        canvas.restore();
    }

    private void drawChangeLeft(Canvas canvas, int r) {
        drawText_h(canvas, mTextChangeColor, mTextStartX,
                (int) (mTextStartX + mProgress * mTextWidth));
    }

    private void drawOriginLeft(Canvas canvas, int r) {
        drawText_h(canvas, mTextOriginColor, (int) (mTextStartX + mProgress
                * mTextWidth), mTextStartX + mTextWidth);
    }

    private void drawChangeRight(Canvas canvas, int r) {
        drawText_h(canvas, mTextChangeColor,
                (int) (mTextStartX + (1 - mProgress) * mTextWidth), mTextStartX
                        + mTextWidth);
    }

    private void drawOriginRight(Canvas canvas, int r) {
        drawText_h(canvas, mTextOriginColor, mTextStartX,
                (int) (mTextStartX + (1 - mProgress) * mTextWidth));
    }

    private void drawChangeTop(Canvas canvas, int r) {
        drawText_v(canvas, mTextChangeColor, mTextStartY,
                (int) (mTextStartY + mProgress * mTextHeight));
    }

    private void drawOriginTop(Canvas canvas, int r) {
        drawText_v(canvas, mTextOriginColor, (int) (mTextStartY + mProgress
                * mTextHeight), mTextStartY + mTextHeight);
    }

    private void drawChangeBottom(Canvas canvas, int t) {
        drawText_v(canvas, mTextChangeColor,
                (int) (mTextStartY + (1 - mProgress) * mTextHeight),
                mTextStartY + mTextHeight);
    }

    private void drawOriginBottom(Canvas canvas, int t) {
        drawText_v(canvas, mTextOriginColor, mTextStartY,
                (int) (mTextStartY + (1 - mProgress) * mTextHeight));
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(@Direction int direction) {
        mDirection = direction;
        invalidate(); //重新改变方向，需要重绘
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();//改变进度，需要重绘，实现字体渐变的效果
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        mPaint.setTextSize(mTextSize);
        requestLayout();//改变字体大小，重新布局
        invalidate();//改变字体大小，需要重绘
    }

    public void setText(String text) {
        this.mText = text;
        requestLayout();
        invalidate();
    }

    public int getTextOriginColor() {
        return mTextOriginColor;
    }

    public void setTextOriginColor(int mTextOriginColor) {
        this.mTextOriginColor = mTextOriginColor;
        invalidate();//改变原始颜色，需要重绘
    }

    public int getTextChangeColor() {
        return mTextChangeColor;
    }

    public void setTextChangeColor(int mTextChangeColor) {
        this.mTextChangeColor = mTextChangeColor;
        invalidate();//改变变化中的颜色，需要重绘
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }

    private static final String KEY_STATE_PROGRESS = "key_progress";
    private static final String KEY_DEFAULT_STATE = "key_default_state";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat(KEY_STATE_PROGRESS, mProgress);
        bundle.putParcelable(KEY_DEFAULT_STATE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getFloat(KEY_STATE_PROGRESS);
            super.onRestoreInstanceState(bundle
                    .getParcelable(KEY_DEFAULT_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
