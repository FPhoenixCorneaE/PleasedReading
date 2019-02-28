package com.wkz.okgo.cookie;

import com.wkz.okgo.cookie.store.IOkCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * CookieJar的实现类，默认管理了用户自己维护的cookie
 */
public class OkCookieJarImpl implements CookieJar {

    private IOkCookieStore cookieStore;

    public OkCookieJarImpl(IOkCookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null!");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.saveCookie(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.loadCookie(url);
    }

    public IOkCookieStore getCookieStore() {
        return cookieStore;
    }
}
