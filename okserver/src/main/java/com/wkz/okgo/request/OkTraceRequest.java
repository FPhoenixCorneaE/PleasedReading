package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkNoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Trace请求的实现类，注意需要传入本类的泛型
 */
public class OkTraceRequest<T> extends OkNoBodyRequest<T, OkTraceRequest<T>> {

    private static final long serialVersionUID = 2900079550304598585L;

    public OkTraceRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.TRACE;
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.method("TRACE", requestBody).url(url).tag(tag).build();
    }
}
