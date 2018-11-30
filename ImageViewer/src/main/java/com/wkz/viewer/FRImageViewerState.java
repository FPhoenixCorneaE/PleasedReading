package com.wkz.viewer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 图片预览器的状态
 */
@IntDef({FRImageViewerState.STATE_READY_OPEN,
        FRImageViewerState.STATE_OPENING,
        FRImageViewerState.STATE_COMPLETE_OPEN,
        FRImageViewerState.STATE_WATCHING,
        FRImageViewerState.STATE_READY_CLOSE,
        FRImageViewerState.STATE_CLOSING,
        FRImageViewerState.STATE_COMPLETE_CLOSE,
        FRImageViewerState.STATE_SILENCE,
        FRImageViewerState.STATE_DRAGGING,
        FRImageViewerState.STATE_READY_REBACK,
        FRImageViewerState.STATE_REBACKING,
        FRImageViewerState.STATE_COMPLETE_REBACK})
@Retention(RetentionPolicy.SOURCE)
public @interface FRImageViewerState {
    /**
     * 准备打开图片预览器
     */
    int STATE_READY_OPEN = 1;
    /**
     * 图片预览器打开中
     */
    int STATE_OPENING = 2;
    /**
     * 图片预览器打开完成
     */
    int STATE_COMPLETE_OPEN = 3;
    /**
     * 图片正在被预览中
     */
    int STATE_WATCHING = 4;
    /**
     * 准备关闭图片预览器
     */
    int STATE_READY_CLOSE = 5;
    /**
     * 图片预览器关闭中
     */
    int STATE_CLOSING = 6;
    /**
     * 关闭图片预览器完成
     */
    int STATE_COMPLETE_CLOSE = 7;
    /**
     * 图片预览器处于未开启状态
     */
    int STATE_SILENCE = 8;
    /**
     * 图片正在被拖拽中
     */
    int STATE_DRAGGING = 9;
    /**
     * 准备将图片恢复原样
     */
    int STATE_READY_REBACK = 10;
    /**
     * 图片正在复位中
     */
    int STATE_REBACKING = 11;
    /**
     * 图片恢复原样完成
     */
    int STATE_COMPLETE_REBACK = 12;
}
