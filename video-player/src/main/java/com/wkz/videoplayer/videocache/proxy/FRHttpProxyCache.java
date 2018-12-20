package com.wkz.videoplayer.videocache.proxy;

import android.text.TextUtils;

import com.wkz.videoplayer.videocache.FRGetRequest;
import com.wkz.videoplayer.videocache.OnCacheListener;
import com.wkz.videoplayer.videocache.file.FRFileCache;
import com.wkz.videoplayer.videocache.source.FROkHttpUrlSource;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Locale;


/**
 * {@link FRProxyCache} that read http url and writes data to {@link Socket}
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
class FRHttpProxyCache extends FRProxyCache {

    private static final float NO_CACHE_BARRIER = .2f;

    private final FROkHttpUrlSource source;
    private final FRFileCache cache;
    private OnCacheListener listener;

    public FRHttpProxyCache(FROkHttpUrlSource source, FRFileCache cache) {
        super(source, cache);
        this.cache = cache;
        this.source = source;
    }

    public void registerCacheListener(OnCacheListener cacheListener) {
        this.listener = cacheListener;
    }

    public void processRequest(FRGetRequest request, Socket socket) throws IOException, FRProxyCacheException {
        OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        String responseHeaders = newResponseHeaders(request);
        out.write(responseHeaders.getBytes("UTF-8"));

        long offset = request.rangeOffset;
        if (isUseCache(request)) {
            responseWithCache(out, offset);
        } else {
            responseWithoutCache(out, offset);
        }
    }

    private boolean isUseCache(FRGetRequest request) throws FRProxyCacheException {
        long sourceLength = source.length();
        boolean sourceLengthKnown = sourceLength > 0;
        long cacheAvailable = cache.available();
        // do not use cache for partial requests which too far from available cache. It seems user seek video.
        return !sourceLengthKnown || !request.partial || request.rangeOffset <= cacheAvailable + sourceLength * NO_CACHE_BARRIER;
    }

    private String newResponseHeaders(FRGetRequest request) throws IOException, FRProxyCacheException {
        String mime = source.getMime();
        boolean mimeKnown = !TextUtils.isEmpty(mime);
        long length = cache.isCompleted() ? cache.available() : source.length();
        boolean lengthKnown = length >= 0;
        long contentLength = request.partial ? length - request.rangeOffset : length;
        boolean addRange = lengthKnown && request.partial;
        return new StringBuilder()
                .append(request.partial ? "HTTP/1.1 206 PARTIAL CONTENT\n" : "HTTP/1.1 200 OK\n")
                .append("Accept-Ranges: bytes\n")
                .append(lengthKnown ? format("Content-Length: %d\n", contentLength) : "")
                .append(addRange ? format("Content-Range: bytes %d-%d/%d\n", request.rangeOffset, length - 1, length) : "")
                .append(mimeKnown ? format("Content-Type: %s\n", mime) : "")
                .append("\n") // headers end
                .toString();
    }

    private void responseWithCache(OutputStream out, long offset) throws FRProxyCacheException, IOException {
        byte[] buffer = new byte[FRProxyCacheUtils.DEFAULT_BUFFER_SIZE];
        int readBytes;
        while ((readBytes = read(buffer, offset, buffer.length)) != -1) {
            out.write(buffer, 0, readBytes);
            offset += readBytes;
        }
        out.flush();
    }

    private void responseWithoutCache(OutputStream out, long offset) throws FRProxyCacheException, IOException {
        FROkHttpUrlSource newSourceNoCache = new FROkHttpUrlSource(this.source);
        try {
            newSourceNoCache.open((int) offset);
            byte[] buffer = new byte[FRProxyCacheUtils.DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = newSourceNoCache.read(buffer)) != -1) {
                out.write(buffer, 0, readBytes);
                offset += readBytes;
            }
            out.flush();
        } finally {
            newSourceNoCache.close();
        }
    }

    private String format(String pattern, Object... args) {
        return String.format(Locale.US, pattern, args);
    }

    @Override
    protected void onCachePercentsAvailableChanged(int percents) {
        if (listener != null) {
            listener.onCacheAvailable(cache.file, source.getUrl(), percents);
        }
    }
}
