package com.wkz.viewer.dragger;

/**
 * 监听图片被拖拽时的状态
 */
public interface OnImageDraggerStateListener {

    /**
     * @param state {@link FRImageDraggerState}
     */
    void onImageDraggerState(int state);
}
