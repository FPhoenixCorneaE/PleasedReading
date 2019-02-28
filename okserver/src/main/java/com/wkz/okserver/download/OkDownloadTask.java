package com.wkz.okserver.download;

import android.content.ContentValues;
import android.text.TextUtils;

import com.wkz.okgo.db.dao.OkDownloadDao;
import com.wkz.okgo.exception.OkFileException;
import com.wkz.okgo.exception.OkHttpException;
import com.wkz.okgo.exception.OkStorageException;
import com.wkz.okgo.model.OkHttpHeaders;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkHttpUtils;
import com.wkz.okgo.utils.OkIOUtils;
import com.wkz.okgo.utils.OkLogger;
import com.wkz.okserver.OkDownload;
import com.wkz.okserver.task.OkPriorityRunnable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载任务类
 */
public class OkDownloadTask implements Runnable {

    private static final int BUFFER_SIZE = 1024 * 8;

    public OkProgress progress;
    public Map<Object, OnOkDownloadListener> listeners;
    private ThreadPoolExecutor executor;
    private OkPriorityRunnable priorityRunnable;

    public OkDownloadTask(String tag, OkRequest<File, ? extends OkRequest> request) {
        OkHttpUtils.checkNotNull(tag, "tag == null");
        progress = new OkProgress();
        progress.tag = tag;
        progress.folder = OkDownload.getInstance().getFolder();
        progress.url = request.getBaseUrl();
        progress.status = OkProgressState.NONE;
        progress.totalSize = -1;
        progress.request = request;

        executor = OkDownload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public OkDownloadTask(OkProgress progress) {
        OkHttpUtils.checkNotNull(progress, "progress == null");
        this.progress = progress;
        executor = OkDownload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public OkDownloadTask folder(String folder) {
        if (folder != null && !TextUtils.isEmpty(folder.trim())) {
            progress.folder = folder;
        } else {
            OkLogger.w("folder is null, ignored!");
        }
        return this;
    }

    public OkDownloadTask fileName(String fileName) {
        if (fileName != null && !TextUtils.isEmpty(fileName.trim())) {
            progress.fileName = fileName;
        } else {
            OkLogger.w("fileName is null, ignored!");
        }
        return this;
    }

    public OkDownloadTask priority(int priority) {
        progress.priority = priority;
        return this;
    }

    public OkDownloadTask extra1(Serializable extra1) {
        progress.extra1 = extra1;
        return this;
    }

    public OkDownloadTask extra2(Serializable extra2) {
        progress.extra2 = extra2;
        return this;
    }

    public OkDownloadTask extra3(Serializable extra3) {
        progress.extra3 = extra3;
        return this;
    }

    public OkDownloadTask save() {
        if (!TextUtils.isEmpty(progress.folder) && !TextUtils.isEmpty(progress.fileName)) {
            progress.filePath = new File(progress.folder, progress.fileName).getAbsolutePath();
        }
        OkDownloadDao.getInstance().replace(progress);
        return this;
    }

    public OkDownloadTask register(OnOkDownloadListener listener) {
        if (listener != null) {
            listeners.put(listener.tag, listener);
        }
        return this;
    }

    public void unRegister(OnOkDownloadListener listener) {
        OkHttpUtils.checkNotNull(listener, "listener == null");
        listeners.remove(listener.tag);
    }

    public void unRegister(String tag) {
        OkHttpUtils.checkNotNull(tag, "tag == null");
        listeners.remove(tag);
    }

    public void start() {
        if (OkDownload.getInstance().getTask(progress.tag) == null || OkDownloadDao.getInstance().get(progress.tag) == null) {
            throw new IllegalStateException("you must call OkDownloadTask#save() before OkDownloadTask#start()！");
        }
        if (progress.status == OkProgressState.NONE
                || progress.status == OkProgressState.PAUSE
                || progress.status == OkProgressState.ERROR) {
            postOnStart(progress);
            postWaiting(progress);
            priorityRunnable = new OkPriorityRunnable(progress.priority, this);
            executor.execute(priorityRunnable);
        } else if (progress.status == OkProgressState.FINISH) {
            if (progress.filePath == null) {
                postOnError(progress, new OkStorageException("the file of the task with tag:" + progress.tag + " may be invalid or damaged, please call the method restart() to download again！"));
            } else {
                File file = new File(progress.filePath);
                if (file.exists() && file.length() == progress.totalSize) {
                    postOnFinish(progress, new File(progress.filePath));
                } else {
                    postOnError(progress, new OkStorageException("the file " + progress.filePath + " may be invalid or damaged, please call the method restart() to download again！"));
                }
            }
        } else {
            OkLogger.w("the task with tag " + progress.tag + " is already in the download queue, current task status is " + progress.status);
        }
    }

    /**
     * 重新下载
     */
    public void restart() {
        pause();
        OkIOUtils.delFileOrFolder(progress.filePath);
        progress.status = OkProgressState.NONE;
        progress.currentSize = 0;
        progress.fraction = 0;
        progress.speed = 0;
        OkDownloadDao.getInstance().replace(progress);
        start();
    }

    /**
     * 暂停的方法
     */
    public void pause() {
        executor.remove(priorityRunnable);
        if (progress.status == OkProgressState.WAITING) {
            postPause(progress);
        } else if (progress.status == OkProgressState.LOADING) {
            progress.speed = 0;
            progress.status = OkProgressState.PAUSE;
        } else {
            OkLogger.w("only the task with status WAITING(1) or LOADING(2) can pause, current status is " + progress.status);
        }
    }

    /**
     * 删除一个任务,会删除下载文件
     */
    public void remove() {
        remove(false);
    }

    /**
     * 删除一个任务,会删除下载文件
     */
    public OkDownloadTask remove(boolean isDeleteFile) {
        pause();
        if (isDeleteFile) OkIOUtils.delFileOrFolder(progress.filePath);
        OkDownloadDao.getInstance().delete(progress.tag);
        OkDownloadTask task = OkDownload.getInstance().removeTask(progress.tag);
        postOnRemove(progress);
        return task;
    }

    @Override
    public void run() {
        //check breakpoint
        long startPosition = progress.currentSize;
        if (startPosition < 0) {
            postOnError(progress, OkFileException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition > 0) {
            if (!TextUtils.isEmpty(progress.filePath)) {
                File file = new File(progress.filePath);
                if (!file.exists()) {
                    postOnError(progress, OkFileException.BREAKPOINT_NOT_EXIST());
                    return;
                }
            }
        }

        //request network from startPosition
        Response response;
        try {
            OkRequest<?, ? extends OkRequest> request = progress.request;
            request.headers(OkHttpHeaders.HEAD_KEY_RANGE, "bytes=" + startPosition + "-");
            response = request.execute();
        } catch (IOException e) {
            postOnError(progress, e);
            return;
        }

        //check network data
        int code = response.code();
        if (code == 404 || code >= 500) {
            postOnError(progress, OkHttpException.NET_ERROR());
            return;
        }
        ResponseBody body = response.body();
        if (body == null) {
            postOnError(progress, new OkHttpException("response body is null"));
            return;
        }
        if (progress.totalSize == -1) {
            progress.totalSize = body.contentLength();
        }

        //create filename
        String fileName = progress.fileName;
        if (TextUtils.isEmpty(fileName)) {
            fileName = OkHttpUtils.getNetFileName(response, progress.url);
            progress.fileName = fileName;
        }
        if (!OkIOUtils.createFolder(progress.folder)) {
            postOnError(progress, OkStorageException.NOT_AVAILABLE());
            return;
        }

        //create and check file
        File file;
        if (TextUtils.isEmpty(progress.filePath)) {
            file = new File(progress.folder, fileName);
            progress.filePath = file.getAbsolutePath();
        } else {
            file = new File(progress.filePath);
        }
        if (startPosition > 0 && !file.exists()) {
            postOnError(progress, OkFileException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition > progress.totalSize) {
            postOnError(progress, OkFileException.BREAKPOINT_EXPIRED());
            return;
        }
        if (startPosition == 0 && file.exists()) {
            OkIOUtils.delFileOrFolder(file);
        }
        if (startPosition == progress.totalSize && startPosition > 0) {
            if (file.exists() && startPosition == file.length()) {
                postOnFinish(progress, file);
                return;
            } else {
                postOnError(progress, OkFileException.BREAKPOINT_EXPIRED());
                return;
            }
        }

        //start downloading
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPosition);
            progress.currentSize = startPosition;
        } catch (Exception e) {
            postOnError(progress, e);
            return;
        }
        try {
            OkDownloadDao.getInstance().replace(progress);
            download(body.byteStream(), randomAccessFile, progress);
        } catch (IOException e) {
            postOnError(progress, e);
            return;
        }

        //check finish status
        if (progress.status == OkProgressState.PAUSE) {
            postPause(progress);
        } else if (progress.status == OkProgressState.LOADING) {
            if (file.length() == progress.totalSize) {
                postOnFinish(progress, file);
            } else {
                postOnError(progress, OkFileException.BREAKPOINT_EXPIRED());
            }
        } else {
            postOnError(progress, OkFileException.UNKNOWN());
        }
    }

    /**
     * 执行文件下载
     */
    private void download(InputStream input, RandomAccessFile out, OkProgress progress) throws IOException {
        if (input == null || out == null) return;

        progress.status = OkProgressState.LOADING;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        int len;
        try {
            while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1 && progress.status == OkProgressState.LOADING) {
                out.write(buffer, 0, len);

                OkProgress.changeProgress(progress, len, progress.totalSize, new OkProgress.Action() {
                    @Override
                    public void call(OkProgress progress) {
                        postLoading(progress);
                    }
                });
            }
        } finally {
            OkIOUtils.closeQuietly(out);
            OkIOUtils.closeQuietly(in);
            OkIOUtils.closeQuietly(input);
        }
    }

    private void postOnStart(final OkProgress progress) {
        progress.speed = 0;
        progress.status = OkProgressState.NONE;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onStart(progress);
                }
            }
        });
    }

    private void postWaiting(final OkProgress progress) {
        progress.speed = 0;
        progress.status = OkProgressState.WAITING;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postPause(final OkProgress progress) {
        progress.speed = 0;
        progress.status = OkProgressState.PAUSE;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postLoading(final OkProgress progress) {
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postOnError(final OkProgress progress, final Throwable throwable) {
        progress.speed = 0;
        progress.status = OkProgressState.ERROR;
        progress.exception = throwable;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onError(progress);
                }
            }
        });
    }

    private void postOnFinish(final OkProgress progress, final File file) {
        progress.speed = 0;
        progress.fraction = 1.0f;
        progress.status = OkProgressState.FINISH;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onFinish(file, progress);
                }
            }
        });
    }

    private void postOnRemove(final OkProgress progress) {
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnOkDownloadListener listener : listeners.values()) {
                    listener.onRemove(progress);
                }
                listeners.clear();
            }
        });
    }

    private void updateDatabase(OkProgress progress) {
        ContentValues contentValues = OkProgress.buildUpdateContentValues(progress);
        OkDownloadDao.getInstance().update(contentValues, progress.tag);
    }
}
