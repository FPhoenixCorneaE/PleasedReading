package com.wkz.videoplayer.videocache.source;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;

import java.io.ByteArrayInputStream;

/**
 * Simple memory based {@link FRSource} implementation.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRByteArraySource implements FRSource {

    private final byte[] data;
    private ByteArrayInputStream arrayInputStream;

    public FRByteArraySource(byte[] data) {
        this.data = data;
    }

    @Override
    public int read(byte[] buffer) throws FRProxyCacheException {
        return arrayInputStream.read(buffer, 0, buffer.length);
    }

    @Override
    public long length() throws FRProxyCacheException {
        return data.length;
    }

    @Override
    public void open(long offset) throws FRProxyCacheException {
        arrayInputStream = new ByteArrayInputStream(data);
        arrayInputStream.skip(offset);
    }

    @Override
    public void close() throws FRProxyCacheException {
    }
}

