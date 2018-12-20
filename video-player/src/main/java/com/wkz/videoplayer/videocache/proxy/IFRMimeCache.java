package com.wkz.videoplayer.videocache.proxy;

public interface IFRMimeCache {

    void putMime(String url, long length, String mime);

    FRUrlMime getMime(String url);
}
