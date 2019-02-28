package com.wkz.okgo.exception;

import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.utils.OkHttpUtils;

public class OkHttpException extends RuntimeException {

    private static final long serialVersionUID = 10889500014491961L;

    private int code;                               //HTTP status code
    private String message;                         //HTTP status message
    private transient OkResponse<?> response;       //The full HTTP response. This may be null if the exception was serialized

    public OkHttpException(String message) {
        super(message);
    }

    public OkHttpException(OkResponse<?> response) {
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }

    private static String getMessage(OkResponse<?> response) {
        OkHttpUtils.checkNotNull(response, "response == null");
        return "HTTP " + response.code() + " " + response.message();
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public OkResponse<?> response() {
        return response;
    }

    public static OkHttpException NET_ERROR() {
        return new OkHttpException("network error! http response code is 404 or 5xx!");
    }

    public static OkHttpException COMMON(String message) {
        return new OkHttpException(message);
    }
}
