package com.wkz.okserver.download;

import com.wkz.okserver.OnOkProgressListener;

import java.io.File;

/**
 * 全局的下载监听
 */
public abstract class OnOkDownloadListener implements OnOkProgressListener<File> {

    public final Object tag;

    public OnOkDownloadListener(Object tag) {
        this.tag = tag;
    }
}
