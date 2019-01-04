package com.wkz.framework.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.wkz.framework.utils.SizeUtils;

/**
 * 自定义的进度条
 */
public class FRProgressBar extends View {

    public static final int STYLE_HORIZONTAL = 0;
    public static final int STYLE_ROUND = 1;
    public static final int STYLE_SECTOR = 2;
    /**
     * 圆形进度条边框宽度
     **/
    private int strokeWidth = SizeUtils.dp2px(2f);
    /**
     * 进度条中心X坐标
     **/
    private int centerX;
    /**
     * 进度条中心Y坐标
     **/
    private int centerY;
    /**
     * 进度提示文字大小
     **/
    private int percentTextSize = 18;
    /**
     * 进度提示文字颜色 蓝色
     **/
    private int percentTextColor = 0xff4b749d;
    /**
     * 进度条背景颜色
     **/
    private int progressBarBgColor = 0xffededed;
    /**
     * 进度颜色 蓝色
     **/
    private int progressColor = 0xff4b749d;
    /**
     * 扇形扫描进度的颜色
     */
    private int sectorColor = 0xaaffffff;
    /**
     * 扇形扫描背景
     */
    private int unSweepColor = 0xaa5e5e5e;
    /**
     * 进度条样式（水平/圆形）
     **/
    private int orientation = STYLE_ROUND;
    /**
     * 圆形进度条半径
     **/
    private int radius = SizeUtils.dp2px(15);
    /**
     * 中心矩形半径
     */
    private int centerRectRadius = SizeUtils.dp2px(5);
    /**
     * 进度最大值
     **/
    private int max = 100;
    /**
     * 进度值
     **/
    private int progress = 0;
    /**
     * 水平进度条是否是空心
     **/
    private boolean isHorizonStroke;
    /**
     * 水平进度圆角值
     **/
    private int rectRound = 0;
    /**
     * 进度文字是否显示百分号
     **/
    private boolean showPercentSign = true;

    /**
     * 进度文字是否显示
     */
    private boolean showText = false;
    /**
     * 中心实心矩形是否显示
     */
    private boolean showCenterRect = true;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    public FRProgressBar(Context context) {
        this(context, null);
    }

