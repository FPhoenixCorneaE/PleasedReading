package com.wkz.dialog.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wkz.dialog.base.CoolBaseDialog;
import com.wkz.dialog.entity.CoolDialogMenuItem;
import com.wkz.dialog.listener.OnCoolOperItemClickListener;
import com.wkz.dialog.utils.CoolCornerUtils;

import java.util.ArrayList;

public class CoolNormalListDialog extends CoolBaseDialog<CoolNormalListDialog> {
    /**
     * ListView
     */
    private ListView mLv;
    /**
     * title
     */
    private TextView mTvTitle;
    /**
     * corner radius,dp(圆角程度,单位dp)
     */
    private float mCornerRadius = 5;
    /**
     * title background color(标题背景颜色)
     */
    private int mTitleBgColor = Color.parseColor("#303030");
    /**
     * title text(标题)
     */
    private String mTitle = "提示";
    /**
     * title textColor(标题颜色)
     */
    private int mTitleTextColor = Color.parseColor("#ffffff");
    /**
     * title textSize(标题字体大小,单位sp)
     */
    private float mTitleTextSize = 16.5f;
    /**
     * ListView background color(ListView背景色)
     */
    private int mLvBgColor = Color.parseColor("#ffffff");
    /**
     * divider color(ListView divider颜色)
     */
    private int mDividerColor = Color.LTGRAY;
    /**
     * divider height(ListView divider高度)
     */
    private float mDividerHeight = 0.8f;
    /**
     * item press color(ListView item按住颜色)
     */
    private int mItemPressColor = Color.parseColor("#ffcccccc");
    /**
     * item textColor(ListView item文字颜色)
     */
    private int mItemTextColor = Color.parseColor("#303030");
    /**
     * item textSize(ListView item文字大小)
     */
    private float mItemTextSize = 15f;
    /**
     * item extra padding(ListView item额外padding)
     */
    private int mItemExtraLeft;
    private int mItemExtraTop;
    private int mItemExtraRight;
    private int mItemExtraBottom;
    /**
     * enable title show(是否显示标题)
     */
    private boolean mIsTitleShow = true;
    /**
     * adapter(自定义适配器)
     */
    private BaseAdapter mAdapter;
    /**
     * operation items(操作items)
     */
    private ArrayList<CoolDialogMenuItem> mContents = new ArrayList<>();
    private OnCoolOperItemClickListener mOnOperItemClickListener;
    private LayoutAnimationController mLac;

    public void setOnOperItemClickL(OnCoolOperItemClickListener onOperItemClickListener) {
        mOnOperItemClickListener = onOperItemClickListener;
    }

    public CoolNormalListDialog(Context context, ArrayList<CoolDialogMenuItem> baseItems) {
        super(context);
        mContents.addAll(baseItems);
        init();
    }

    public CoolNormalListDialog(Context context, String[] items) {
        super(context);
        mContents = new ArrayList<>();
        for (String item : items) {
            CoolDialogMenuItem customBaseItem = new CoolDialogMenuItem(item, 0);
            mContents.add(customBaseItem);
        }
        init();
    }

    public CoolNormalListDialog(Context context, BaseAdapter adapter) {
        super(context);
        mAdapter = adapter;
        init();
    }

