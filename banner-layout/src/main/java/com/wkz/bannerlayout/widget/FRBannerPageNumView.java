package com.wkz.bannerlayout.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.wkz.bannerlayout.annotation.FRTipsPageNumSiteMode;
import com.wkz.bannerlayout.utils.FRBannerSelectorUtils;
import com.wkz.bannerlayout.utils.FRDisplayUtils;

public class FRBannerPageNumView extends AppCompatTextView {

    @NonNull
    public final FrameLayout.LayoutParams initPageView(@NonNull FRBannerPageNumView.PageNumViewInterface pageNumViewInterface) {
        this.setGravity(Gravity.CENTER);
        this.setTextColor(pageNumViewInterface.pageNumViewTextColor());
        this.setTextSize(pageNumViewInterface.pageNumViewTextSize());
        this.setBackground(
                FRBannerSelectorUtils.getShape(
                        this.getContext(),
                        pageNumViewInterface.pageNumViewRadius(),
                        pageNumViewInterface.pageNumViewBackgroundColor()
                )
        );
        this.setPadding(
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewPaddingLeft()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewPaddingTop()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewPaddingRight()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewPaddingBottom())
        );

        FrameLayout.LayoutParams pageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        @FRTipsPageNumSiteMode
        int pageNumViewSiteMode = pageNumViewInterface.pageNumViewSite();
        switch (pageNumViewSiteMode) {
            case FRTipsPageNumSiteMode.TOP_LEFT:
            default:
                pageParams.gravity = Gravity.START | Gravity.TOP;
                break;
            case FRTipsPageNumSiteMode.TOP_RIGHT:
                pageParams.gravity = Gravity.END | Gravity.TOP;
                break;
            case FRTipsPageNumSiteMode.BOTTOM_LEFT:
                pageParams.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case FRTipsPageNumSiteMode.BOTTOM_RIGHT:
                pageParams.gravity = Gravity.END | Gravity.BOTTOM;
                break;
            case FRTipsPageNumSiteMode.CENTER_LEFT:
                pageParams.gravity = Gravity.START | Gravity.CENTER;
                break;
            case FRTipsPageNumSiteMode.CENTER_RIGHT:
                pageParams.gravity = Gravity.END | Gravity.CENTER;
                break;
            case FRTipsPageNumSiteMode.TOP_CENTER:
                pageParams.gravity = Gravity.TOP | Gravity.CENTER;
                break;
            case FRTipsPageNumSiteMode.BOTTOM_CENTER:
                pageParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        }

        pageParams.setMargins(
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewLeftMargin()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewTopMargin()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewRightMargin()),
                FRDisplayUtils.dp2px(getContext(), pageNumViewInterface.pageNumViewBottomMargin())
        );
        return pageParams;
    }

    public FRBannerPageNumView(@NonNull Context context) {
        this(context, null);
    }

    public FRBannerPageNumView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FRBannerPageNumView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface PageNumViewInterface {

        float pageNumViewTopMargin();

        float pageNumViewRightMargin();

        float pageNumViewBottomMargin();

        float pageNumViewLeftMargin();

        int pageNumViewTextColor();

        float pageNumViewTextSize();

        float pageNumViewPaddingTop();

        float pageNumViewPaddingLeft();

        float pageNumViewPaddingBottom();

        float pageNumViewPaddingRight();

        float pageNumViewRadius();

        int pageNumViewBackgroundColor();

        int pageNumViewSite();

    }
}