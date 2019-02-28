package com.wkz.okgo.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.SystemClock;

import com.wkz.okgo.OkGo;
import com.wkz.okgo.request.base.OkRequest;
import com.wkz.okgo.utils.OkIOUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OkProgress implements Serializable {
    private static final long serialVersionUID = -4645798210956294048L;

    public static final String TAG = "tag";
    public static final String URL = "url";
    public static final String FOLDER = "folder";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_NAME = "fileName";
    public static final String FRACTION = "fraction";
    public static final String TOTAL_SIZE = "totalSize";
    public static final String CURRENT_SIZE = "currentSize";
    public static final String STATUS = "status";
    public static final String PRIORITY = "priority";
    public static final String DATE = "date";
    public static final String REQUEST = "request";
    public static final String EXTRA1 = "extra1";
    public static final String EXTRA2 = "extra2";
    public static final String EXTRA3 = "extra3";

    public String tag;                                  //下载的标识键
    public String url;                                  //网址
    public String folder;                               //保存文件夹
    public String filePath;                             //保存文件地址
    public String fileName;                             //保存的文件名
    public float fraction;                              //下载的进度，0-1
    public long totalSize;                              //总字节长度, byte
    public long currentSize;                            //本次下载的大小, byte
    public transient long speed;                        //网速，byte/s
    public int status;                                  //当前状态
    public int priority;                                //任务优先级
    public long date;                                   //创建时间
    public OkRequest<?, ? extends OkRequest> request;   //网络请求
    public Serializable extra1;                         //额外的数据
    public Serializable extra2;                         //额外的数据
    public Serializable extra3;                         //额外的数据
    public Throwable exception;                         //当前进度出现的异常

    private transient long tempSize;                    //每一小段时间间隔的网络流量
    private transient long lastRefreshTime;             //最后一次刷新的时间
    private transient List<Long> speedBuffer;           //网速做平滑的缓存，避免抖动过快

    public OkProgress() {
        lastRefreshTime = SystemClock.elapsedRealtime();
        totalSize = 0;
        priority = OkPriority.DEFAULT;
        date = System.currentTimeMillis();
        speedBuffer = new ArrayList<>();
    }

    public static OkProgress changeProgress(OkProgress progress, long writeSize, Action action) {
        return changeProgress(progress, writeSize, progress.totalSize, action);
    }

    public static OkProgress changeProgress(final OkProgress progress, long writeSize, long totalSize, final Action action) {
        progress.totalSize = totalSize;
        progress.currentSize += writeSize;
        progress.tempSize += writeSize;

        long currentTime = SystemClock.elapsedRealtime();
        boolean isNotify = (currentTime - progress.lastRefreshTime) >= OkGo.REFRESH_TIME;
        if (isNotify || progress.currentSize == totalSize) {
            long diffTime = currentTime - progress.lastRefreshTime;
            if (diffTime == 0) diffTime = 1;
            progress.fraction = progress.currentSize * 1.0f / totalSize;
            progress.speed = progress.bufferSpeed(progress.tempSize * 1000 / diffTime);
            progress.lastRefreshTime = currentTime;
            progress.tempSize = 0;
            if (action != null) {
                action.call(progress);
            }
        }
        return progress;
    }

    /**
     * 平滑网速，避免抖动过大
     */
    private long bufferSpeed(long speed) {
        speedBuffer.add(speed);
        if (speedBuffer.size() > 10) {
            speedBuffer.remove(0);
        }
        long sum = 0;
        for (float speedTemp : speedBuffer) {
            sum += speedTemp;
        }
        return sum / speedBuffer.size();
    }

    /**
     * 转换进度信息
     */
    public void from(OkProgress progress) {
        totalSize = progress.totalSize;
        currentSize = progress.currentSize;
        fraction = progress.fraction;
        speed = progress.speed;
        lastRefreshTime = progress.lastRefreshTime;
        tempSize = progress.tempSize;
    }

    public interface Action {
        void call(OkProgress progress);
    }

    public static ContentValues buildContentValues(OkProgress progress) {
        ContentValues values = new ContentValues();
        values.put(TAG, progress.tag);
        values.put(URL, progress.url);
        values.put(FOLDER, progress.folder);
        values.put(FILE_PATH, progress.filePath);
        values.put(FILE_NAME, progress.fileName);
        values.put(FRACTION, progress.fraction);
        values.put(TOTAL_SIZE, progress.totalSize);
        values.put(CURRENT_SIZE, progress.currentSize);
        values.put(STATUS, progress.status);
        values.put(PRIORITY, progress.priority);
        values.put(DATE, progress.date);
        values.put(REQUEST, OkIOUtils.toByteArray(progress.request));
        values.put(EXTRA1, OkIOUtils.toByteArray(progress.extra1));
        values.put(EXTRA2, OkIOUtils.toByteArray(progress.extra2));
        values.put(EXTRA3, OkIOUtils.toByteArray(progress.extra3));
        return values;
    }

    public static ContentValues buildUpdateContentValues(OkProgress progress) {
        ContentValues values = new ContentValues();
        values.put(FRACTION, progress.fraction);
        values.put(TOTAL_SIZE, progress.totalSize);
        values.put(CURRENT_SIZE, progress.currentSize);
        values.put(STATUS, progress.status);
        values.put(PRIORITY, progress.priority);
        values.put(DATE, progress.date);
        return values;
    }

    public static OkProgress parseCursorToBean(Cursor cursor) {
        OkProgress progress = new OkProgress();
        progress.tag = cursor.getString(cursor.getColumnIndex(OkProgress.TAG));
        progress.url = cursor.getString(cursor.getColumnIndex(OkProgress.URL));
        progress.folder = cursor.getString(cursor.getColumnIndex(OkProgress.FOLDER));
        progress.filePath = cursor.getString(cursor.getColumnIndex(OkProgress.FILE_PATH));
        progress.fileName = cursor.getString(cursor.getColumnIndex(OkProgress.FILE_NAME));
        progress.fraction = cursor.getFloat(cursor.getColumnIndex(OkProgress.FRACTION));
        progress.totalSize = cursor.getLong(cursor.getColumnIndex(OkProgress.TOTAL_SIZE));
        progress.currentSize = cursor.getLong(cursor.getColumnIndex(OkProgress.CURRENT_SIZE));
        progress.status = cursor.getInt(cursor.getColumnIndex(OkProgress.STATUS));
        progress.priority = cursor.getInt(cursor.getColumnIndex(OkProgress.PRIORITY));
        progress.date = cursor.getLong(cursor.getColumnIndex(OkProgress.DATE));
        progress.request = (OkRequest<?, ? extends OkRequest>) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(OkProgress.REQUEST)));
        progress.extra1 = (Serializable) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(OkProgress.EXTRA1)));
        progress.extra2 = (Serializable) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(OkProgress.EXTRA2)));
        progress.extra3 = (Serializable) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(OkProgress.EXTRA3)));
        return progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OkProgress progress = (OkProgress) o;
        return tag != null ? tag.equals(progress.tag) : progress.tag == null;

    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OkProgress{" +//
                "fraction=" + fraction +//
                ", totalSize=" + totalSize +//
                ", currentSize=" + currentSize +//
                ", speed=" + speed +//
                ", status=" + status +//
                ", priority=" + priority +//
                ", folder=" + folder +//
                ", filePath=" + filePath +//
                ", fileName=" + fileName +//
                ", tag=" + tag +//
                ", url=" + url +//
                '}';
    }
}
