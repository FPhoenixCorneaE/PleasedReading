package com.wkz.okgo.request;

import com.wkz.okgo.model.OkHttpMethod;
import com.wkz.okgo.request.base.OkBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Post请求的实现类，注意需要传入本类的泛型
 */
public class OkPostRequest<T> extends OkBodyRequest<T, OkPostRequest<T>> {

    private static final long serialVersionUID = 6997034604931075789L;

    public OkPostRequest(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return OkHttpMethod.POST;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}
