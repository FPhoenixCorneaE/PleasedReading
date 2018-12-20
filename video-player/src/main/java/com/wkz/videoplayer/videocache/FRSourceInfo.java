package com.wkz.videoplayer.videocache;

/**
 * Stores source's info.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRSourceInfo {

    public final String url;
    public long length;
    public String mime;

    public FRSourceInfo(String url, long length, String mime) {
        this.url = url;
        this.length = length;
        this.mime = mime;
    }

    @Override
    public String toString() {
        return "FRSourceInfo{" +
                "url='" + url + '\'' +
                ", length=" + length +
                ", mime='" + mime + '\'' +
                '}';
    }
}
