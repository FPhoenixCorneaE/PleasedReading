package com.wkz.framework.widgets.tab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wkz.framework.utils.SizeUtils;

import java.util.Arrays;
import java.util.List;

public class FRColorTrackTabLayout extends RelativeLayout {

    private Builder mBuilder;

    public FRColorTrackTabLayout(Builder builder) {
        super(builder.mContext);
        init(builder.mContext);
    }

    public FRColorTrackTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FRColorTrackTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FRColorTrackTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context mContext) {
        if (mBuilder == null) {
            mBuilder = new Builder(mContext);
        }
        LayoutParams layoutParams=new LayoutParams(mBuilder.mTabWidth,mBuilder.mTabHeight);
    }

    public static class Builder {

        private Context mContext;
        private ViewPager mViewPager;
        private List<String> mPageTitles;
        private List<Fragment> mPageFragments;
        private int mTabWidth = LayoutParams.MATCH_PARENT;
        private int mTabHeight = SizeUtils.dp2px(40f);
        private int mTabBackground = Color.WHITE;
        private int mTabMode = TabLayout.MODE_SCROLLABLE;
        private int mTabGravity = TabLayout.GRAVITY_FILL;
        private int mTabIndicatorColor = Color.RED;
        private int mTabIndicatorHeight = SizeUtils.dp2px(2f);
        private TabLayout.OnTabSelectedListener mOnTabSelectedListener;
        private ColorStateList mTabTextColors;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder with(ViewPager viewPager) {
            this.mViewPager = viewPager;
            return this;
        }

        public Builder setPageTitles(List<String> pageTitles) {
            this.mPageTitles = pageTitles;
            return this;
        }
        public Builder setPageTitles(String[] pageTitles) {
            this.mPageTitles = Arrays.asList(pageTitles);
            return this;
        }

        public Builder setPageFragments(List<Fragment> pageFragments) {
            this.mPageFragments = pageFragments;
            return this;
        }

        public Builder setTabWidthAndHeight(int width, int height) {
            this.mTabWidth = width;
            this.mTabHeight = height;
            return this;
        }

        public Builder setTabMode(@TabLayout.Mode int mode) {
            this.mTabMode = mode;
            return this;
        }

        public Builder setTabBackground(@ColorInt int color) {
            this.mTabBackground = color;
            return this;
        }

        public Builder setTabGravity(@TabLayout.TabGravity int gravity) {
            this.mTabGravity = gravity;
            return this;
        }

        public Builder setSelectedTabIndicatorColor(@ColorInt int color) {
            this.mTabIndicatorColor = color;
            return this;
        }

        public Builder setSelectedTabIndicatorHeight(int height) {
            this.mTabIndicatorHeight = height;
            return this;
        }

        public Builder addOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener listener) {
            this.mOnTabSelectedListener = listener;
            return this;
        }

        public Builder setTabTextColors(int normalColor, int selectedColor) {
            setTabTextColors(createColorStateList(normalColor, selectedColor));
            return this;
        }

        public Builder setTabTextColors(@Nullable ColorStateList textColor) {
            this.mTabTextColors = textColor;
            return this;
        }

        private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
            final int[][] states = new int[2][];
            final int[] colors = new int[2];
            int i = 0;
            states[i] = SELECTED_STATE_SET;
            colors[i] = selectedColor;
            i++;

            // Default enabled state
            states[i] = EMPTY_STATE_SET;
            colors[i] = defaultColor;
            return new ColorStateList(states, colors);
        }

        public FRColorTrackTabLayout build() {
            return new FRColorTrackTabLayout(this);
        }
    }
}
