package com.wkz.videoplayer.videocache.source;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;

/**
 * FRSource for proxy.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface FRSource {

    /**
     * Opens source. FRSource should be open before using {@link #read(byte[])}
     *
     * @param offset offset in bytes for source.
     * @throws FRProxyCacheException if error occur while opening source.
     */
    void open(long offset) throws FRProxyCacheException;

    /**
     * Returns length bytes or <b>negative value</b> if length is unknown.
     *
     * @return bytes length
     * @throws FRProxyCacheException if error occur while fetching source data.
     */
    long length() throws FRProxyCacheException;

    /**
     * Read data to byte buffer from source with current offset.
     *
     * @param buffer a buffer to be used for reading data.
     * @return a count of read bytes
     * @throws FRProxyCacheException if error occur while reading source.
     */
    int read(byte[] buffer) throws FRProxyCacheException;

    /**
     * Closes source and release resources. Every opened source should be closed.
     *
     * @throws FRProxyCacheException if error occur while closing source.
     */
    void close() throws FRProxyCacheException;
}
