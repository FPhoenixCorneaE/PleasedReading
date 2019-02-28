package com.wkz.okgo.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.wkz.okgo.cache.OkCacheEntity;
import com.wkz.okgo.db.OkDBHelper;

import java.util.List;

/**
 * 缓存管理者
 */
public class OkCacheDao extends OkBaseDao<OkCacheEntity<?>> {

    public static OkCacheDao getInstance() {
        return CacheManagerHolder.instance;
    }

    private static class CacheManagerHolder {
        private static final OkCacheDao instance = new OkCacheDao();
    }

    private OkCacheDao() {
        super(new OkDBHelper());
    }

    @Override
    public OkCacheEntity<?> parseCursorToBean(Cursor cursor) {
        return OkCacheEntity.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(OkCacheEntity<?> cacheEntity) {
        return OkCacheEntity.getContentValues(cacheEntity);
    }

    @Override
    public String getTableName() {
        return OkDBHelper.TABLE_CACHE;
    }

    @Override
    public void unInit() {
    }

    /**
     * 根据key获取缓存
     */
    public OkCacheEntity<?> get(String key) {
        if (key == null) return null;
        List<OkCacheEntity<?>> cacheEntities = query(OkCacheEntity.KEY + "=?", new String[]{key});
        return cacheEntities.size() > 0 ? cacheEntities.get(0) : null;
    }

    /**
     * 移除一个缓存
     */
    public boolean remove(String key) {
        if (key == null) return false;
        return delete(OkCacheEntity.KEY + "=?", new String[]{key});
    }

    /**
     * 返回带泛型的对象,注意必须确保泛型和对象对应才不会发生类型转换异常
     */
    @SuppressWarnings("unchecked")
    public <T> OkCacheEntity<T> get(String key, Class<T> clazz) {
        return (OkCacheEntity<T>) get(key);
    }

    /**
     * 获取所有缓存
     */
    public List<OkCacheEntity<?>> getAll() {
        return queryAll();
    }

    /**
     * 更新缓存，没有就创建，有就替换
     *
     * @param key    缓存的key
     * @param entity 需要替换的的缓存
     * @return OkCacheEntity 被替换的缓存
     */
    public <T> OkCacheEntity<T> replace(String key, OkCacheEntity<T> entity) {
        entity.setKey(key);
        replace(entity);
        return entity;
    }

    /**
     * 清空缓存
     */
    public boolean clear() {
        return deleteAll();
    }
}
