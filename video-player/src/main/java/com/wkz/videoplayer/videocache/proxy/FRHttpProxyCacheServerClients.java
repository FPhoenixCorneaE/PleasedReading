package com.wkz.videoplayer.videocache.proxy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wkz.videoplayer.videocache.FRConfig;
import com.wkz.videoplayer.videocache.FRGetRequest;
import com.wkz.videoplayer.videocache.FRPreconditions;
import com.wkz.videoplayer.videocache.OnCacheListener;
import com.wkz.videoplayer.videocache.file.FRFileCache;
import com.wkz.videoplayer.videocache.source.FROkHttpUrlSource;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client for {@link FRHttpProxyCacheServer}
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
final class FRHttpProxyCacheServerClients {

    private final AtomicInteger clientsCount = new AtomicInteger(0);
    private final String url;
    private volatile FRHttpProxyCache proxyCache;
    private final List<OnCacheListener> listeners = new CopyOnWriteArrayList<>();
    private final OnCacheListener uiCacheListener;
    private final FRConfig config;
    private IFRMimeCache iMimeCache;

    public FRHttpProxyCacheServerClients(String url, FRConfig config, IFRMimeCache iMimeCache) {
        this.url = FRPreconditions.checkNotNull(url);
        this.config = FRPreconditions.checkNotNull(config);
        this.iMimeCache = iMimeCache;
        this.uiCacheListener = new UiListenerHandler(url, listeners);
    }

    public void processRequest(FRGetRequest request, Socket socket) throws FRProxyCacheException, IOException {
        startProcessRequest();
        try {
            clientsCount.incrementAndGet();
            proxyCache.processRequest(request, socket);
        } finally {
            finishProcessRequest();
        }
    }

    private synchronized void startProcessRequest() throws FRProxyCacheException {
        proxyCache = proxyCache == null ? newHttpProxyCache() : proxyCache;
    }

    private synchronized void finishProcessRequest() {
        if (clientsCount.decrementAndGet() <= 0) {
            proxyCache.shutdown();
            proxyCache = null;
        }
    }

    public void registerCacheListener(OnCacheListener cacheListener) {
        listeners.add(cacheListener);
    }

    public void unregisterCacheListener(OnCacheListener cacheListener) {
        listeners.remove(cacheListener);
    }

    public void shutdown() {
        listeners.clear();
        if (proxyCache != null) {
            proxyCache.registerCacheListener(null);
            proxyCache.shutdown();
            proxyCache = null;
        }
        clientsCount.set(0);
    }

    public int getClientsCount() {
        return clientsCount.get();
    }

    private FRHttpProxyCache newHttpProxyCache() throws FRProxyCacheException {
        FROkHttpUrlSource source = new FROkHttpUrlSource(iMimeCache, url);
        FRFileCache cache = new FRFileCache(config.generateCacheFile(url), config.diskUsage);
        FRHttpProxyCache httpProxyCache = new FRHttpProxyCache(source, cache);
        httpProxyCache.registerCacheListener(uiCacheListener);
        return httpProxyCache;
    }

    private static final class UiListenerHandler extends Handler implements OnCacheListener {

        private final String url;
        private final List<OnCacheListener> listeners;

        public UiListenerHandler(String url, List<OnCacheListener> listeners) {
            super(Looper.getMainLooper());
            this.url = url;
            this.listeners = listeners;
        }

        @Override
        public void onCacheAvailable(File file, String url, int percentsAvailable) {
            Message message = obtainMessage();
            message.arg1 = percentsAvailable;
            message.obj = file;
            sendMessage(message);
        }

        @Override
        public void handleMessage(Message msg) {
            for (OnCacheListener cacheListener : listeners) {
                cacheListener.onCacheAvailable((File) msg.obj, url, msg.arg1);
            }
        }
    }
}
