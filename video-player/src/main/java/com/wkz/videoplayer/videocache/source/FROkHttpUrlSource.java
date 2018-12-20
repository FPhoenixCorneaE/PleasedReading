package com.wkz.videoplayer.videocache.source;

import android.text.TextUtils;
import android.util.Log;

import com.wkz.videoplayer.videocache.FRPreconditions;
import com.wkz.videoplayer.videocache.proxy.FRInterruptedProxyCacheException;
import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;
import com.wkz.videoplayer.videocache.proxy.FRProxyCacheUtils;
import com.wkz.videoplayer.videocache.proxy.FRUrlMime;
import com.wkz.videoplayer.videocache.proxy.IFRMimeCache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FROkHttpUrlSource extends FRUrlSource {

    private static String LOG_TAG = "FROkHttpUrlSource";
    private static final int MAX_REDIRECTS = 5;
    private IFRMimeCache mimeCache;
    private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private InputStream inputStream;

    public FROkHttpUrlSource(IFRMimeCache mimeCache, String url) {
        super(url);
        this.mimeCache = mimeCache;
    }

    public FROkHttpUrlSource(FRUrlSource urlSource) {
        super(urlSource);
    }

    @Override
    public long length() throws FRProxyCacheException {
        if (length == Integer.MIN_VALUE) {
            tryLoadMimeCache();
        }
        if (length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        return length;
    }

    @Override
    public String getMime() throws FRProxyCacheException {
        if (TextUtils.isEmpty(mime)) {
            tryLoadMimeCache();
        }
        if (TextUtils.isEmpty(mime)) {
            fetchContentInfo();
        }
        return mime;
    }

    @Override
    public void open(long offset) throws FRProxyCacheException {
        try {
            Response response = openConnection(offset, -1);
            FRPreconditions.checkNotNull(response.body());
            FRPreconditions.checkNotNull(response.body().contentType());
            mime = response.body().contentType().toString();
            length = readSourceAvailableBytes(response, offset);
            inputStream = new BufferedInputStream(response.body().byteStream(), FRProxyCacheUtils.DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            throw new FRProxyCacheException("Error opening connection for " + url + " with offset " + offset, e);
        }
    }

    private long readSourceAvailableBytes(Response response, long offset) throws IOException {
        int responseCode = response.code();
        int contentLength = (int) (response.body() != null ? response.body().contentLength() : 0);
        return responseCode == HttpURLConnection.HTTP_OK ? contentLength
                : responseCode == HttpURLConnection.HTTP_PARTIAL ? contentLength + offset : length;
    }

    @Override
    public int read(byte[] buffer) throws FRProxyCacheException {
        if (inputStream == null) {
            throw new FRProxyCacheException("Error reading data from " + url + ": connection is absent!");
        }
        try {
            return inputStream.read(buffer, 0, buffer.length);
        } catch (InterruptedIOException e) {
            throw new FRInterruptedProxyCacheException("Reading source " + url + " is interrupted", e);
        } catch (IOException e) {
            throw new FRProxyCacheException("Error reading data from " + url, e);
        }
    }

    @Override
    public void close() throws FRProxyCacheException {
        FRProxyCacheUtils.close(inputStream);
    }

    private void fetchContentInfo() throws FRProxyCacheException {
        Log.d(LOG_TAG, "Read content info from " + url);
        Response response;
        try {
            response = openConnectionForHeader(10000);
            if (response == null || !response.isSuccessful()) {
                throw new FRProxyCacheException("Fail to fetchContentInfo: " + url);
            }
            length = (int) (response.body() != null ? response.body().contentLength() : 0);
            FRPreconditions.checkNotNull(response.body());
            FRPreconditions.checkNotNull(response.body().contentType());
            mime = response.body().contentType().toString();
            tryPutMimeCache();
            Log.i(LOG_TAG, "Content info for `" + url + "`: mime: " + mime + ", content-length: " + length);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error fetching info from " + url, e);
        } finally {
            Log.d(LOG_TAG, "Closed connection from :" + url);
        }
    }

    private Response openConnectionForHeader(int timeout) throws IOException, FRProxyCacheException {
        if (timeout > 0) {
            httpClientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            httpClientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
            httpClientBuilder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        }
        Response response;
        boolean isRedirect = false;
        String newUrl = this.url;
        int redirectCount = 0;
        do {
            FRPreconditions.checkNotNull(newUrl);
            Request request = new Request.Builder()
                    .head()
                    .url(newUrl)
                    .build();
            response = httpClientBuilder.build().newCall(request).execute();
            if (response.isRedirect()) {
                newUrl = response.header("Location");
                isRedirect = response.isRedirect();
                redirectCount++;
            }
            if (redirectCount > MAX_REDIRECTS) {
                throw new FRProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (isRedirect);

        return response;
    }

    private Response openConnection(long offset, int timeout) throws IOException, FRProxyCacheException {
        if (timeout > 0) {
            httpClientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            httpClientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
            httpClientBuilder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        }
        Response response;
        boolean isRedirect = false;
        String newUrl = this.url;
        int redirectCount = 0;
        do {
            Log.d(LOG_TAG, "Open connection" + (offset > 0 ? " with offset " + offset : "") + " to " + url);
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.get();
            FRPreconditions.checkNotNull(newUrl);
            requestBuilder.url(newUrl);
            if (offset > 0) {
                requestBuilder.addHeader("Range", "bytes=" + offset + "-");
            }
            response = httpClientBuilder.build().newCall(requestBuilder.build()).execute();
            if (response.isRedirect()) {
                newUrl = response.header("Location");
                isRedirect = response.isRedirect();
                redirectCount++;
            }
            if (redirectCount > MAX_REDIRECTS) {
                throw new FRProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (isRedirect);

        return response;
    }

    private void tryLoadMimeCache() {
        if (mimeCache != null) {
            FRUrlMime urlMime = mimeCache.getMime(url);
            if (urlMime != null && !TextUtils.isEmpty(urlMime.getMime()) && urlMime.getLength() != Integer.MIN_VALUE) {
                this.mime = urlMime.getMime();
                this.length = urlMime.getLength();
            }
        }
    }

    private void tryPutMimeCache() {
        if (mimeCache != null) {
            mimeCache.putMime(url, length, mime);
        }
    }
}
