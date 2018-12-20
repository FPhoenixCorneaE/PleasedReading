package com.wkz.videoplayer.videocache.source;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;

public abstract class FRUrlSource implements FRSource {
    protected String url;
    protected volatile long length = Integer.MIN_VALUE;
    protected volatile String mime;

    public FRUrlSource(String url) {
        this.url = url;
    }

    public FRUrlSource(FRUrlSource urlSource) {
        this.url = urlSource.url;
        this.length = urlSource.length;
        this.mime = urlSource.mime;
    }

    public String getUrl() {
        return url;
    }

    public abstract String getMime() throws FRProxyCacheException;

    @Override
    public String toString() {
        return "FRUrlSource{" +
                "url='" + url + '\'' +
                ", length=" + length +
                ", mime='" + mime + '\'' +
                '}';
    }
}
