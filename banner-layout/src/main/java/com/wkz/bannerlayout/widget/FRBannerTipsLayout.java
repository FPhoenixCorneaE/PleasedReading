package com.wkz.bannerlayout.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wkz.bannerlayout.annotation.FRTipsDotsSiteMode;
import com.wkz.bannerlayout.annotation.FRTipsLayoutSiteMode;
import com.wkz.bannerlayout.annotation.FRTipsProgressesSiteMode;
import com.wkz.bannerlayout.annotation.FRTipsTitleSiteMode;
import com.wkz.bannerlayout.utils.FRDisplayUtils;

public final class FRBannerTipsLayout extends RelativeLayout {

    private Context mContext;
    private TextView mTextView;
    private LinearLayout mDotsContainer;
    private LinearLayout mProgressesContainer;

    /**
     * Initialize the dots
     */
    public void setDots(@NonNull FRBannerTipsLayout.DotsInterface dotsInterface) {
        this.mDotsContainer.removeAllViews();

        for (int i = 0; i < dotsInterface.dotsCount(); ++i) {
            View view = new View(mContext);
            view.setBackground(dotsInterface.dotsSelector());
            view.setEnabled(i == 0);

            MarginLayoutParams marginLayoutParams = new MarginLayoutParams(
                    FRDisplayUtils.dp2px(mContext, dotsInterface.dotsWidth()),
                    FRDisplayUtils.dp2px(mContext, dotsInterface.dotsHeight()));
            MarginLayoutParamsCompat.setMarginStart(marginLayoutParams,
                    FRDisplayUtils.dp2px(mContext, dotsInterface.dotsLeftMargin()));
            marginLayoutParams.topMargin = FRDisplayUtils.dp2px(mContext, dotsInterface.dotsTopMargin());
            MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams,
                    FRDisplayUtils.dp2px(mContext, dotsInterface.dotsRightMargin()));
            marginLayoutParams.bottomMargin = FRDisplayUtils.dp2px(mContext, dotsInterface.dotsBottomMargin());
            this.mDotsContainer.addView(view, marginLayoutParams);
        }

