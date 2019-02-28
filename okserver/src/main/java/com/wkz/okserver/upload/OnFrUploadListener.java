package com.wkz.okserver.upload;

import com.wkz.okserver.OnOkProgressListener;

/**
 * 全局的上传监听
 */
public abstract class OnFrUploadListener<T> implements OnOkProgressListener<T> {

    public final Object tag;

    public OnFrUploadListener(Object tag) {
        this.tag = tag;
    }
}
