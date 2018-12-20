package com.wkz.videoplayer.videocache.proxy;

public class FRUrlMime {
    protected long length = Integer.MIN_VALUE;
    protected String mime;

    public FRUrlMime() {
    }

    public FRUrlMime(long length, String mime) {
        this.length = length;
        this.mime = mime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}
