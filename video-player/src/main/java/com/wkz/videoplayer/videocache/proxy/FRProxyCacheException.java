package com.wkz.videoplayer.videocache.proxy;

import com.wkz.videoplayer.BuildConfig;

/**
 * Indicates any error in work of {@link FRProxyCache}.
 *
 * @author Alexey Danilov
 */
public class FRProxyCacheException extends Exception {

    private static final String LIBRARY_VERSION = ". Version: " + BuildConfig.VERSION_NAME;

    public FRProxyCacheException(String message) {
        super(message + LIBRARY_VERSION);
    }

    public FRProxyCacheException(String message, Throwable cause) {
        super(message + LIBRARY_VERSION, cause);
    }

    public FRProxyCacheException(Throwable cause) {
        super("No explanation error" + LIBRARY_VERSION, cause);
    }
}
