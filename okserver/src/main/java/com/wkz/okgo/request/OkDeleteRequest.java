package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Delete请求
 * @param <T>
 */
public class OkDeleteRequest<T> extends OkBodyRequest<T, OkDeleteRequest<T>> {

    private static final long serialVersionUID = 423669594898123096L;

    public OkDeleteRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.DELETE;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.delete(requestBody).url(url).tag(tag).build();
    }
}