    private void init() {
        widthScale(0.8f);

        /* LayoutAnimation */
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(550);

        mLac = new LayoutAnimationController(animation, 0.12f);
        mLac.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public View onCreateView() {
        LinearLayout ll_container = new LinearLayout(mContext);
        ll_container.setOrientation(LinearLayout.VERTICAL);

        /* title */
        mTvTitle = new TextView(mContext);
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mTvTitle.setSingleLine(true);
        mTvTitle.setPadding(dp2px(18), dp2px(10), 0, dp2px(10));

        ll_container.addView(mTvTitle);

        /* listview */
        mLv = new ListView(mContext);
        mLv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLv.setCacheColorHint(Color.TRANSPARENT);
        mLv.setFadingEdgeLength(0);
        mLv.setVerticalScrollBarEnabled(false);
        mLv.setSelector(new ColorDrawable(Color.TRANSPARENT));

        ll_container.addView(mLv);

        return ll_container;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUiBeforeShow() {
        /* title */
        float radius = dp2px(mCornerRadius);
        mTvTitle.setBackgroundDrawable(CoolCornerUtils.cornerDrawable(mTitleBgColor, new float[]{radius, radius, radius,
                radius, 0, 0, 0, 0}));
        mTvTitle.setText(mTitle);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);
        mTvTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);

        /* listview */
        mLv.setDivider(new ColorDrawable(mDividerColor));
        mLv.setDividerHeight(dp2px(mDividerHeight));

        if (mIsTitleShow) {
            mLv.setBackgroundDrawable(CoolCornerUtils.cornerDrawable(mLvBgColor, new float[]{0, 0, 0, 0, radius, radius, radius,
                    radius}));
        } else {
            mLv.setBackgroundDrawable(CoolCornerUtils.cornerDrawable(mLvBgColor, radius));
        }

        if (mAdapter == null) {
            mAdapter = new ListDialogAdapter();
        }

        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnOperItemClickListener != null) {
                    mOnOperItemClickListener.onOperItemClick(parent, view, position, id);
                }
            }
        });

        mLv.setLayoutAnimation(mLac);
    }

    /**
     * set title background color(设置标题栏背景色) @return CoolNormalListDialog
     */
    public CoolNormalListDialog titleBgColor(int titleBgColor) {
        mTitleBgColor = titleBgColor;
        return this;
    }

    /**
     * set title text(设置标题内容)
     */
    public CoolNormalListDialog title(String title) {
        mTitle = title;
        return this;
    }

    /**
     * set title textSize(设置标题字体大小)
     */
    public CoolNormalListDialog titleTextSize_SP(float titleTextSize_SP) {
        mTitleTextSize = titleTextSize_SP;
        return this;
    }

    /**
     * set title textColor(设置标题字体颜色)
     */
    public CoolNormalListDialog titleTextColor(int titleTextColor) {
        mTitleTextColor = titleTextColor;
        return this;
    }

    /*** enable title show(设置标题是否显示) */
    public CoolNormalListDialog isTitleShow(boolean isTitleShow) {
        mIsTitleShow = isTitleShow;
        return this;
    }

    /**
     * set ListView background color(设置ListView背景)
     */
    public CoolNormalListDialog lvBgColor(int lvBgColor) {
        mLvBgColor = lvBgColor;
        return this;
    }

    /**
     * set corner radius(设置圆角程度,单位dp)
     */
    public CoolNormalListDialog cornerRadius(float cornerRadius_DP) {
        mCornerRadius = cornerRadius_DP;
        return this;
    }

    /**
     * set divider color(ListView divider颜色)
     */
    public CoolNormalListDialog dividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        return this;
    }

    /**
     * set divider height(ListView divider高度)
     */
    public CoolNormalListDialog dividerHeight(float dividerHeight_DP) {
        mDividerHeight = dividerHeight_DP;
        return this;
    }

    /**
     * set item press color(item按住颜色)
     */
    public CoolNormalListDialog itemPressColor(int itemPressColor) {
        mItemPressColor = itemPressColor;
        return this;
    }

    /**
     * set item textColor(item字体颜色)
     */
    public CoolNormalListDialog itemTextColor(int itemTextColor) {
        mItemTextColor = itemTextColor;
        return this;
    }

    /**
     * set item textSize(item字体大小)
     */
    public CoolNormalListDialog itemTextSize(float itemTextSize_SP) {
        mItemTextSize = itemTextSize_SP;
        return this;
    }

    /**
     * set item height(item高度)
     */
    public CoolNormalListDialog setItemExtraPadding(int itemLeft, int itemTop, int itemRight, int itemBottom) {
        mItemExtraLeft = dp2px(itemLeft);
        mItemExtraTop = dp2px(itemTop);
        mItemExtraRight = dp2px(itemRight);
        mItemExtraBottom = dp2px(itemBottom);

        return this;
    }

    /**
     * set layoutAnimation(设置layout动画 ,传入null将不显示layout动画)
     */
    public CoolNormalListDialog layoutAnimation(LayoutAnimationController lac) {
        mLac = lac;
        return this;
    }

    class ListDialogAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mContents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CoolDialogMenuItem item = mContents.get(position);

            LinearLayout llItem = new LinearLayout(mContext);
            llItem.setOrientation(LinearLayout.HORIZONTAL);
            llItem.setGravity(Gravity.CENTER_VERTICAL);

            ImageView ivItem = new ImageView(mContext);
            ivItem.setPadding(0, 0, dp2px(15), 0);
            llItem.addView(ivItem);

            TextView tvItem = new TextView(mContext);
            tvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvItem.setSingleLine(true);
            tvItem.setTextColor(mItemTextColor);
            tvItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, mItemTextSize);

            llItem.addView(tvItem);
            float radius = dp2px(mCornerRadius);
            if (mIsTitleShow) {
                llItem.setBackgroundDrawable((CoolCornerUtils.listItemSelector(radius, Color.TRANSPARENT, mItemPressColor,
                        position == mContents.size() - 1)));
            } else {
                llItem.setBackgroundDrawable(CoolCornerUtils.listItemSelector(radius, Color.TRANSPARENT, mItemPressColor,
                        mContents.size(), position));
            }

            int left = item.mResId == 0 ? dp2px(18) : dp2px(16);
            int top = dp2px(10);
            int right = 0;
            int bottom = dp2px(10);
            llItem.setPadding(left + mItemExtraLeft, top + mItemExtraTop, right + mItemExtraRight, bottom + mItemExtraBottom);

            ivItem.setImageResource(item.mResId);
            tvItem.setText(item.mOperName);
            ivItem.setVisibility(item.mResId == 0 ? View.GONE : View.VISIBLE);

            return llItem;
        }
    }
}
