package com.wkz.dialog.base.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wkz.dialog.animation.bounceEnter.CoolBounceLeftEnter;
import com.wkz.dialog.animation.fadeExit.CoolFadeExit;
import com.wkz.dialog.R;
import com.wkz.dialog.utils.CoolCornerUtils;
import com.wkz.dialog.utils.CoolStatusBarUtils;
import com.wkz.dialog.view.CoolTriangleView;
import com.wkz.dialog.base.internal.CoolInternalBasePopup;

/**
 * Use dialog to realize bubble style popup(利用Dialog实现泡泡样式的弹窗)
 * thanks https://github.com/michaelye/EasyDialog
 */
public abstract class CoolBaseBubblePopup<T extends CoolBaseBubblePopup<T>> extends CoolInternalBasePopup<T> {

    protected View mWrappedView;
    protected LinearLayout mLlContent;
    protected CoolTriangleView mTriangleView;
    protected RelativeLayout.LayoutParams mLayoutParams;
    protected int mBubbleColor;
    protected int mCornerRadius;
    protected int mMarginLeft;
    protected int mMarginRight;
    protected int triangleWidth;
    protected int triangleHeight;
    private RelativeLayout.LayoutParams mTriangleLayoutParams;

    public CoolBaseBubblePopup(Context context) {
        super(context);
        mWrappedView = onCreateBubbleView();
        init();
    }

    public CoolBaseBubblePopup(Context context, View wrappedView) {
        super(context);
        mWrappedView = wrappedView;
        init();
    }

    private void init() {
        showAnim(new CoolBounceLeftEnter());
        dismissAnim(new CoolFadeExit());
        dimEnabled(false);

        bubbleColor(Color.parseColor("#BB000000"));
        cornerRadius(5);
        margin(8, 8);
        gravity(Gravity.TOP);
        triangleWidth(24);
        triangleHeight(12);
    }

    public abstract View onCreateBubbleView();

    @Override
    public View onCreateView() {
        View inflate = View.inflate(mContext, R.layout.cool_popup_bubble, null);
        mLlContent = (LinearLayout) inflate.findViewById(R.id.ll_content);
        mTriangleView = (CoolTriangleView) inflate.findViewById(R.id.triangle_view);
        mLlContent.addView(mWrappedView);

        mLayoutParams = (RelativeLayout.LayoutParams) mLlContent.getLayoutParams();
        mTriangleLayoutParams = (RelativeLayout.LayoutParams) mTriangleView.getLayoutParams();
        //让mOnCreateView充满父控件,防止ViewHelper.setXY导致点击事件无效
        inflate.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return inflate;
    }

    @Override
    public void setUiBeforeShow() {
        mLlContent.setBackgroundDrawable(
                CoolCornerUtils.cornerDrawable(mBubbleColor, mCornerRadius));
        mLayoutParams.setMargins(mMarginLeft, 0, mMarginRight, 0);
        mLlContent.setLayoutParams(mLayoutParams);

        mTriangleView.setColor(mBubbleColor);
        mTriangleView.setGravity(mGravity == Gravity.TOP ? Gravity.BOTTOM : Gravity.TOP);

        mTriangleLayoutParams.width = triangleWidth;
        mTriangleLayoutParams.height = triangleHeight;
        mTriangleView.setLayoutParams(mTriangleLayoutParams);
    }

    @Override
    public void onLayoutObtain() {
        mTriangleView.setX(mX - mTriangleView.getWidth() / 2);

        if (mGravity == Gravity.TOP) {
            int y = mY - mTriangleView.getHeight();
            mTriangleView.setY(y);
            mLlContent.setY(y - mLlContent.getHeight());
        } else {
            mTriangleView.setY(mY);
            mLlContent.setY(mY + mTriangleView.getHeight());
        }

        /**
         * mX--->三角形中心距离屏幕左边距离
         * mDisplayMetrics.widthPixels - mX--->三角形中心距离屏幕右边距离
         */
        int availableLeft = mX - mLayoutParams.leftMargin;//左侧最大可用距离
        int availableRight = mDisplayMetrics.widthPixels - mX - mLayoutParams.rightMargin;//右侧最大可用距离

        int x = 0;
        int contentWidth = mLlContent.getWidth();
        if (contentWidth / 2 <= availableLeft && contentWidth / 2 <= availableRight) {
            x = mX - contentWidth / 2;
        } else {
            if (availableLeft <= availableRight) {//三角形在屏幕中心的左边
                x = mLayoutParams.leftMargin;
            } else {//三角形在屏幕中心的右边
                x = mDisplayMetrics.widthPixels - (contentWidth + mLayoutParams.rightMargin);
            }
        }
        mLlContent.setX(x);
    }

    @Override
    public T anchorView(View anchorView) {
        if (anchorView != null) {
            mAnchorView = anchorView;
            int[] location = new int[2];
            mAnchorView.getLocationOnScreen(location);

            mX = location[0] + anchorView.getWidth() / 2;
            if (mGravity == Gravity.TOP) {
                mY = location[1] - CoolStatusBarUtils.getHeight(mContext)
                        - dp2px(1);
            } else {
                mY = location[1] - CoolStatusBarUtils.getHeight(mContext)
                        + anchorView.getHeight() + dp2px(1);
            }
        }
        return (T) this;
    }

    public T bubbleColor(int bubbleColor) {
        mBubbleColor = bubbleColor;
        return (T) this;
    }

    public T cornerRadius(float cornerRadius) {
        mCornerRadius = dp2px(cornerRadius);
        return (T) this;
    }

    public T margin(float marginLeft, float marginRight) {
        mMarginLeft = dp2px(marginLeft);
        mMarginRight = dp2px(marginRight);
        return (T) this;
    }

    public T triangleWidth(float width) {
        triangleWidth = dp2px(width);
        return (T) this;
    }

    public T triangleHeight(float height) {
        triangleHeight = dp2px(height);
        return (T) this;
    }
}
