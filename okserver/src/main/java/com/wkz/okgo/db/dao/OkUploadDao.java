package com.wkz.okgo.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.wkz.okgo.db.OkDBHelper;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;

import java.util.List;

public class OkUploadDao extends OkBaseDao<OkProgress> {

    private OkUploadDao() {
        super(new OkDBHelper());
    }

    private static class OkUploadDaoHolder {
        private static final OkUploadDao instance = new OkUploadDao();
    }

    public static OkUploadDao getInstance() {
        return OkUploadDaoHolder.instance;
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
        return OkDBHelper.TABLE_UPLOAD;
    }

    @Override
    public void unInit() {
    }

    /**
     * 获取上传任务
     */
    public OkProgress get(String tag) {
        return queryOne(OkProgress.TAG + "=?", new String[]{tag});
    }

    /**
     * 移除上传任务
     */
    public void delete(String taskKey) {
        delete(OkProgress.TAG + "=?", new String[]{taskKey});
    }

    /**
     * 更新上传任务
     */
    public boolean update(OkProgress progress) {
        return update(progress, OkProgress.TAG + "=?", new String[]{progress.tag});
    }

    /**
     * 更新上传任务
     */
    public boolean update(ContentValues contentValues, String tag) {
        return update(contentValues, OkProgress.TAG + "=?", new String[]{tag});
    }

    /**
     * 获取所有上传信息
     */
    public List<OkProgress> getAll() {
        return query(null, null, null, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 获取所有上传信息
     */
    public List<OkProgress> getFinished() {
        return query(null, "status=?", new String[]{OkProgressState.FINISH + ""}, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 获取所有上传信息
     */
    public List<OkProgress> getUploading() {
        return query(null, "status not in(?)", new String[]{OkProgressState.FINISH + ""}, null, null, OkProgress.DATE + " ASC", null);
    }

    /**
     * 清空上传任务
     */
    public boolean clear() {
        return deleteAll();
    }
}
