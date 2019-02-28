package com.wkz.okgo.callback;

import com.wkz.okgo.converter.OkFileConverter;

import java.io.File;

import okhttp3.Response;

/**
 * 文件的回调下载进度监听
 */
public abstract class OkFileCallback extends OkAbsCallback<File> {

    private OkFileConverter convert;    //文件转换类

    public OkFileCallback() {
        this(null);
    }

    public OkFileCallback(String destFileName) {
        this(null, destFileName);
    }

    public OkFileCallback(String destFileDir, String destFileName) {
        convert = new OkFileConverter(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
