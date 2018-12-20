package com.wkz.videoplayer.videocache.sourcestorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wkz.videoplayer.videocache.FRPreconditions;
import com.wkz.videoplayer.videocache.FRSourceInfo;

/**
 * Database based {@link FRSourceInfoStorage}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
class FRDatabaseSourceInfoStorage extends SQLiteOpenHelper implements FRSourceInfoStorage {

    private static final String TABLE = "SourceInfo";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_MIME = "mime";
    private static final String[] ALL_COLUMNS = new String[]{COLUMN_ID, COLUMN_URL, COLUMN_LENGTH, COLUMN_MIME};
    private static final String CREATE_SQL =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_URL + " TEXT NOT NULL," +
                    COLUMN_MIME + " TEXT," +
                    COLUMN_LENGTH + " INTEGER" +
                    ");";

    FRDatabaseSourceInfoStorage(Context context) {
        super(context, "AndroidVideoCache.db", null, 1);
        FRPreconditions.checkNotNull(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FRPreconditions.checkNotNull(db);
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("Should not be called. There is no any migration");
    }

    @Override
    public FRSourceInfo get(String url) {
        FRPreconditions.checkNotNull(url);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(TABLE, ALL_COLUMNS, COLUMN_URL + "=?", new String[]{url}, null, null, null);
            return cursor == null || !cursor.moveToFirst() ? null : convert(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void put(String url, FRSourceInfo sourceInfo) {
        FRPreconditions.checkAllNotNull(url, sourceInfo);
        FRSourceInfo sourceInfoFromDb = get(url);
        boolean exist = sourceInfoFromDb != null;
        ContentValues contentValues = convert(sourceInfo);
        if (exist) {
            getWritableDatabase().update(TABLE, contentValues, COLUMN_URL + "=?", new String[]{url});
        } else {
            getWritableDatabase().insert(TABLE, null, contentValues);
        }
    }

    @Override
    public void release() {
        close();
    }

    private FRSourceInfo convert(Cursor cursor) {
        return new FRSourceInfo(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIME))
        );
    }

    private ContentValues convert(FRSourceInfo sourceInfo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, sourceInfo.url);
        values.put(COLUMN_LENGTH, sourceInfo.length);
        values.put(COLUMN_MIME, sourceInfo.mime);
        return values;
    }
}
