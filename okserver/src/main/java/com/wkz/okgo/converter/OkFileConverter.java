package com.wkz.okgo.converter;

import android.os.Environment;
import android.text.TextUtils;

import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.utils.OkHttpUtils;
import com.wkz.okgo.utils.OkIOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 文件转换类
 */
public class OkFileConverter implements IOkConverter<File> {

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹

    private String folder;                  //目标文件存储的文件夹路径
    private String fileName;                //目标文件存储的文件名
    private IOkCallback<File> callback;        //下载回调

    public OkFileConverter() {
        this(null);
    }

    public OkFileConverter(String fileName) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, fileName);
    }

    public OkFileConverter(String folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
    }

    public void setCallback(IOkCallback<File> callback) {
        this.callback = callback;
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        String url = response.request().url().toString();
        if (TextUtils.isEmpty(folder))
            folder = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        if (TextUtils.isEmpty(fileName)) fileName = OkHttpUtils.getNetFileName(response, url);

        File dir = new File(folder);
        OkIOUtils.createFolder(dir);
        File file = new File(dir, fileName);
        OkIOUtils.delFileOrFolder(file);

        InputStream bodyStream = null;
        byte[] buffer = new byte[8192];
        FileOutputStream fileOutputStream = null;
        try {
            ResponseBody body = response.body();
            if (body == null) return null;

            bodyStream = body.byteStream();
            OkProgress progress = new OkProgress();
            progress.totalSize = body.contentLength();
            progress.fileName = fileName;
            progress.filePath = file.getAbsolutePath();
            progress.status = OkProgressState.LOADING;
            progress.url = url;
            progress.tag = url;

            int len;
            fileOutputStream = new FileOutputStream(file);
            while ((len = bodyStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);

                if (callback == null) continue;
                OkProgress.changeProgress(progress, len, new OkProgress.Action() {
                    @Override
                    public void call(OkProgress progress) {
                        onProgress(progress);
                    }
                });
            }
            fileOutputStream.flush();
            return file;
        } finally {
            OkIOUtils.closeQuietly(bodyStream);
            OkIOUtils.closeQuietly(fileOutputStream);
        }
    }

    private void onProgress(final OkProgress progress) {
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.downloadProgress(progress);   //进度回调的方法
            }
        });
    }
}
