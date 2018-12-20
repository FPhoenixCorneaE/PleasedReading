package com.wkz.videoplayer.videocache;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * Simple memory based {@link FRCache} implementation.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRByteArrayCache implements FRCache {

    private volatile byte[] data;
    private volatile boolean completed;

    public FRByteArrayCache() {
        this(new byte[0]);
    }

    public FRByteArrayCache(byte[] data) {
        this.data = FRPreconditions.checkNotNull(data);
    }

    @Override
    public int read(byte[] buffer, long offset, int length) throws FRProxyCacheException {
        if (offset >= data.length) {
            return -1;
        }
        if (offset > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Too long offset for memory cache " + offset);
        }
        return new ByteArrayInputStream(data).read(buffer, (int) offset, length);
    }

    @Override
    public long available() throws FRProxyCacheException {
        return data.length;
    }

    @Override
    public void append(byte[] newData, int length) throws FRProxyCacheException {
        FRPreconditions.checkNotNull(data);
        FRPreconditions.checkArgument(length >= 0 && length <= newData.length);

        byte[] appendedData = Arrays.copyOf(data, data.length + length);
        System.arraycopy(newData, 0, appendedData, data.length, length);
        data = appendedData;
    }

    @Override
    public void close() throws FRProxyCacheException {
    }

    @Override
    public void complete() {
        completed = true;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
