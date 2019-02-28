package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkNoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Get请求的实现类，注意需要传入本类的泛型
 */
public class OkGetRequest<T> extends OkNoBodyRequest<T, OkGetRequest<T>> {

    private static final long serialVersionUID = -851232844779546388L;

    public OkGetRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.GET;
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}
