package com.wkz.videoplayer.videocache.sourcestorage;


import com.wkz.videoplayer.videocache.FRSourceInfo;

/**
 * Storage for {@link FRSourceInfo}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface FRSourceInfoStorage {

    FRSourceInfo get(String url);

    void put(String url, FRSourceInfo sourceInfo);

    void release();
}
