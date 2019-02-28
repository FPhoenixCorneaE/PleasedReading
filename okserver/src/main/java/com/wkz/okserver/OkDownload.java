package com.wkz.okserver;

import android.os.Environment;

import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okserver.download.OkDownloadTask;
import com.wkz.okserver.download.OkDownloadThreadPool;
import com.wkz.okgo.db.dao.OkDownloadDao;
import com.wkz.okgo.utils.OkIOUtils;
import com.wkz.okgo.utils.OkLogger;
import com.wkz.okserver.task.OkThreadPoolExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局的下载管理类
 */
public class OkDownload {

    private String folder;                                      //下载的默认文件夹
    private OkDownloadThreadPool threadPool;                      //下载的线程池
    private ConcurrentHashMap<String, OkDownloadTask> taskMap;    //所有任务

    public static OkDownload getInstance() {
        return OkDownloadHolder.instance;
    }

    private static class OkDownloadHolder {
        private static final OkDownload instance = new OkDownload();
    }

    private OkDownload() {
        folder = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;
        OkIOUtils.createFolder(folder);
        threadPool = new OkDownloadThreadPool();
        taskMap = new ConcurrentHashMap<>();

        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<OkProgress> taskList = OkDownloadDao.getInstance().getDownloading();
        for (OkProgress info : taskList) {
            if (info.status == OkProgressState.WAITING ||
                    info.status == OkProgressState.LOADING ||
                    info.status == OkProgressState.PAUSE) {
                info.status = OkProgressState.NONE;
            }
        }
        OkDownloadDao.getInstance().replace(taskList);
    }

    public static OkDownloadTask request(String tag, OkRequest<File, ? extends OkRequest> request) {
        Map<String, OkDownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        OkDownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new OkDownloadTask(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<OkDownloadTask> restore() {
        return restore(OkDownloadDao.getInstance().getAll());
    }

    /**
     * 从数据库中恢复任务
     */
    public static OkDownloadTask restore(OkProgress progress) {
        Map<String, OkDownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        OkDownloadTask task = taskMap.get(progress.tag);
        if (task == null) {
            task = new OkDownloadTask(progress);
            taskMap.put(progress.tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<OkDownloadTask> restore(List<OkProgress> progressList) {
        Map<String, OkDownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        List<OkDownloadTask> tasks = new ArrayList<>();
        for (OkProgress progress : progressList) {
            OkDownloadTask task = taskMap.get(progress.tag);
            if (task == null) {
                task = new OkDownloadTask(progress);
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
        for (Map.Entry<String, OkDownloadTask> entry : taskMap.entrySet()) {
            OkDownloadTask task = entry.getValue();
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
        for (Map.Entry<String, OkDownloadTask> entry : taskMap.entrySet()) {
            OkDownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != OkProgressState.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, OkDownloadTask> entry : taskMap.entrySet()) {
            OkDownloadTask task = entry.getValue();
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
        removeAll(false);
    }

    /**
     * 删除所有任务
     *
     * @param isDeleteFile 删除任务是否删除文件
     */
    public void removeAll(boolean isDeleteFile) {
        Map<String, OkDownloadTask> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, OkDownloadTask> entry : map.entrySet()) {
            OkDownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != OkProgressState.LOADING) {
                task.remove(isDeleteFile);
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, OkDownloadTask> entry : map.entrySet()) {
            OkDownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == OkProgressState.LOADING) {
                task.remove(isDeleteFile);
            }
        }
    }

    /**
     * 设置下载目录
     */
    public String getFolder() {
        return folder;
    }

    public OkDownload setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public OkDownloadThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, OkDownloadTask> getTaskMap() {
        return taskMap;
    }

    public OkDownloadTask getTask(String tag) {
        return taskMap.get(tag);
    }

    public boolean hasTask(String tag) {
        return taskMap.containsKey(tag);
    }

    public OkDownloadTask removeTask(String tag) {
        return taskMap.remove(tag);
    }

    public void addOnAllTaskEndListener(OkThreadPoolExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(OkThreadPoolExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }
}
