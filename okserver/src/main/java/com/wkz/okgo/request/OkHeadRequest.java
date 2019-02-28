package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkNoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Head请求
 * @param <T>
 */
public class OkHeadRequest<T> extends OkNoBodyRequest<T, OkHeadRequest<T>> {

    private static final long serialVersionUID = -4487107797563880983L;

    public OkHeadRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.HEAD;
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.head().url(url).tag(tag).build();
    }
}
