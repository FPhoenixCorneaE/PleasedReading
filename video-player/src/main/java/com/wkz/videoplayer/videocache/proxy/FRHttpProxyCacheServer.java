package com.wkz.videoplayer.videocache.proxy;

import android.content.Context;
import android.net.Uri;

import com.wkz.videoplayer.utils.FRVideoLogUtils;
import com.wkz.videoplayer.videocache.FRConfig;
import com.wkz.videoplayer.videocache.FRGetRequest;
import com.wkz.videoplayer.videocache.FRPinger;
import com.wkz.videoplayer.videocache.FRPreconditions;
import com.wkz.videoplayer.videocache.FRStorageUtils;
import com.wkz.videoplayer.videocache.OnCacheListener;
import com.wkz.videoplayer.videocache.file.FRDiskUsage;
import com.wkz.videoplayer.videocache.file.FRFileNameGenerator;
import com.wkz.videoplayer.videocache.file.FRMd5FileNameGenerator;
import com.wkz.videoplayer.videocache.file.FRTotalCountLruDiskUsage;
import com.wkz.videoplayer.videocache.file.FRTotalSizeLruDiskUsage;
import com.wkz.videoplayer.videocache.headers.FREmptyHeadersInjector;
import com.wkz.videoplayer.videocache.headers.FRHeaderInjector;
import com.wkz.videoplayer.videocache.sourcestorage.FRSourceInfoStorage;
import com.wkz.videoplayer.videocache.sourcestorage.FRSourceInfoStorageFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Simple lightweight proxy server with file caching support that handles HTTP requests.
 * Typical usage:
 * <pre><code>
 * public onCreate(Bundle state) {
 *      super.onCreate(state);
 *
 *      FRHttpProxyCacheServer proxy = getProxy();
 *      String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
 *      videoView.setVideoPath(proxyUrl);
 * }
 *
 * private FRHttpProxyCacheServer getProxy() {
 * // should return single instance of FRHttpProxyCacheServer shared for whole app.
 * }
 * </code></pre>
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRHttpProxyCacheServer implements IFRMimeCache {

    private static final String PROXY_HOST = "127.0.0.1";

    private final Object clientsLock = new Object();
    private final ExecutorService socketProcessor = Executors.newFixedThreadPool(8);
    private final Map<String, FRHttpProxyCacheServerClients> clientsMap = new ConcurrentHashMap<>();
    private final ServerSocket serverSocket;
    private final int port;
    private final Thread waitConnectionThread;
    private final FRConfig config;
    private final FRPinger pinger;
    private Map<String, FRUrlMime> urlMimeMap = new HashMap<>();

    public FRHttpProxyCacheServer(Context context) {
        this(new Builder(context).buildConfig());
    }

    private FRHttpProxyCacheServer(FRConfig config) {
        this.config = FRPreconditions.checkNotNull(config);
        try {
            InetAddress inetAddress = InetAddress.getByName(PROXY_HOST);
            this.serverSocket = new ServerSocket(0, 8, inetAddress);
            this.port = serverSocket.getLocalPort();
            FRIgnoreHostProxySelector.install(PROXY_HOST, port);
            CountDownLatch startSignal = new CountDownLatch(1);
            this.waitConnectionThread = new Thread(new WaitRequestsRunnable(startSignal));
            this.waitConnectionThread.start();
            startSignal.await(); // freeze thread, wait for server starts
            this.pinger = new FRPinger(PROXY_HOST, port, this);
            FRVideoLogUtils.i("Proxy cache server started. Is it alive? " + isAlive());
        } catch (IOException | InterruptedException e) {
            socketProcessor.shutdown();
            throw new IllegalStateException("Error starting local proxy server", e);
        }
    }

    /**
     * Returns url that wrap original url and should be used for client (MediaPlayer, ExoPlayer, etc).
     * <p>
     * If file for this url is fully cached (it means method {@link #isCached(String)} returns {@code true})
     * then file:// uri to cached file will be returned.
     * <p>
     * Calling this method has same effect as calling {@link #getProxyUrl(String, boolean)} with 2nd parameter set to {@code true}.
     *
     * @param url a url to file that should be cached.
     * @return a wrapped by proxy url if file is not fully cached or url pointed to cache file otherwise.
     */
    public String getProxyUrl(String url) {
        return getProxyUrl(url, true);
    }

    /**
     * Returns url that wrap original url and should be used for client (MediaPlayer, ExoPlayer, etc).
     * <p>
     * If parameter {@code allowCachedFileUri} is {@code true} and file for this url is fully cached
     * (it means method {@link #isCached(String)} returns {@code true}) then file:// uri to cached file will be returned.
     *
     * @param url                a url to file that should be cached.
     * @param allowCachedFileUri {@code true} if allow to return file:// uri if url is fully cached
     * @return a wrapped by proxy url if file is not fully cached or url pointed to cache file otherwise (if {@code allowCachedFileUri} is {@code true}).
     */
    public String getProxyUrl(String url, boolean allowCachedFileUri) {
        if (allowCachedFileUri && isCached(url)) {
            File cacheFile = getCacheFile(url);
            touchFileSafely(cacheFile);
            return Uri.fromFile(cacheFile).toString();
        }
        return isAlive() ? appendToProxyUrl(url) : url;
    }

    public void registerCacheListener(OnCacheListener cacheListener, String url) {
        FRPreconditions.checkAllNotNull(cacheListener, url);
        synchronized (clientsLock) {
            try {
                getClients(url).registerCacheListener(cacheListener);
            } catch (FRProxyCacheException e) {
                FRVideoLogUtils.e("Error registering cache listener", e);
            }
        }
    }

    public void unregisterCacheListener(OnCacheListener cacheListener, String url) {
        FRPreconditions.checkAllNotNull(cacheListener, url);
        synchronized (clientsLock) {
            try {
                getClients(url).unregisterCacheListener(cacheListener);
            } catch (FRProxyCacheException e) {
                FRVideoLogUtils.e("Error registering cache listener", e);
            }
        }
    }

    public void unregisterCacheListener(OnCacheListener cacheListener) {
        FRPreconditions.checkNotNull(cacheListener);
        synchronized (clientsLock) {
            for (FRHttpProxyCacheServerClients clients : clientsMap.values()) {
                clients.unregisterCacheListener(cacheListener);
            }
        }
    }

    /**
     * Checks is cache contains fully cached file for particular url.
     *
     * @param url an url cache file will be checked for.
     * @return {@code true} if cache contains fully cached file for passed in parameters url.
     */
    public boolean isCached(String url) {
        FRPreconditions.checkNotNull(url, "Url can't be null!");
        return getCacheFile(url).exists();
    }

    public void shutdown() {
        FRVideoLogUtils.i("Shutdown proxy server");

        shutdownClients();

        config.sourceInfoStorage.release();

        waitConnectionThread.interrupt();
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            onError(new FRProxyCacheException("Error shutting down proxy server", e));
        }
    }

    private boolean isAlive() {
        return pinger.ping(3, 70);   // 70+140+280=max~500ms
    }

    private String appendToProxyUrl(String url) {
        return String.format(Locale.US, "http://%s:%d/%s", PROXY_HOST, port, FRProxyCacheUtils.encode(url));
    }

    private File getCacheFile(String url) {
        File cacheDir = config.cacheRoot;
        String fileName = config.fileNameGenerator.generate(url);
        return new File(cacheDir, fileName);
    }

    private void touchFileSafely(File cacheFile) {
        try {
            config.diskUsage.touch(cacheFile);
        } catch (IOException e) {
            FRVideoLogUtils.e("Error touching file " + cacheFile, e);
        }
    }

    private void shutdownClients() {
        synchronized (clientsLock) {
            for (FRHttpProxyCacheServerClients clients : clientsMap.values()) {
                clients.shutdown();
            }
            clientsMap.clear();
        }
    }

    private void waitForRequest() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                FRVideoLogUtils.d("Accept new socket " + socket);
                socketProcessor.submit(new SocketProcessorRunnable(socket));
            }
        } catch (IOException e) {
            onError(new FRProxyCacheException("Error during waiting connection", e));
        }
    }

    private void processSocket(Socket socket) {
        try {
            FRGetRequest request = FRGetRequest.read(socket.getInputStream());
            FRVideoLogUtils.d("Request to cache proxy:" + request);
            String url = FRProxyCacheUtils.decode(request.uri);
            if (pinger.isPingRequest(url)) {
                pinger.responseToPing(socket);
            } else {
                FRHttpProxyCacheServerClients clients = getClients(url);
                clients.processRequest(request, socket);
            }
        } catch (SocketException e) {
            // There is no way to determine that client closed connection http://stackoverflow.com/a/10241044/999458
            // So just to prevent log flooding don't log stacktrace
            FRVideoLogUtils.e("Closing socket… Socket is closed by client.");
        } catch (FRProxyCacheException | IOException e) {
            onError(new FRProxyCacheException("Error processing request", e));
        } finally {
            releaseSocket(socket);
            FRVideoLogUtils.d("Opened connections: " + getClientsCount());
        }
    }

    private FRHttpProxyCacheServerClients getClients(String url) throws FRProxyCacheException {
        synchronized (clientsLock) {
            FRHttpProxyCacheServerClients clients = clientsMap.get(url);
            if (clients == null) {
                clients = new FRHttpProxyCacheServerClients(url, config, this);
                clientsMap.put(url, clients);
            }
            return clients;
        }
    }

    private int getClientsCount() {
        synchronized (clientsLock) {
            int count = 0;
            for (FRHttpProxyCacheServerClients clients : clientsMap.values()) {
                count += clients.getClientsCount();
            }
            return count;
        }
    }

    private void releaseSocket(Socket socket) {
        closeSocketInput(socket);
        closeSocketOutput(socket);
        closeSocket(socket);
    }

    private void closeSocketInput(Socket socket) {
        try {
            if (!socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        } catch (SocketException e) {
            // There is no way to determine that client closed connection http://stackoverflow.com/a/10241044/999458
            // So just to prevent log flooding don't log stacktrace
            FRVideoLogUtils.e("Releasing input stream… Socket is closed by client.");
        } catch (IOException e) {
            onError(new FRProxyCacheException("Error closing socket input stream", e));
        }
    }

    private void closeSocketOutput(Socket socket) {
        try {
            if (!socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
        } catch (IOException e) {
            FRVideoLogUtils.e("Failed to close socket on proxy side: {}. It seems client have already closed connection.", e);
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            onError(new FRProxyCacheException("Error closing socket", e));
        }
    }

    private void onError(Throwable e) {
        FRVideoLogUtils.e("FRHttpProxyCacheServer error", e);
    }

    @Override
    public void putMime(String url, long length, String mime) {
        urlMimeMap.put(url, new FRUrlMime(length, mime));
    }

    @Override
    public FRUrlMime getMime(String url) {
        return urlMimeMap.get(url);
    }

    private final class WaitRequestsRunnable implements Runnable {

        private final CountDownLatch startSignal;

        public WaitRequestsRunnable(CountDownLatch startSignal) {
            this.startSignal = startSignal;
        }

        @Override
        public void run() {
            startSignal.countDown();
            waitForRequest();
        }
    }

    private final class SocketProcessorRunnable implements Runnable {

        private final Socket socket;

        public SocketProcessorRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            processSocket(socket);
        }
    }

    /**
     * Builder for {@link FRHttpProxyCacheServer}.
     */
    public static final class Builder {

        private static final long DEFAULT_MAX_SIZE = 512 * 1024 * 1024;

        private File cacheRoot;
        private FRFileNameGenerator fileNameGenerator;
        private FRDiskUsage diskUsage;
        private FRSourceInfoStorage sourceInfoStorage;
        private FRHeaderInjector headerInjector;

        public Builder(Context context) {
            this.sourceInfoStorage = FRSourceInfoStorageFactory.newSourceInfoStorage(context);
            this.cacheRoot = FRStorageUtils.getIndividualCacheDirectory(context);
            this.diskUsage = new FRTotalSizeLruDiskUsage(DEFAULT_MAX_SIZE);
            this.fileNameGenerator = new FRMd5FileNameGenerator();
            this.headerInjector = new FREmptyHeadersInjector();
        }

        /**
         * Overrides default cache folder to be used for caching files.
         * <p>
         * By default AndroidVideoCache uses
         * '/Android/data/[app_package_name]/cache/video-cache/' if card is mounted and app has appropriate permission
         * or 'video-cache' subdirectory in default application's cache directory otherwise.
         * </p>
         * <b>Note</b> directory must be used <b>only</b> for AndroidVideoCache files.
         *
         * @param file a cache directory, can't be null.
         * @return a builder.
         */
        public Builder cacheDirectory(File file) {
            this.cacheRoot = FRPreconditions.checkNotNull(file);
            return this;
        }

        /**
         * Overrides default cache file name generator {@link FRMd5FileNameGenerator} .
         *
         * @param fileNameGenerator a new file name generator.
         * @return a builder.
         */
        public Builder fileNameGenerator(FRFileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = FRPreconditions.checkNotNull(fileNameGenerator);
            return this;
        }

        /**
         * Sets max cache size in bytes.
         * <p>
         * All files that exceeds limit will be deleted using LRU strategy.
         * Default value is 512 Mb.
         * </p>
         * Note this method overrides result of calling {@link #maxCacheFilesCount(int)}
         *
         * @param maxSize max cache size in bytes.
         * @return a builder.
         */
        public Builder maxCacheSize(long maxSize) {
            this.diskUsage = new FRTotalSizeLruDiskUsage(maxSize);
            return this;
        }

        /**
         * Sets max cache files count.
         * All files that exceeds limit will be deleted using LRU strategy.
         * Note this method overrides result of calling {@link #maxCacheSize(long)}
         *
         * @param count max cache files count.
         * @return a builder.
         */
        public Builder maxCacheFilesCount(int count) {
            this.diskUsage = new FRTotalCountLruDiskUsage(count);
            return this;
        }

        /**
         * Set custom FRDiskUsage logic for handling when to keep or clean cache.
         *
         * @param diskUsage a disk usage strategy, cant be {@code null}.
         * @return a builder.
         */
        public Builder diskUsage(FRDiskUsage diskUsage) {
            this.diskUsage = FRPreconditions.checkNotNull(diskUsage);
            return this;
        }

        /**
         * Add headers along the request to the server
         *
         * @param headerInjector to inject header base on url
         * @return a builder
         */
        public Builder headerInjector(FRHeaderInjector headerInjector) {
            this.headerInjector = FRPreconditions.checkNotNull(headerInjector);
            return this;
        }

        /**
         * Builds new instance of {@link FRHttpProxyCacheServer}.
         *
         * @return proxy cache. Only single instance should be used across whole app.
         */
        public FRHttpProxyCacheServer build() {
            FRConfig config = buildConfig();
            return new FRHttpProxyCacheServer(config);
        }

        private FRConfig buildConfig() {
            return new FRConfig(cacheRoot, fileNameGenerator, diskUsage, sourceInfoStorage, headerInjector);
        }

    }
}
