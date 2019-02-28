package com.wkz.okserver;

import com.wkz.okgo.db.dao.OkUploadDao;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkLogger;
import com.wkz.okserver.task.OkThreadPoolExecutor;
import com.wkz.okserver.upload.OkUploadTask;
import com.wkz.okserver.upload.OkUploadThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局的上传管理
 */
public class OkUpload {

    private Map<String, OkUploadTask<?>> taskMap;         //所有任务
    private OkUploadThreadPool threadPool;                //上传的线程池

    public static OkUpload getInstance() {
        return OkUploadHolder.instance;
    }

    private static class OkUploadHolder {
        private static final OkUpload instance = new OkUpload();
    }

    private OkUpload() {
        threadPool = new OkUploadThreadPool();
        taskMap = new LinkedHashMap<>();

        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<OkProgress> taskList = OkUploadDao.getInstance().getUploading();
        for (OkProgress info : taskList) {
            if (info.status == OkProgressState.WAITING ||
                    info.status == OkProgressState.LOADING ||
                    info.status == OkProgressState.PAUSE) {
                info.status = OkProgressState.NONE;
            }
        }
        OkUploadDao.getInstance().replace(taskList);
    }

    public static <T> OkUploadTask<T> request(String tag, OkRequest<T, ? extends OkRequest> request) {
        Map<String, OkUploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        //noinspection unchecked
        OkUploadTask<T> task = (OkUploadTask<T>) taskMap.get(tag);
        if (task == null) {
            task = new OkUploadTask<>(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<OkUploadTask<?>> restore() {
        return restore(OkUploadDao.getInstance().getAll());
    }

    /**
     * 从数据库中恢复任务
     */
    public static <T> OkUploadTask<T> restore(OkProgress progress) {
        Map<String, OkUploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        //noinspection unchecked
        OkUploadTask<T> task = (OkUploadTask<T>) taskMap.get(progress.tag);
        if (task == null) {
            task = new OkUploadTask<>(progress);
            taskMap.put(progress.tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<OkUploadTask<?>> restore(List<OkProgress> progressList) {
        Map<String, OkUploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        List<OkUploadTask<?>> tasks = new ArrayList<>();
        for (OkProgress progress : progressList) {
            OkUploadTask<?> task = taskMap.get(progress.tag);
            if (task == null) {
                task = new OkUploadTask<>(progress);
                taskMap.put(progress.tag, task);
            }
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * 开始所有任务
     */
    public void startAll() {
        for (Map.Entry<String, OkUploadTask<?>> entry : taskMap.entrySet()) {
            OkUploadTask<?> task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            task.start();
        }
    }

    /**
     * 暂停全部任务
     */
    public void pauseAll() {
        //先停止未开始的任务
        for (Map.Entry<String, OkUploadTask<?>> entry : taskMap.entrySet()) {
            OkUploadTask<?> task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != OkProgressState.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, OkUploadTask<?>> entry : taskMap.entrySet()) {
            OkUploadTask<?> task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == OkProgressState.LOADING) {
                task.pause();
            }
        }
    }

    /**
     * 删除所有任务
     */
    public void removeAll() {
        Map<String, OkUploadTask<?>> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, OkUploadTask<?>> entry : map.entrySet()) {
            OkUploadTask<?> task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != OkProgressState.LOADING) {
                task.remove();
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, OkUploadTask<?>> entry : map.entrySet()) {
            OkUploadTask<?> task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == OkProgressState.LOADING) {
                task.remove();
            }
        }
    }

    public OkUploadThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, OkUploadTask<?>> getTaskMap() {
        return taskMap;
    }

    public OkUploadTask<?> getTask(String tag) {
        return taskMap.get(tag);
    }

    public boolean hasTask(String tag) {
        return taskMap.containsKey(tag);
    }

    public OkUploadTask<?> removeTask(String tag) {
        return taskMap.remove(tag);
    }

    public void addOnAllTaskEndListener(OkThreadPoolExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(OkThreadPoolExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }
}
