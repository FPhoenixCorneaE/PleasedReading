package com.wkz.okgo.request.base;

import com.wkz.okgo.utils.OkHttpUtils;

import okhttp3.RequestBody;

public abstract class OkNoBodyRequest<T, R extends OkNoBodyRequest> extends OkRequest<T, R> {
    private static final long serialVersionUID = 1200621102761691196L;

    public OkNoBodyRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    protected okhttp3.Request.Builder generateRequestBuilder(RequestBody requestBody) {
        url = OkHttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        return OkHttpUtils.appendHeaders(requestBuilder, headers);
    }
}
