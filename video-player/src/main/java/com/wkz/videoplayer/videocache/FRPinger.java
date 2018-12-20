package com.wkz.videoplayer.videocache;

import com.wkz.videoplayer.utils.FRVideoLogUtils;
import com.wkz.videoplayer.videocache.proxy.FRProxyCacheException;
import com.wkz.videoplayer.videocache.proxy.IFRMimeCache;
import com.wkz.videoplayer.videocache.source.FROkHttpUrlSource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Pings {@link com.wkz.videoplayer.videocache.proxy.FRHttpProxyCacheServer} to make sure it works.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */

public class FRPinger {

    private static final String PING_REQUEST = "ping";
    private static final String PING_RESPONSE = "ping ok";

    private final ExecutorService pingExecutor = Executors.newSingleThreadExecutor();
    private final String host;
    private final int port;
    private IFRMimeCache mimeCache;

    public FRPinger(String host, int port, IFRMimeCache mimeCache) {
        this.host = FRPreconditions.checkNotNull(host);
        this.port = port;
        this.mimeCache = mimeCache;
    }

    public boolean ping(int maxAttempts, int startTimeout) {
        FRPreconditions.checkArgument(maxAttempts >= 1);
        FRPreconditions.checkArgument(startTimeout > 0);

        int timeout = startTimeout;
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                Future<Boolean> pingFuture = pingExecutor.submit(new PingCallable());
                boolean pinged = pingFuture.get(timeout, TimeUnit.MILLISECONDS);
                if (pinged) {
                    return true;
                }
            } catch (TimeoutException e) {
                FRVideoLogUtils.w("Error pinging server (attempt: " + attempts + ", timeout: " + timeout + "). ");
            } catch (InterruptedException | ExecutionException e) {
                FRVideoLogUtils.e("Error pinging server due to unexpected error", e);
            }
            attempts++;
            timeout *= 2;
        }
        String error = String.format(Locale.US, "Error pinging server (attempts: %d, max timeout: %d). " +
                        "If you see this message, please, report at https://github.com/danikula/AndroidVideoCache/issues/134. " +
                        "Default proxies are: %s"
                , attempts, timeout / 2, getDefaultProxies());
        FRVideoLogUtils.e(error, new FRProxyCacheException(error));
        return false;
    }

    private List<Proxy> getDefaultProxies() {
        try {
            ProxySelector defaultProxySelector = ProxySelector.getDefault();
            return defaultProxySelector.select(new URI(getPingUrl()));
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isPingRequest(String request) {
        return PING_REQUEST.equals(request);
    }

    public void responseToPing(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write("HTTP/1.1 200 OK\n\n".getBytes());
        out.write(PING_RESPONSE.getBytes());
    }

    private boolean pingServer() throws FRProxyCacheException {
        String pingUrl = getPingUrl();
        FROkHttpUrlSource source = new FROkHttpUrlSource(mimeCache, pingUrl);
        try {
            byte[] expectedResponse = PING_RESPONSE.getBytes();
            source.open(0);
            byte[] response = new byte[expectedResponse.length];
            source.read(response);
            boolean pingOk = Arrays.equals(expectedResponse, response);
            FRVideoLogUtils.i("Ping response: `" + new String(response) + "`, pinged? " + pingOk);
            return pingOk;
        } catch (FRProxyCacheException e) {
            FRVideoLogUtils.e("Error reading ping response", e);
            return false;
        } finally {
            source.close();
        }
    }

    private String getPingUrl() {
        return String.format(Locale.US, "http://%s:%d/%s", host, port, PING_REQUEST);
    }

    private class PingCallable implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            return pingServer();
        }
    }

}
