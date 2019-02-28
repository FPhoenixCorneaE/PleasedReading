package com.wkz.okserver;

import com.wkz.okgo.model.OkProgress;

public interface OnOkProgressListener<T> {
    /**
     * 成功添加任务的回调
     */
    void onStart(OkProgress progress);

    /**
     * 下载进行时回调
     */
    void onProgress(OkProgress progress);

    /**
     * 下载出错时回调
     */
    void onError(OkProgress progress);

    /**
     * 下载完成时回调
     */
    void onFinish(T t, OkProgress progress);

    /**
     * 被移除时回调
     */
    void onRemove(OkProgress progress);
}
