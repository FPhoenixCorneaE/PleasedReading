package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Put请求
 *
 * @param <T>
 */
public class OkPutRequest<T> extends OkBodyRequest<T, OkPutRequest<T>> {

    private static final long serialVersionUID = 3445593630232290129L;

    public OkPutRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.PUT;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.put(requestBody).url(url).tag(tag).build();
    }
}
