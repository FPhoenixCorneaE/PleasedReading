package com.wkz.okgo.cookie.store;

import android.content.Context;

import com.wkz.okgo.cookie.OkSerializableCookie;
import com.wkz.okgo.db.dao.OkCookieDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 使用数据库 持久化存储 cookie
 */
public class OkDBCookieStore implements IOkCookieStore {

    /**
     * 数据结构如下
     * Url.host -> cookie1.name@cookie1.domain,cookie2.name@cookie2.domain,cookie3.name@cookie3.domain
     * cookie_cookie1.name@cookie1.domain -> cookie1
     * cookie_cookie2.name@cookie2.domain -> cookie2
     */
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;

    public OkDBCookieStore(Context context) {
        OkCookieDao.init(context);
        cookies = new HashMap<>();
        List<OkSerializableCookie> cookieList = OkCookieDao.getInstance().queryAll();
        for (OkSerializableCookie serializableCookie : cookieList) {
            if (!cookies.containsKey(serializableCookie.host)) {
                cookies.put(serializableCookie.host, new ConcurrentHashMap<String, Cookie>());
            }
            Cookie cookie = serializableCookie.getCookie();
            cookies.get(serializableCookie.host).put(getCookieToken(cookie), cookie);
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 当前cookie是否过期
     */
    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    /**
     * 将url的所有Cookie保存在本地
     */
    @Override
    public synchronized void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        for (Cookie cookie : urlCookies) {
            saveCookie(url, cookie);
        }
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        //当前cookie是否过期
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie);
        } else {
            //内存缓存
            cookies.get(url.host()).put(getCookieToken(cookie), cookie);
            //数据库缓存
            OkSerializableCookie serializableCookie = new OkSerializableCookie(url.host(), cookie);
            OkCookieDao.getInstance().replace(serializableCookie);
        }
    }

    /**
     * 根据当前url获取所有需要的cookie,只返回没有过期的cookie
     */
    @Override
    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        if (!cookies.containsKey(url.host())) return ret;

        List<OkSerializableCookie> query = OkCookieDao.getInstance().query("host=?", new String[]{url.host()});
        for (OkSerializableCookie serializableCookie : query) {
            Cookie cookie = serializableCookie.getCookie();
            if (isCookieExpired(cookie)) {
                removeCookie(url, cookie);
            } else {
                ret.add(cookie);
            }
        }
        return ret;
    }

    /**
     * 根据url移除当前的cookie
     */
    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) return false;
        String cookieToken = getCookieToken(cookie);
        if (!cookies.get(url.host()).containsKey(cookieToken)) return false;

        //内存移除
        cookies.get(url.host()).remove(cookieToken);
        //数据库移除
        String whereClause = "host=? and name=? and domain=?";
        String[] whereArgs = {url.host(), cookie.name(), cookie.domain()};
        OkCookieDao.getInstance().delete(whereClause, whereArgs);
        return true;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url) {
        if (!cookies.containsKey(url.host())) return false;

        //内存移除
        cookies.remove(url.host());
        //数据库移除
        String whereClause = "host=?";
        String[] whereArgs = {url.host()};
        OkCookieDao.getInstance().delete(whereClause, whereArgs);
        return true;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        //内存移除
        cookies.clear();
        //数据库移除
        OkCookieDao.getInstance().deleteAll();
        return true;
    }

    /**
     * 获取所有的cookie
     */
    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet()) {
            ret.addAll(cookies.get(key).values());
        }
        return ret;
    }

    @Override
    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        Map<String, Cookie> mapCookie = cookies.get(url.host());
        if (mapCookie != null) ret.addAll(mapCookie.values());
        return ret;
    }
}
