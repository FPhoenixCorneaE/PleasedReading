package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Patch请求
 *
 * @param <T>
 */
public class OkPatchRequest<T> extends OkBodyRequest<T, OkPatchRequest<T>> {

    private static final long serialVersionUID = -5713307239110443928L;

    public OkPatchRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.PATCH;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.patch(requestBody).url(url).tag(tag).build();
    }
}
