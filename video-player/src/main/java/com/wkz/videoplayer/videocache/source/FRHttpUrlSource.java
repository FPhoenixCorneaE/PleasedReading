package com.wkz.videoplayer.videocache.source;

import android.text.TextUtils;

import com.wkz.videoplayer.utils.FRVideoLogUtils;
import com.wkz.videoplayer.videocache.FRPreconditions;
import com.wkz.videoplayer.videocache.FRSourceInfo;
import com.wkz.videoplayer.videocache.headers.FREmptyHeadersInjector;
import com.wkz.videoplayer.videocache.headers.FRHeaderInjector;
import com.wkz.videoplayer.videocache.proxy.FRInterruptedProxyCacheException;
import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;
import com.wkz.videoplayer.videocache.proxy.FRProxyCacheUtils;
import com.wkz.videoplayer.videocache.proxy.FRUrlMime;
import com.wkz.videoplayer.videocache.proxy.IFRMimeCache;
import com.wkz.videoplayer.videocache.sourcestorage.FRSourceInfoStorage;
import com.wkz.videoplayer.videocache.sourcestorage.FRSourceInfoStorageFactory;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * {@link FRSource} that uses http resource as source for {@link com.wkz.videoplayer.videocache.proxy.FRProxyCache}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRHttpUrlSource extends FRUrlSource {

    private static final int MAX_REDIRECTS = 5;
    private final FRSourceInfoStorage sourceInfoStorage;
    private final FRHeaderInjector headerInjector;
    private FRSourceInfo sourceInfo;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private IFRMimeCache iMimeCache;

    public FRHttpUrlSource(String url, IFRMimeCache iMimeCache) {
        this(url, FRSourceInfoStorageFactory.newEmptySourceInfoStorage(), iMimeCache);
    }

    public FRHttpUrlSource(String url, FRSourceInfoStorage sourceInfoStorage, IFRMimeCache iMimeCache) {
        this(url, sourceInfoStorage, new FREmptyHeadersInjector(), iMimeCache);
    }

    public FRHttpUrlSource(String url, FRSourceInfoStorage sourceInfoStorage, FRHeaderInjector headerInjector, IFRMimeCache iMimeCache) {
        super(url);
        this.sourceInfoStorage = FRPreconditions.checkNotNull(sourceInfoStorage);
        this.headerInjector = FRPreconditions.checkNotNull(headerInjector);
        this.iMimeCache = iMimeCache;
        FRSourceInfo sourceInfo = sourceInfoStorage.get(url);
        this.sourceInfo = sourceInfo != null ? sourceInfo :
                new FRSourceInfo(url, Integer.MIN_VALUE, FRProxyCacheUtils.getSupposablyMime(url));
    }

    public FRHttpUrlSource(FRHttpUrlSource source) {
        super(source.sourceInfo.url);
        this.sourceInfo = source.sourceInfo;
        this.sourceInfoStorage = source.sourceInfoStorage;
        this.headerInjector = source.headerInjector;
    }

    @Override
    public synchronized long length() throws FRProxyCacheException {
        if (sourceInfo.length == Integer.MIN_VALUE) {
            tryLoadMimeCache();
        }
        if (sourceInfo.length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        return sourceInfo.length;
    }

    private void tryPutMimeCache() {
        if (iMimeCache != null) {
            iMimeCache.putMime(sourceInfo.url, sourceInfo.length, sourceInfo.mime);
        }
    }

    private void tryLoadMimeCache() {
        if (iMimeCache != null) {
            FRUrlMime urlMime = iMimeCache.getMime(sourceInfo.url);
            if (urlMime != null && !TextUtils.isEmpty(urlMime.getMime()) && urlMime.getLength() != Integer.MIN_VALUE) {
                this.sourceInfo.mime = urlMime.getMime();
                this.sourceInfo.length = urlMime.getLength();
            }
        }
    }

    @Override
    public void open(long offset) throws FRProxyCacheException {
        try {
            connection = openConnection(offset, -1);
            String mime = connection.getContentType();
            inputStream = new BufferedInputStream(connection.getInputStream(), FRProxyCacheUtils.DEFAULT_BUFFER_SIZE);
            long length = readSourceAvailableBytes(connection, offset, connection.getResponseCode());
            this.sourceInfo = new FRSourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
        } catch (IOException e) {
            throw new FRProxyCacheException("Error opening connection for " + sourceInfo.url + " with offset " + offset, e);
        }
    }

    private long readSourceAvailableBytes(HttpURLConnection connection, long offset, int responseCode) throws IOException {
        long contentLength = getContentLength(connection);
        return responseCode == HttpURLConnection.HTTP_OK ? contentLength
                : responseCode == HttpURLConnection.HTTP_PARTIAL ? contentLength + offset : sourceInfo.length;
    }

    private long getContentLength(HttpURLConnection connection) {
        String contentLengthValue = connection.getHeaderField("Content-Length");
        return contentLengthValue == null ? -1 : Long.parseLong(contentLengthValue);
    }

    @Override
    public void close() throws FRProxyCacheException {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (NullPointerException | IllegalArgumentException e) {
                String message = "Wait... but why? WTF!? " +
                        "Really shouldn't happen any more after fixing https://github.com/danikula/AndroidVideoCache/issues/43. " +
                        "If you read it on your device log, please, notify me danikula@gmail.com or create issue here " +
                        "https://github.com/danikula/AndroidVideoCache/issues.";
                throw new RuntimeException(message, e);
            } catch (ArrayIndexOutOfBoundsException e) {
                FRVideoLogUtils.e("Error closing connection correctly. Should happen only on Android L. " +
                        "If anybody know how to fix it, please visit https://github.com/danikula/AndroidVideoCache/issues/88. " +
                        "Until good solution is not know, just ignore this issue :(", e);
            }
        }
    }

    @Override
    public int read(byte[] buffer) throws FRProxyCacheException {
        if (inputStream == null) {
            throw new FRProxyCacheException("Error reading data from " + sourceInfo.url + ": connection is absent!");
        }
        try {
            return inputStream.read(buffer, 0, buffer.length);
        } catch (InterruptedIOException e) {
            throw new FRInterruptedProxyCacheException("Reading source " + sourceInfo.url + " is interrupted", e);
        } catch (IOException e) {
            throw new FRProxyCacheException("Error reading data from " + sourceInfo.url, e);
        }
    }

    private void fetchContentInfo() throws FRProxyCacheException {
        FRVideoLogUtils.d("Read content info from " + sourceInfo.url);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = openConnectionForHeader(0, 10000);
            long length = getContentLength(urlConnection);
            String mime = urlConnection.getContentType();
            //将数据放入缓存
            tryPutMimeCache();
            inputStream = urlConnection.getInputStream();
            this.sourceInfo = new FRSourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
            FRVideoLogUtils.d("FRSource info fetched: " + sourceInfo);
        } catch (IOException e) {
            FRVideoLogUtils.e("Error fetching info from " + sourceInfo.url, e);
        } finally {
            FRProxyCacheUtils.close(inputStream);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * fetchContentInfo()利用HttpURLConnection获取url的长度和文件类型，默认是"GET"方法，返回了BODY数据，
     * 在fetchContentInfo()中，BODY里面的数据根本就没有用到，并且在某些API版本中HttpURLConnection.disconnect()方法
     * 会将HttpURLConnection中的数据流读完才会关闭，耗时数秒甚至更长(取决于url指向的文件大小)。
     * 而url的length(长度)和mime(文件类型)这两个值是存在Http响应的头部，那么可以使用Http的“HEAD”方法，只返回头部，
     * 不需要BODY，既可以提高响应速度也可以减少网络流量。只需要增加一行代码即可，但是openConnection()方法在其它地方也会用到，
     * 因此应该单独为fetchContentInfo()写一个openConnection()方法，如openConnectionForHeader():
     *
     * @param offset
     * @param timeout
     * @return
     * @throws IOException
     * @throws FRProxyCacheException
     */
    private HttpURLConnection openConnectionForHeader(long offset, int timeout) throws IOException, FRProxyCacheException {
        HttpURLConnection connection;
        boolean redirected;
        int redirectCount = 0;
        String url = this.sourceInfo.url;
        do {
            FRVideoLogUtils.d("Open connection " + (offset > 0 ? " with offset " + offset : "") + " to " + url);
            connection = (HttpURLConnection) new URL(url).openConnection();
            injectCustomHeaders(connection, url);
            if (offset > 0) {
                connection.setRequestProperty("Range", "bytes=" + offset + "-");
            }
            if (timeout > 0) {
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
            }
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            redirected = code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_SEE_OTHER;
            if (redirected) {
                url = connection.getHeaderField("Location");
                redirectCount++;
                connection.disconnect();
            }
            if (redirectCount > MAX_REDIRECTS) {
                throw new FRProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection;
    }

    private HttpURLConnection openConnection(long offset, int timeout) throws IOException, FRProxyCacheException {
        HttpURLConnection connection;
        boolean redirected;
        int redirectCount = 0;
        String url = this.sourceInfo.url;
        do {
            FRVideoLogUtils.d("Open connection " + (offset > 0 ? " with offset " + offset : "") + " to " + url);
            connection = (HttpURLConnection) new URL(url).openConnection();
            injectCustomHeaders(connection, url);
            if (offset > 0) {
                connection.setRequestProperty("Range", "bytes=" + offset + "-");
            }
            if (timeout > 0) {
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
            }
            int code = connection.getResponseCode();
            redirected = code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_SEE_OTHER;
            if (redirected) {
                url = connection.getHeaderField("Location");
                redirectCount++;
                connection.disconnect();
            }
            if (redirectCount > MAX_REDIRECTS) {
                throw new FRProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection;
    }

    private void injectCustomHeaders(HttpURLConnection connection, String url) {
        Map<String, String> extraHeaders = headerInjector.addHeaders(url);
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    public synchronized String getMime() throws FRProxyCacheException {
        if (TextUtils.isEmpty(sourceInfo.mime)) {
            tryLoadMimeCache();
        }
        if (TextUtils.isEmpty(sourceInfo.mime)) {
            fetchContentInfo();
        }
        return sourceInfo.mime;
    }

    public String getUrl() {
        return sourceInfo.url;
    }

    @Override
    public String toString() {
        return "FRHttpUrlSource{sourceInfo='" + sourceInfo + "}";
    }
}
