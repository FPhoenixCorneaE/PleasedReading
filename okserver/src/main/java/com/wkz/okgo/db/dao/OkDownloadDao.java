package com.wkz.okgo.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.wkz.okgo.db.OkDBHelper;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;

import java.util.List;

public class OkDownloadDao extends OkBaseDao<OkProgress> {

    private OkDownloadDao() {
        super(new OkDBHelper());
    }

    private static class OkDownloadDaoHolder {
        private static final OkDownloadDao instance = new OkDownloadDao();
    }

    public static OkDownloadDao getInstance() {
        return OkDownloadDaoHolder.instance;
    }

    @Override
    public OkProgress parseCursorToBean(Cursor cursor) {
        return OkProgress.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(OkProgress progress) {
        return OkProgress.buildContentValues(progress);
    }

    @Override
    public String getTableName() {
        return OkDBHelper.TABLE_DOWNLOAD;
    }

    @Override
    public void unInit() {
    }

    /**
     * 获取下载任务
     */
    public OkProgress get(String tag) {
        return queryOne(OkProgress.TAG + "=?", new String[]{tag});
    }

    /**
     * 移除下载任务
     */
    public void delete(String taskKey) {
        delete(OkProgress.TAG + "=?", new String[]{taskKey});
    }

    /**
     * 更新下载任务
     */
    public boolean update(OkProgress progress) {
        return update(progress, OkProgress.TAG + "=?", new String[]{progress.tag});
    }

    /**
     * 更新下载任务
     */
    public boolean update(ContentValues contentValues, String tag) {
        return update(contentValues, OkProgress.TAG + "=?", new String[]{tag});
    }

    /**
     * 获取所有下载信息
     */
    public List<OkProgress> getAll() {
        return query(null, null, null, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 获取所有下载信息
     */
    public List<OkProgress> getFinished() {
        return query(null, "status=?", new String[]{OkProgressState.FINISH + ""}, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 获取所有下载信息
     */
    public List<OkProgress> getDownloading() {
        return query(null, "status not in(?)", new String[]{OkProgressState.FINISH + ""}, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 清空下载任务
     */
    public boolean clear() {
        return deleteAll();
    }
}
