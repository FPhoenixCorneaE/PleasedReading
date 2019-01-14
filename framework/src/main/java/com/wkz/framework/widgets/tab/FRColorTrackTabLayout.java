package com.wkz.framework.widgets.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wkz.framework.R;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.utils.SizeUtils;

import java.util.Arrays;
import java.util.List;

public class FRColorTrackTabLayout extends RelativeLayout {

    private Builder mBuilder;

    public FRColorTrackTabLayout(Context context) {
        super(context);
    }

    public FRColorTrackTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Builder builder) {
        mBuilder = builder == null ? new Builder() : builder;
        if (mBuilder.mViewPager == null) return;
        if (mBuilder.mFragmentManager == null) return;
        if (mBuilder.mPageFragments == null) return;
        if (mBuilder.mPageTitles == null) return;
        if (mBuilder.mPageFragments.size() != mBuilder.mPageTitles.size()) return;
        mBuilder.mViewPager.setAdapter(new SimpleFragmentPagerAdapter(mBuilder.mFragmentManager, mBuilder.mPageFragments, mBuilder.mPageTitles));
        mBuilder.mViewPager.setOffscreenPageLimit(mBuilder.mOffscreenPageLimit);
        TabLayout tabLayout = new TabLayout(getContext());
        tabLayout.setLayoutParams(new LayoutParams(mBuilder.mTabWidth, mBuilder.mTabHeight));
        tabLayout.setBackgroundColor(mBuilder.mTabBackground);
        tabLayout.setTabMode(mBuilder.mTabMode);
        tabLayout.setTabGravity(mBuilder.mTabGravity);
        tabLayout.setTabTextColors(mBuilder.mTabTextColors);
        tabLayout.setSelectedTabIndicatorHeight(mBuilder.mTabIndicatorHeight);
        tabLayout.setSelectedTabIndicatorColor(mBuilder.mTabIndicatorColor);
        tabLayout.setupWithViewPager(mBuilder.mViewPager);
        if (mBuilder.mOnTabSelectedListener != null) {
            tabLayout.addOnTabSelectedListener(mBuilder.mOnTabSelectedListener);
        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) return;
            if (i == 0)
                tab.setCustomView(getTabView(i, true));
            else {
                tab.setCustomView(getTabView(i, false));
            }
        }

        mBuilder.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean checkIfScroll = false;
            private int lastPosition;

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (checkIfScroll) {
                    if (positionOffset > 0) {
                        FRColorTrackView left = (FRColorTrackView) tabLayout.getTabAt(position).getCustomView();
                        FRColorTrackView right = (FRColorTrackView) tabLayout.getTabAt(position + 1).getCustomView();

                        left.setDirection(FRColorTrackView.Direction.RIGHT);
                        right.setDirection(FRColorTrackView.Direction.LEFT);
                        left.setProgress(1 - positionOffset);
                        right.setProgress(positionOffset);
                    }
                }
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onPageSelected(int position) {
                ((FRColorTrackView) tabLayout.getTabAt(lastPosition).getCustomView()).setProgress(0f);
                ((FRColorTrackView) tabLayout.getTabAt(position).getCustomView()).setProgress(1f);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    checkIfScroll = true;
                } else if (state == 0) {
                    checkIfScroll = false;
                }
            }
        });

        addView(tabLayout);
    }

    private FRColorTrackView getTabView(int position, boolean isSelected) {
        FRColorTrackView ctvColorTrack = new FRColorTrackView(getContext());
        LayoutParams layoutParams = new LayoutParams(SizeUtils.dp2px(80f), LayoutParams.MATCH_PARENT);
        ctvColorTrack.setLayoutParams(layoutParams);
        ctvColorTrack.setTextChangeColor(ResourceUtils.getColor(R.color.fr_color_light_red));
        ctvColorTrack.setTextOriginColor(ResourceUtils.getColor(R.color.fr_color_black));
        ctvColorTrack.setTextSize(SizeUtils.sp2px(16));
        ctvColorTrack.setText(mBuilder.mPageTitles.get(position));
        if (isSelected) {
            ctvColorTrack.setProgress(1f);
        }
        return ctvColorTrack;
    }

    private static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<String> mPageTitles;
        private List<Fragment> mPageFragments;

        SimpleFragmentPagerAdapter(FragmentManager fm, @NonNull List<Fragment> mPageFragments, @NonNull List<String> mPageTitles) {
            super(fm);
            this.mPageFragments = mPageFragments;
            this.mPageTitles = mPageTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return mPageFragments.get(position);
        }

        @Override
        public int getCount() {
            return mPageFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitles.get(position);
        }
    }

    public static class Builder {

        private FragmentManager mFragmentManager;
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
        private ColorStateList mTabTextColors = createColorStateList(Color.BLACK, Color.RED);
        private int mOffscreenPageLimit;

        public Builder() {
        }

        public Builder with(ViewPager viewPager) {
            this.mViewPager = viewPager;
            return this;
        }

        public Builder setFragmentManager(FragmentManager mFragmentManager) {
            this.mFragmentManager = mFragmentManager;
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

        public Builder setOffscreenPageLimit(int mOffscreenPageLimit) {
            this.mOffscreenPageLimit = mOffscreenPageLimit;
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
    }
}
