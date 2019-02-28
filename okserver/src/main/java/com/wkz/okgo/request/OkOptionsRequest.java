package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Options请求
 */
public class OkOptionsRequest<T> extends OkBodyRequest<T, OkOptionsRequest<T>> {

    private static final long serialVersionUID = 5026947211887721002L;

    public OkOptionsRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.OPTIONS;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}
