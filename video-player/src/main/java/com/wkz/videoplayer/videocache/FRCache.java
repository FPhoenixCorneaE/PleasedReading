package com.wkz.videoplayer.videocache;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;

/**
 * FRCache for proxy.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface FRCache {

    long available() throws FRProxyCacheException;

    int read(byte[] buffer, long offset, int length) throws FRProxyCacheException;

    void append(byte[] data, int length) throws FRProxyCacheException;

    void close() throws FRProxyCacheException;

    void complete() throws FRProxyCacheException;

    boolean isCompleted();
}
