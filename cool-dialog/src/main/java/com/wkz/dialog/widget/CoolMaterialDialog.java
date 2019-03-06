package com.wkz.dialog.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.wkz.dialog.utils.CoolCornerUtils;
import com.wkz.dialog.base.internal.CoolBaseAlertDialog;

/**
 * Dialog like Material Design Dialog
 */
public class CoolMaterialDialog extends CoolBaseAlertDialog<CoolMaterialDialog> {

    public CoolMaterialDialog(Context context) {
        super(context);

        /* default value*/
        mTitleTextColor = Color.parseColor("#DE000000");
        mTitleTextSize = 22f;
        mContentTextColor = Color.parseColor("#8a000000");
        mContentTextSize = 16f;
        mLeftBtnTextColor = Color.parseColor("#383838");
        mRightBtnTextColor = Color.parseColor("#468ED0");
        mMiddleBtnTextColor = Color.parseColor("#00796B");
        /* default value*/
    }

    @Override
    public View onCreateView() {

        /* title */
        mTvTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTvTitle.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(0));
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLlContainer.addView(mTvTitle);

        /* content */
        mTvContent.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(20));
        mTvContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLlContainer.addView(mTvContent);

        /* btns */
        mLlBtns.setGravity(Gravity.END);
        mLlBtns.addView(mTvBtnLeft);
        mLlBtns.addView(mTvBtnMiddle);
        mLlBtns.addView(mTvBtnRight);

        mTvBtnLeft.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mTvBtnRight.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mTvBtnMiddle.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mLlBtns.setPadding(dp2px(20), dp2px(0), dp2px(10), dp2px(10));

        mLlContainer.addView(mLlBtns);

        return mLlContainer;
    }

    @Override
    public void setUiBeforeShow() {
        super.setUiBeforeShow();
        /* set background color and corner radius */
        float radius = dp2px(mCornerRadius);
        mLlContainer.setBackgroundDrawable(CoolCornerUtils.cornerDrawable(mBgColor, radius));
        mTvBtnLeft.setBackgroundDrawable(CoolCornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
        mTvBtnRight.setBackgroundDrawable(CoolCornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
        mTvBtnMiddle.setBackgroundDrawable(CoolCornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
    }
}
