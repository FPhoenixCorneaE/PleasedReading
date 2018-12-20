package com.wkz.videoplayer.videocache.sourcestorage;

import com.wkz.videoplayer.videocache.FRSourceInfo;

/**
 * {@link FRSourceInfoStorage} that does nothing.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRNoSourceInfoStorage implements FRSourceInfoStorage {

    @Override
    public FRSourceInfo get(String url) {
        return null;
    }

    @Override
    public void put(String url, FRSourceInfo sourceInfo) {
    }

    @Override
    public void release() {
    }
}
