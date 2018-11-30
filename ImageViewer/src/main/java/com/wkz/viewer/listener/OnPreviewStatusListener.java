package com.wkz.viewer.listener;

import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.widget.FRScaleImageView;

/**
 * 监听图片浏览器的状态
 */
public interface OnPreviewStatusListener {

    /**
     * 监听图片预览器的当前状态
     *
     * @param state      图片预览器的当前状态
     * @param imagePager 当前的 itemView
     */
    void onPreviewStatus(@FRImageViewerState int state, FRScaleImageView imagePager);
}
