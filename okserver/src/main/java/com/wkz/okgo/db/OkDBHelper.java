package com.wkz.okgo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wkz.okgo.OkGo;
import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.cookie.OkSerializableCookie;
import com.wkz.okgo.model.OkProgress;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据库帮助类
 */
public class OkDBHelper extends SQLiteOpenHelper {

    private static final String DB_CACHE_NAME = "okserver.db";
    private static final int DB_CACHE_VERSION = 1;
    public static final String TABLE_CACHE = "cache";
    public static final String TABLE_COOKIE = "cookie";
    public static final String TABLE_DOWNLOAD = "download";
    public static final String TABLE_UPLOAD = "upload";

    public static final Lock lock = new ReentrantLock();

    private OkTableEntity cacheTableEntity = new OkTableEntity(TABLE_CACHE);
    private OkTableEntity cookieTableEntity = new OkTableEntity(TABLE_COOKIE);
    private OkTableEntity downloadTableEntity = new OkTableEntity(TABLE_DOWNLOAD);
    private OkTableEntity uploadTableEntity = new OkTableEntity(TABLE_UPLOAD);

    public OkDBHelper() {
        this(OkGo.getInstance().getContext());
    }

    public OkDBHelper(Context context) {
        super(context, DB_CACHE_NAME, null, DB_CACHE_VERSION);

        cacheTableEntity.addColumn(new OkColumnEntity(OkCacheEntity.KEY, "VARCHAR", true, true))//
                .addColumn(new OkColumnEntity(OkCacheEntity.LOCAL_EXPIRE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkCacheEntity.HEAD, "BLOB"))//
                .addColumn(new OkColumnEntity(OkCacheEntity.DATA, "BLOB"));

        cookieTableEntity.addColumn(new OkColumnEntity(OkSerializableCookie.HOST, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkSerializableCookie.NAME, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkSerializableCookie.DOMAIN, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkSerializableCookie.COOKIE, "BLOB"))//
                .addColumn(new OkColumnEntity(OkSerializableCookie.HOST, OkSerializableCookie.NAME, OkSerializableCookie.DOMAIN));

        downloadTableEntity.addColumn(new OkColumnEntity(OkProgress.TAG, "VARCHAR", true, true))//
                .addColumn(new OkColumnEntity(OkProgress.URL, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FOLDER, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FILE_PATH, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FILE_NAME, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FRACTION, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.TOTAL_SIZE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.CURRENT_SIZE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.STATUS, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.PRIORITY, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.DATE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.REQUEST, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA1, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA2, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA3, "BLOB"));

        uploadTableEntity.addColumn(new OkColumnEntity(OkProgress.TAG, "VARCHAR", true, true))//
                .addColumn(new OkColumnEntity(OkProgress.URL, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FOLDER, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FILE_PATH, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FILE_NAME, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.FRACTION, "VARCHAR"))//
                .addColumn(new OkColumnEntity(OkProgress.TOTAL_SIZE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.CURRENT_SIZE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.STATUS, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.PRIORITY, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.DATE, "INTEGER"))//
                .addColumn(new OkColumnEntity(OkProgress.REQUEST, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA1, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA2, "BLOB"))//
                .addColumn(new OkColumnEntity(OkProgress.EXTRA3, "BLOB"));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cacheTableEntity.buildTableString());
        db.execSQL(cookieTableEntity.buildTableString());
        db.execSQL(downloadTableEntity.buildTableString());
        db.execSQL(uploadTableEntity.buildTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (OkDBUtils.isNeedUpgradeTable(db, cacheTableEntity))
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
        if (OkDBUtils.isNeedUpgradeTable(db, cookieTableEntity))
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COOKIE);
        if (OkDBUtils.isNeedUpgradeTable(db, downloadTableEntity))
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD);
        if (OkDBUtils.isNeedUpgradeTable(db, uploadTableEntity))
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPLOAD);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
