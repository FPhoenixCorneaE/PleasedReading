package com.wkz.okgo.request.base;

import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.utils.OkHttpUtils;
import com.wkz.okgo.utils.OkLogger;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 包装的请求体，处理进度，可以处理任何的 RequestBody
 */
public class OkProgressRequestBody<T> extends RequestBody {

    private RequestBody requestBody;         //实际的待包装请求体
    private IOkCallback<T> callback;
    private UploadInterceptor interceptor;

    OkProgressRequestBody(RequestBody requestBody, IOkCallback<T> callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    /**
     * 重写调用实际的响应体的contentType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            OkLogger.printStackTrace(e);
            return -1;
        }
    }

    /**
     * 重写进行写入
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    /**
     * 包装
     */
    private final class CountingSink extends ForwardingSink {

        private OkProgress progress;

        CountingSink(Sink delegate) {
            super(delegate);
            progress = new OkProgress();
            progress.totalSize = contentLength();
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            OkProgress.changeProgress(progress, byteCount, new OkProgress.Action() {
                @Override
                public void call(OkProgress progress) {
                    if (interceptor != null) {
                        interceptor.uploadProgress(progress);
                    } else {
                        onProgress(progress);
                    }
                }
            });
        }
    }

    private void onProgress(final OkProgress progress) {
        OkHttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.uploadProgress(progress);
                }
            }
        });
    }

    public void setInterceptor(UploadInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public interface UploadInterceptor {
        void uploadProgress(OkProgress progress);
    }
}