        this.mDotsContainer.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < dotsInterface.dotsSite().length; i++) {
            params.addRule(dotsInterface.dotsSite()[i]);
        }
        this.addView(this.mDotsContainer, params);
    }

    /**
     * Initialize the Progresses
     */
    public void setProgresses(@NonNull FRBannerTipsLayout.ProgressInterface progressInterface) {
        this.mProgressesContainer.removeAllViews();

        for (int i = 0; i < progressInterface.progressCount(); ++i) {
            View view = new View(mContext);
            view.setBackground(progressInterface.progressBuilder().build());

            MarginLayoutParams marginLayoutParams = new MarginLayoutParams(
                    (int) progressInterface.progressBuilder().getWidth(),
                    (int) progressInterface.progressBuilder().getHeight());
            MarginLayoutParamsCompat.setMarginStart(marginLayoutParams,
                    FRDisplayUtils.dp2px(mContext, progressInterface.progressLeftMargin()));
            marginLayoutParams.topMargin = FRDisplayUtils.dp2px(mContext, progressInterface.progressTopMargin());
            MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams,
                    FRDisplayUtils.dp2px(mContext, progressInterface.progressRightMargin()));
            marginLayoutParams.bottomMargin = FRDisplayUtils.dp2px(mContext, progressInterface.progressBottomMargin());
            this.mProgressesContainer.addView(view, marginLayoutParams);
        }

        this.mProgressesContainer.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < progressInterface.progressSite().length; i++) {
            params.addRule(progressInterface.progressSite()[i]);
        }
        this.addView(this.mProgressesContainer, params);
    }

    @NonNull
    public FrameLayout.LayoutParams setBannerTips(@NonNull FRBannerTipsLayout.TipsInterface tipsInterface) {
        FrameLayout.LayoutParams tipsParams = new FrameLayout.LayoutParams(tipsInterface.tipsWidth(), tipsInterface.tipsHeight());
        @FRTipsLayoutSiteMode
        int tipsSiteMode = tipsInterface.tipsSite();
        switch (tipsSiteMode) {
            case FRTipsLayoutSiteMode.LEFT:
                tipsParams.gravity = Gravity.START;
                break;
            case FRTipsLayoutSiteMode.TOP:
                tipsParams.gravity = Gravity.TOP;
                break;
            case FRTipsLayoutSiteMode.RIGHT:
                tipsParams.gravity = Gravity.END;
                break;
            case FRTipsLayoutSiteMode.BOTTOM:
                tipsParams.gravity = Gravity.BOTTOM;
                break;
            case FRTipsLayoutSiteMode.CENTER:
                tipsParams.gravity = Gravity.CENTER;
        }

        if (tipsInterface.showBackgroundColor()) {
            this.setBackgroundColor(tipsInterface.tipsLayoutBackgroundColor());
        }

        return tipsParams;
    }

    /**
     * Update the dot position
     */
    public void changeDotsPosition(int position, int newPosition) {
        View childAt = this.mDotsContainer.getChildAt(position);
        View newChildAt = this.mDotsContainer.getChildAt(newPosition);
        if (childAt != null) {
            childAt.setEnabled(false);
        }

        if (newChildAt != null) {
            newChildAt.setEnabled(true);
        }

    }

    /**
     * Update the Progress position
     */
    public void changeProgressesPosition(int position, int newPosition) {
        View childAt = this.mProgressesContainer.getChildAt(position);
        View newChildAt = this.mProgressesContainer.getChildAt(newPosition);
        if (childAt != null) {
            if (childAt.getBackground() instanceof FRProgressDrawable) {
                ((FRProgressDrawable) childAt.getBackground()).end();
            }
        }

        if (newChildAt != null) {
            if (newChildAt.getBackground() instanceof FRProgressDrawable) {
                ((FRProgressDrawable) newChildAt.getBackground()).start();
            }
        }

    }

    /**
     * Update title, the default on the left
     */
    public final void setTitle(@NonNull FRBannerTipsLayout.TitleInterface titleInterface) {
        this.mTextView.setGravity(Gravity.CENTER_VERTICAL);
        this.mTextView.setTextColor(titleInterface.titleColor());
        this.mTextView.setTextSize(titleInterface.titleSize());
        this.mTextView.setSingleLine(true);
        this.mTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.mTextView.setBackgroundColor(titleInterface.titleBackgroundColor());
        this.mTextView.setPadding(
                FRDisplayUtils.dp2px(mContext, titleInterface.titleLeftMargin()),
                FRDisplayUtils.dp2px(mContext, titleInterface.titleTopMargin()),
                FRDisplayUtils.dp2px(mContext, titleInterface.titleRightMargin()),
                FRDisplayUtils.dp2px(mContext, titleInterface.titleBottomMargin())
        );

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                titleInterface.titleWidth() == -1 ?
                        ViewGroup.LayoutParams.MATCH_PARENT :
                        FRDisplayUtils.dp2px(mContext, titleInterface.titleWidth()),
                titleInterface.titleHeight() == -2 ?
                        ViewGroup.LayoutParams.WRAP_CONTENT :
                        FRDisplayUtils.dp2px(mContext, titleInterface.titleHeight())
        );
        for (int i = 0; i < titleInterface.titleSite().length; i++) {
            params.addRule(titleInterface.titleSite()[i]);
        }

        this.addView(this.mTextView, params);
    }

    public void setTitle(String title) {
        this.clearText();
        if (!TextUtils.isEmpty(title)) {
            this.mTextView.setText(title);
        }
    }

    private void clearText() {
        this.mTextView.setText("");
    }

    public FRBannerTipsLayout(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.mTextView = new TextView(mContext);
        this.mDotsContainer = new LinearLayout(mContext);
        this.mProgressesContainer = new LinearLayout(mContext);
    }

    public interface TipsInterface {
        boolean showBackgroundColor();

        int tipsSite();

        int tipsWidth();

        int tipsHeight();

        int tipsLayoutBackgroundColor();
    }


    public interface TitleInterface {
        int titleColor();

        float titleSize();

        float titleLeftMargin();

        float titleTopMargin();

        float titleRightMargin();

        float titleBottomMargin();

        float titleWidth();

        float titleHeight();

        @ColorInt
        int titleBackgroundColor();

        @FRTipsTitleSiteMode
        int[] titleSite();
    }

    public interface DotsInterface {
        int dotsCount();

        Drawable dotsSelector();

        float dotsHeight();

        float dotsWidth();

        float dotsLeftMargin();

        float dotsTopMargin();

        float dotsRightMargin();

        float dotsBottomMargin();

        @FRTipsDotsSiteMode
        int[] dotsSite();
    }

    public interface ProgressInterface {
        int progressCount();

        float progressLeftMargin();

        float progressTopMargin();

        float progressRightMargin();

        float progressBottomMargin();

        @FRTipsProgressesSiteMode
        int[] progressSite();

        FRProgressDrawable.Builder progressBuilder();
    }
}
