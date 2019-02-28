package com.wkz.okgo.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.wkz.okgo.cookie.OkSerializableCookie;
import com.wkz.okgo.db.OkDBHelper;

public class OkCookieDao extends OkBaseDao<OkSerializableCookie> {

    private static Context context;
    private volatile static OkCookieDao instance;

    public static OkCookieDao getInstance() {
        if (instance == null) {
            synchronized (OkCookieDao.class) {
                if (instance == null) {
                    instance = new OkCookieDao();
                }
            }
        }
        return instance;
    }

    private OkCookieDao() {
        super(new OkDBHelper(context));
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    @Override
    public OkSerializableCookie parseCursorToBean(Cursor cursor) {
        return OkSerializableCookie.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(OkSerializableCookie serializableCookie) {
        return OkSerializableCookie.getContentValues(serializableCookie);
    }

    @Override
    public String getTableName() {
        return OkDBHelper.TABLE_COOKIE;
    }

    @Override
    public void unInit() {
    }
}
