package com.wkz.viewer.listener;

import com.wkz.viewer.widget.FRScaleImageView;

/**
 * 图片的切换监听事件
 */
public interface OnImageChangedListener {

    void onImageSelected(int position, FRScaleImageView view);
}