    public FRProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FRProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        radius = centerX - strokeWidth / 2;
        if (orientation == STYLE_HORIZONTAL) {
            drawHoriRectProgressBar(canvas, mPaint);
        } else if (orientation == STYLE_ROUND) {
            drawRoundProgressBar(canvas, mPaint);
        } else {
            drawSectorProgressBar(canvas, mPaint);
        }
    }

    /**
     * 绘制圆形进度条
     *
     * @param canvas
     */
    private void drawRoundProgressBar(Canvas canvas, Paint piant) {
        // 初始化画笔属性
        piant.setColor(progressBarBgColor);
        piant.setStyle(Paint.Style.STROKE);
        piant.setStrokeWidth(strokeWidth);
        // 画圆
        canvas.drawCircle(centerX, centerY, radius, piant);
        // 画圆形进度
        piant.setColor(progressColor);
        piant.setStyle(Paint.Style.STROKE);
        piant.setStrokeWidth(strokeWidth);
        RectF oval = new RectF(centerX - radius, centerY - radius, radius + centerX, radius + centerY);
        canvas.drawArc(oval, -90, 360 * progress / max, false, piant);

        if (showCenterRect) {
            //画中心实心矩形
            piant.setColor(progressBarBgColor);
            piant.setStyle(Paint.Style.FILL);
            RectF rectF = new RectF(centerX - centerRectRadius, centerY - centerRectRadius, radius + centerRectRadius, radius + centerRectRadius);
            canvas.drawRect(rectF, piant);
        }

        if (showText) {
            // 画进度文字
            piant.setStyle(Paint.Style.FILL);
            piant.setColor(percentTextColor);
            piant.setTextSize(percentTextSize);

            //进度
            String percent = String.valueOf(progress * 100 / max);
            if (showPercentSign)
                percent = percent + "%";

            Rect rect = new Rect();
            piant.getTextBounds(percent, 0, percent.length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();
            if (textWidth >= radius * 2) {
                textWidth = radius * 2;
            }
            canvas.drawText(percent, centerX - textWidth / 2, centerY + textHeight / 2, piant);
        }
    }

    /**
     * 绘制水平矩形进度条
     *
     * @param canvas
     */
    private void drawHoriRectProgressBar(Canvas canvas, Paint piant) {
        // 初始化画笔属性
        piant.setColor(progressBarBgColor);
        if (isHorizonStroke) {
            piant.setStyle(Paint.Style.STROKE);
            piant.setStrokeWidth(1);
        } else {
            piant.setStyle(Paint.Style.FILL);
        }
        // 画水平矩形
        canvas.drawRoundRect(new RectF(0, 0,
                mWidth, mHeight), rectRound, rectRound, piant);

        // 画水平进度
        piant.setStyle(Paint.Style.FILL);
        piant.setColor(progressColor);
        if (isHorizonStroke) {
            canvas.drawRoundRect(new RectF(0, 0,
                    ((progress * 100 / max) * mWidth) / 100, mHeight), rectRound, rectRound, piant);
        } else {
            piant.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawRoundRect(new RectF(0, 0,
                    ((progress * 100 / max) * mWidth) / 100, mHeight), rectRound, rectRound, piant);
            piant.setXfermode(null);
        }

        if (showText) {
            // 画进度文字
            piant.setStyle(Paint.Style.FILL);
            piant.setColor(percentTextColor);
            piant.setTextSize(percentTextSize);

            //进度
            String percent = String.valueOf(progress * 100 / max);
            if (showPercentSign)
                percent = percent + "%";

            Rect rect = new Rect();
            piant.getTextBounds(percent, 0, percent.length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();
            if (textWidth >= mWidth) {
                textWidth = mWidth;
            }
            canvas.drawText(percent, centerX - textWidth / 2, centerY + textHeight / 2, piant);
        }
    }

    /**
     * 绘制扇形扫描式进度
     *
     * @param canvas
     * @param piant
     */
    private void drawSectorProgressBar(Canvas canvas, Paint piant) {
        // 初始化画笔属性
        piant.setColor(sectorColor);
        piant.setStyle(Paint.Style.STROKE);
        piant.setStrokeWidth(2);
        // 绘外圈
        canvas.drawCircle(centerX, centerY, radius, piant);
        // 绘内圈
        piant.setColor(unSweepColor);
        piant.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius - 2, piant);
        piant.setColor(sectorColor);
        RectF oval = new RectF(centerX - radius + 2, centerY - radius + 2, radius + centerX - 2, radius + centerY - 2);
        canvas.drawArc(oval, -90, 360 * progress / max, true, piant);

        if (showText) {
            // 画进度文字
            piant.setStyle(Paint.Style.FILL);
            piant.setColor(percentTextColor);
            piant.setTextSize(percentTextSize);

            //进度
            String percent = String.valueOf(progress * 100 / max);
            if (showPercentSign)
                percent = percent + "%";

            Rect rect = new Rect();
            piant.getTextBounds(percent, 0, percent.length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();
            if (textWidth >= radius * 2) {
                textWidth = radius * 2;
            }
            canvas.drawText(percent, centerX - textWidth / 2, centerY + textHeight / 2, piant);
        }
    }

    public FRProgressBar setProgress(int progress) {
        if (progress > max) {
            this.progress = max;
        } else {
            this.progress = progress;
        }
        postInvalidate();
        return this;
    }

    public FRProgressBar setMax(int max) {
        this.max = max;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public FRProgressBar setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public int getPercentTextSize() {
        return percentTextSize;
    }

    public FRProgressBar setPercentTextSize(int percentTextSize) {
        this.percentTextSize = percentTextSize;
        return this;
    }

    public int getPercentTextColor() {
        return percentTextColor;
    }

    public FRProgressBar setPercentTextColor(int percentTextColor) {
        this.percentTextColor = percentTextColor;
        return this;
    }

    public int getProgressBarBgColor() {
        return progressBarBgColor;
    }

    public FRProgressBar setProgressBarBgColor(int progressBarBgColor) {
        this.progressBarBgColor = progressBarBgColor;
        return this;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public FRProgressBar setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public int getOrientation() {
        return orientation;
    }

    public FRProgressBar setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public boolean isHorizonStroke() {
        return isHorizonStroke;
    }

    public FRProgressBar setHorizonStroke(boolean isHorizonStroke) {
        this.isHorizonStroke = isHorizonStroke;
        return this;
    }

    public int getRectRound() {
        return rectRound;
    }

    public FRProgressBar setRectRound(int rectRound) {
        this.rectRound = rectRound;
        return this;
    }

    public int getMax() {
        return max;
    }

    public int getProgress() {
        return progress;
    }

    public int getSectorColor() {
        return sectorColor;
    }

    public FRProgressBar setSectorColor(int sectorColor) {
        this.sectorColor = sectorColor;
        return this;
    }

    public int getUnSweepColor() {
        return unSweepColor;
    }

    public FRProgressBar setUnSweepColor(int unSweepColor) {
        this.unSweepColor = unSweepColor;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public FRProgressBar setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public boolean isShowPercentSign() {
        return showPercentSign;
    }

    public FRProgressBar setShowPercentSign(boolean showPercentSign) {
        this.showPercentSign = showPercentSign;
        return this;
    }

    public boolean isShowText() {
        return showText;
    }

    public FRProgressBar setShowText(boolean showText) {
        this.showText = showText;
        return this;
    }

    public int getCenterRectRadius() {
        return centerRectRadius;
    }

    public FRProgressBar setCenterRectRadius(int centerRectRadius) {
        this.centerRectRadius = centerRectRadius;
        return this;
    }

    public boolean isShowCenterRect() {
        return showCenterRect;
    }

    public FRProgressBar setShowCenterRect(boolean showCenterRect) {
        this.showCenterRect = showCenterRect;
        return this;
    }
}
