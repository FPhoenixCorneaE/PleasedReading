package com.wkz.okserver.upload;

import android.content.ContentValues;

import com.wkz.okgo.db.dao.OkUploadDao;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkProgressRequestBody;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkHttpUtils;
import com.wkz.okgo.utils.OkLogger;
import com.wkz.okserver.OkUpload;
import com.wkz.okserver.task.OkPriorityRunnable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Call;

/**
 * 上传任务类
 */
public class OkUploadTask<T> implements Runnable {

    public OkProgress progress;
    public Map<Object, OnFrUploadListener<T>> listeners;
    private ThreadPoolExecutor executor;
    private OkPriorityRunnable priorityRunnable;

    public OkUploadTask(String tag, OkRequest<T, ? extends OkRequest> request) {
        OkHttpUtils.checkNotNull(tag, "tag == null");
        progress = new OkProgress();
        progress.tag = tag;
        progress.url = request.getBaseUrl();
        progress.status = OkProgressState.NONE;
        progress.totalSize = -1;
        progress.request = request;

        executor = OkUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public OkUploadTask(OkProgress progress) {
        OkHttpUtils.checkNotNull(progress, "progress == null");
        this.progress = progress;
        executor = OkUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public OkUploadTask<T> priority(int priority) {
        progress.priority = priority;
        return this;
    }

    public OkUploadTask<T> extra1(Serializable extra1) {
        progress.extra1 = extra1;
        return this;
    }

    public OkUploadTask<T> extra2(Serializable extra2) {
        progress.extra2 = extra2;
        return this;
    }

    public OkUploadTask<T> extra3(Serializable extra3) {
        progress.extra3 = extra3;
        return this;
    }

    public OkUploadTask<T> save() {
        OkUploadDao.getInstance().replace(progress);
        return this;
    }

    public OkUploadTask<T> register(OnFrUploadListener<T> listener) {
        if (listener != null) {
            listeners.put(listener.tag, listener);
        }
        return this;
    }

    public void unRegister(OnFrUploadListener<T> listener) {
        OkHttpUtils.checkNotNull(listener, "listener == null");
        listeners.remove(listener.tag);
    }

    public void unRegister(String tag) {
        OkHttpUtils.checkNotNull(tag, "tag == null");
        listeners.remove(tag);
    }

    public OkUploadTask<T> start() {
        if (OkUpload.getInstance().getTask(progress.tag) == null || OkUploadDao.getInstance().get(progress.tag) == null) {
            throw new IllegalStateException("you must call OkUploadTask#save() before OkUploadTask#start()！");
        }
        if (progress.status != OkProgressState.WAITING &&
                progress.status != OkProgressState.LOADING) {
            postOnStart(progress);
            postWaiting(progress);
            priorityRunnable = new OkPriorityRunnable(progress.priority, this);
            executor.execute(priorityRunnable);
        } else {
            OkLogger.w("the task with tag " + progress.tag + " is already in the upload queue, current task status is " + progress.status);
        }
        return this;
    }

    public void restart() {
        pause();
        progress.status = OkProgressState.NONE;
        progress.currentSize = 0;
        progress.fraction = 0;
        progress.speed = 0;
        OkUploadDao.getInstance().replace(progress);
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
    public OkUploadTask<T> remove() {
        pause();
        OkUploadDao.getInstance().delete(progress.tag);
        //noinspection unchecked
        OkUploadTask<T> task = (OkUploadTask<T>) OkUpload.getInstance().removeTask(progress.tag);
        postOnRemove(progress);
        return task;
    }

    @Override
    public void run() {
        progress.status = OkProgressState.LOADING;
        postLoading(progress);
        final OkResponse<T> response;
        try {
            //noinspection unchecked
            OkRequest<T, ? extends OkRequest> request = (OkRequest<T, ? extends OkRequest>) progress.request;
            final Call rawCall = request.getRawCall();
            request.uploadInterceptor(new OkProgressRequestBody.UploadInterceptor() {
                @Override
                public void uploadProgress(OkProgress innerProgress) {
                    if (rawCall.isCanceled()) return;
                    if (progress.status != OkProgressState.LOADING) {
                        rawCall.cancel();
                        return;
                    }
                    progress.from(innerProgress);
                    postLoading(progress);
                }
            });
            response = request.adapt().execute();
        } catch (Exception e) {
            postOnError(progress, e);
            return;
        }

        if (response.isSuccessful()) {
            postOnFinish(progress, response.body());
        } else {
            postOnError(progress, response.getException());
        }
    }

    private void postOnStart(final OkProgress progress) {
        progress.speed = 0;
        progress.status = OkProgressState.NONE;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnFrUploadListener<T> listener : listeners.values()) {
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
                for (OnFrUploadListener<T> listener : listeners.values()) {
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
                for (OnFrUploadListener<T> listener : listeners.values()) {
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
                for (OnFrUploadListener<T> listener : listeners.values()) {
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
                for (OnFrUploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onError(progress);
                }
            }
        });
    }

    private void postOnFinish(final OkProgress progress, final T t) {
        progress.speed = 0;
        progress.fraction = 1.0f;
        progress.status = OkProgressState.FINISH;
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnFrUploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onFinish(t, progress);
                }
            }
        });
    }

    private void postOnRemove(final OkProgress progress) {
        updateDatabase(progress);
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnFrUploadListener<T> listener : listeners.values()) {
                    listener.onRemove(progress);
                }
                listeners.clear();
            }
        });
    }

    private void updateDatabase(OkProgress progress) {
        ContentValues contentValues = OkProgress.buildUpdateContentValues(progress);
        OkUploadDao.getInstance().update(contentValues, progress.tag);
    }
}
