package com.wkz.viewer.widget.viewpager;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wkz.viewer.widget.FRImageViewerAttacher;
import com.wkz.viewer.widget.FRScaleImageView;

import java.util.ArrayList;
import java.util.List;


public class FRPreviewAdapter extends PagerAdapter {
    // 无效值
    private final int INVALID_VALUE = -1;
    // 预览的起始位置
    private int mStartPosition;
    // 第一个展示的 View
    private FRScaleImageView mStartView;
    // 图片资源
    private List mImageDataList;
    // itemView 集合（在 itemView 被移除后，会被重复使用）
    private List<FRScaleImageView> mActiveViews;
    private FRImageViewerAttacher mAttacher;

    public FRPreviewAdapter(FRImageViewerAttacher attacher) {
        this.mAttacher = attacher;
        mImageDataList = new ArrayList<>();
        mActiveViews = new ArrayList<>();
    }

    public void setStartView(FRScaleImageView itemView) {
        mStartPosition = itemView.getPosition();
        // 提前创建 itemView，用作执行图片浏览器的开启动画
        mStartView = itemView;
    }

    /**
     * 设置图片资源
     *
     * @param list
     */
    @SuppressWarnings("unchecked")
    public void setImageData(List list) {
        this.mImageDataList.clear();
        this.mImageDataList.addAll(list);
    }

    @Override
    public int getCount() {
        return mImageDataList != null ? mImageDataList.size() : 0;
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        FRScaleImageView itemView = null;
        if (mStartPosition == position) {
            itemView = mStartView;
            mActiveViews.add(itemView);
            // 无效化起始位置值，防止重复执行此方法
            mStartPosition = INVALID_VALUE;
        } else if (mActiveViews != null && mActiveViews.size() > 0) {
            for (int i = 0, len = mActiveViews.size(); i < len; i++) {
                FRScaleImageView scaleImageView = mActiveViews.get(i);
                if (scaleImageView.getParent() == null) {
                    itemView = mAttacher.setupItemViewConfig(position, scaleImageView);
                    break;
                }
            }
        }
        if (itemView == null) {
            itemView = mAttacher.createItemView(position);
            mActiveViews.add(itemView);
        }
        // 加载页面
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 回收图片，释放内存
        ((FRScaleImageView) object).recycle();
        // 移除页面
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法，此方法会刷新所有的 item
        return POSITION_NONE;
    }

    /**
     * 根据 position 获取 itemView
     *
     * @param position
     * @return
     */
    public FRScaleImageView getViewByPosition(int position) {
        FRScaleImageView itemView = null;
        if (mStartPosition == INVALID_VALUE) {
            for (int i = 0, len = mActiveViews.size(); i < len; i++) {
                if (mActiveViews.get(i).getId() == position) {
                    itemView = mActiveViews.get(i);
                    break;
                }
            }
        } else if (mStartPosition == position) {
            itemView = mStartView;
        }
        return itemView;
    }

    public void clear() {
        if (mActiveViews != null && mActiveViews.size() > 0) {
            for (int i = 0, len = mActiveViews.size(); i < len; i++) {
                FRScaleImageView itemView = mActiveViews.get(i);
                itemView.recycle();
                itemView = null;
            }
            mActiveViews.clear();
        }
        if (mStartView != null) {
            mStartView.recycle();
            mStartView = null;
        }
        mStartPosition = INVALID_VALUE;
    }
}
