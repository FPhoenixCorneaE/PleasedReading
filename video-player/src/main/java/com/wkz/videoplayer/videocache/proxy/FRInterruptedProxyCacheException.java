package com.wkz.videoplayer.videocache.proxy;

/**
 * Indicates interruption error in work of {@link FRProxyCache} fired by user.
 *
 * @author Alexey Danilov
 */
public class FRInterruptedProxyCacheException extends FRProxyCacheException {

    public FRInterruptedProxyCacheException(String message) {
        super(message);
    }

    public FRInterruptedProxyCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public FRInterruptedProxyCacheException(Throwable cause) {
        super(cause);
    }
}
