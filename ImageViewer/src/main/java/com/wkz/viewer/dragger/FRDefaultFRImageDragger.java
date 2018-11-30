package com.wkz.viewer.dragger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.FRViewData;

/**
 * 默认的图片拖拽处理类
 */
public class FRDefaultFRImageDragger extends FRImageDragger {
    // 恢复原样的动画时间
    private final int BACK_ANIM_DURATION = 200;
    // 退出预览的动画时间
    private final int EXIT_ANIM_DURATION = 200;

    public FRDefaultFRImageDragger() {

    }

    @Override
    public void onDragging(float x1, float y1, float x2, float y2) {
        super.onDragging(x1, y1, x2, y2);
        View imageView = scaleImageView.getImageView();
        // 计算 view 的 Y 轴坐标
        final float diff = y2 - y1;
        final float viewY = imageView.getY() + diff;
        // 计算背景透明度
        final float value = Math.abs(viewY) / mAlphaBase;
        mBackgroundAlpha = ( value < 0.8f ? 1 - value : 0.2f) * DEF_BACKGROUND_ALPHA;
        imageView.setY(viewY);
        setBackgroundAlpha((int) mBackgroundAlpha);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        if (!scaleImageView.isImageAnimRunning()) {
            View imageView = scaleImageView.getImageView();
            final float imageViewY = imageView.getY();
            if (Math.abs(imageViewY) <= mMaxDisOnY) {
                reback();
            } else {
                exit();
            }
        }
    }

    /**
     * 图片恢复原样
     */
    private void reback() {
        setImageDraggerState(FRImageDraggerState.DRAG_STATE_BEGIN_REBACK);
        setPreviewStatus(FRImageViewerState.STATE_READY_REBACK, scaleImageView);
        final View imageView = scaleImageView.getImageView();
        final float imageViewY = imageView.getY();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(BACK_ANIM_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            FloatEvaluator evaluator = new FloatEvaluator();

            @SuppressLint("WrongConstant")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float currentValue = (float) animation.getAnimatedValue();
                final float y = evaluator.evaluate(currentValue, imageViewY, 0);
                final float alpha = evaluator.evaluate(currentValue, mBackgroundAlpha, DEF_BACKGROUND_ALPHA);
                imageView.setY(y);
                setBackgroundAlpha((int) alpha);
                setImageDraggerState(FRImageDraggerState.DRAG_STATE_REBACKING);
                setPreviewStatus(FRImageViewerState.STATE_REBACKING, scaleImageView);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBackgroundAlpha = DEF_BACKGROUND_ALPHA;
                setImageDraggerState(FRImageDraggerState.DRAG_STATE_END_REBACK);
                if (checkAttacherNotNull()) {
                    mAttacher.setViewPagerScrollable(true);
                    setPreviewStatus(FRImageViewerState.STATE_COMPLETE_REBACK, scaleImageView);
                    setPreviewStatus(FRImageViewerState.STATE_WATCHING, scaleImageView);
                }
            }
        });
        animator.start();
    }

    /**
     * 退出预览
     */
    private void exit() {
        setImageDraggerState(FRImageDraggerState.DRAG_STATE_BEGIN_EXIT);
        setPreviewStatus(FRImageViewerState.STATE_READY_CLOSE, scaleImageView);
        final ImageView imageView = scaleImageView.getImageView();
        final FRViewData viewData = scaleImageView.getViewData();
        // imageView 当前的 Y 轴坐标
        final float imageViewY = imageView.getY();
        // 图片的原始宽高
        float oriImg_width = 0, oriImg_height = 0;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            oriImg_width = drawable.getIntrinsicWidth();
            oriImg_height = drawable.getIntrinsicHeight();
        } else if (viewData.getImageWidth() != 0 && viewData.getImageHeight() != 0) {
            oriImg_width = viewData.getImageWidth();
            oriImg_height = viewData.getImageHeight();
        }
        final float scale = Math.min(mPreviewWidth / oriImg_width, mPreviewHeight / oriImg_height);
        // 图片的缩放等级为 1f 时的图片高度
        final float adjustHeight = oriImg_height * scale;
        // 图片的缩放等级为 1f 且居中时时，在预览界面中的 Y 轴坐标
        final float adjustImgY = (mPreviewHeight - adjustHeight) / 2;
        // 图片在预览界面中的当前 Y 轴坐标
        float currentImgY = imageViewY + adjustImgY;
        // 此处加 20 ,是为了减少误差，防止影响动画美观
        final float toY = currentImgY > adjustImgY ? imageViewY + (mPreviewHeight - currentImgY + 20) : imageViewY - (currentImgY + adjustHeight + 20);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(EXIT_ANIM_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            FloatEvaluator evaluator = new FloatEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float y = evaluator.evaluate(progress, imageViewY, toY);
                final float alpha = evaluator.evaluate(progress, mBackgroundAlpha, 0);
                imageView.setY(y);
                setBackgroundAlpha((int) alpha);
                setImageDraggerState(FRImageDraggerState.DRAG_STATE_EXITTING);
                setPreviewStatus(FRImageViewerState.STATE_CLOSING, scaleImageView);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (checkAttacherNotNull()) mAttacher.exit();
                mBackgroundAlpha = DEF_BACKGROUND_ALPHA;
                setImageDraggerState(FRImageDraggerState.DRAG_STATE_END_EXIT);
                imageView.setY(0);
                setBackgroundAlpha((int) mBackgroundAlpha);
                setPreviewStatus(FRImageViewerState.STATE_COMPLETE_CLOSE, scaleImageView);
                setPreviewStatus(FRImageViewerState.STATE_SILENCE, null);
            }
        });
        animator.start();
    }
}
