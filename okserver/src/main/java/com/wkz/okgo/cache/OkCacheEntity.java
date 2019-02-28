package com.wkz.okgo.cache;

import android.content.ContentValues;
import android.database.Cursor;

import com.wkz.okgo.model.OkHttpHeaders;
import com.wkz.okgo.utils.OkIOUtils;

import java.io.Serializable;

public class OkCacheEntity<T> implements Serializable {
    private static final long serialVersionUID = 6435574291092942465L;

    public static final long CACHE_NEVER_EXPIRE = -1;        //缓存永不过期

    //表中的字段
    public static final String KEY = "key";
    public static final String LOCAL_EXPIRE = "localExpire";
    public static final String HEAD = "head";
    public static final String DATA = "data";

    private String key;                    // 缓存key
    private long localExpire;              // 缓存过期时间
    private OkHttpHeaders responseHeaders;   // 缓存的响应头
    private T data;                        // 缓存的实体数据
    private boolean isExpire;   //缓存是否过期该变量不必保存到数据库，程序运行起来后会动态计算

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OkHttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(OkHttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getLocalExpire() {
        return localExpire;
    }

    public void setLocalExpire(long localExpire) {
        this.localExpire = localExpire;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }

    /**
     * @param cacheTime 允许的缓存时间
     * @param baseTime  基准时间,小于当前时间视为过期
     * @return 是否过期
     */
    public boolean checkExpire(@OkCacheMode int cacheMode, long cacheTime, long baseTime) {
        //304的默认缓存模式,设置缓存时间无效,需要依靠服务端的响应头控制
        if (cacheMode == OkCacheMode.DEFAULT) return getLocalExpire() < baseTime;
        if (cacheTime == CACHE_NEVER_EXPIRE) return false;
        return getLocalExpire() + cacheTime < baseTime;
    }

    public static <T> ContentValues getContentValues(OkCacheEntity<T> cacheEntity) {
        ContentValues values = new ContentValues();
        values.put(KEY, cacheEntity.getKey());
        values.put(LOCAL_EXPIRE, cacheEntity.getLocalExpire());
        values.put(HEAD, OkIOUtils.toByteArray(cacheEntity.getResponseHeaders()));
        values.put(DATA, OkIOUtils.toByteArray(cacheEntity.getData()));
        return values;
    }

    public static <T> OkCacheEntity<T> parseCursorToBean(Cursor cursor) {
        OkCacheEntity<T> cacheEntity = new OkCacheEntity<>();
        cacheEntity.setKey(cursor.getString(cursor.getColumnIndex(KEY)));
        cacheEntity.setLocalExpire(cursor.getLong(cursor.getColumnIndex(LOCAL_EXPIRE)));
        cacheEntity.setResponseHeaders((OkHttpHeaders) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(HEAD))));
        //noinspection unchecked
        cacheEntity.setData((T) OkIOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(DATA))));
        return cacheEntity;
    }

    @Override
    public String toString() {
        return "OkCacheEntity{key='" + key + '\'' + //
                ", responseHeaders=" + responseHeaders + //
                ", data=" + data + //
                ", localExpire=" + localExpire + //
                '}';
    }
}
