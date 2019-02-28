package com.wkz.okgo.model;

import okhttp3.Call;
import okhttp3.Headers;

/**
 * 响应体的包装类
 */
public final class OkResponse<T> {

    private T body;
    private Throwable throwable;
    private boolean isFromCache;
    private okhttp3.Call rawCall;
    private okhttp3.Response rawResponse;

    public static <T> OkResponse<T> success(boolean isFromCache, T body, Call rawCall, okhttp3.Response rawResponse) {
        OkResponse<T> response = new OkResponse<>();
        response.setFromCache(isFromCache);
        response.setBody(body);
        response.setRawCall(rawCall);
        response.setRawResponse(rawResponse);
        return response;
    }

    public static <T> OkResponse<T> error(boolean isFromCache, Call rawCall, okhttp3.Response rawResponse, Throwable throwable) {
        OkResponse<T> response = new OkResponse<>();
        response.setFromCache(isFromCache);
        response.setRawCall(rawCall);
        response.setRawResponse(rawResponse);
        response.setException(throwable);
        return response;
    }

    public OkResponse() {
    }

    public int code() {
        if (rawResponse == null) return -1;
        return rawResponse.code();
    }

    public String message() {
        if (rawResponse == null) return null;
        return rawResponse.message();
    }

    public Headers headers() {
        if (rawResponse == null) return null;
        return rawResponse.headers();
    }

    public boolean isSuccessful() {
        return throwable == null;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public T body() {
        return body;
    }

    public Throwable getException() {
        return throwable;
    }

    public void setException(Throwable exception) {
        this.throwable = exception;
    }

    public Call getRawCall() {
        return rawCall;
    }

    public void setRawCall(Call rawCall) {
        this.rawCall = rawCall;
    }

    public okhttp3.Response getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(okhttp3.Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public void setFromCache(boolean fromCache) {
        isFromCache = fromCache;
    }
}
