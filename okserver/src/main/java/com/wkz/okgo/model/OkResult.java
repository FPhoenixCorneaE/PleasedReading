package com.wkz.okgo.model;

import java.io.IOException;

public final class OkResult<T> {
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static <T> OkResult<T> error(Throwable error) {
        if (error == null) throw new NullPointerException("error == null");
        return new OkResult<>(null, error);
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static <T> OkResult<T> response(OkResponse<T> response) {
        if (response == null) throw new NullPointerException("response == null");
        return new OkResult<>(response, null);
    }

    private final OkResponse<T> response;
    private final Throwable error;

    private OkResult(OkResponse<T> response, Throwable error) {
        this.response = response;
        this.error = error;
    }

    /**
     * The response received from executing an HTTP request. Only present when {@link #isError()} is
     * false, null otherwise.
     */

    public OkResponse<T> response() {
        return response;
    }

    /**
     * The error experienced while attempting to execute an HTTP request. Only present when {@link
     * #isError()} is true, null otherwise.
     * If the error is an {@link IOException} then there was a problem with the transport to the
     * remote server. Any other exception type indicates an unexpected failure and should be
     * considered fatal (configuration error, programming error, etc.).
     */

    public Throwable error() {
        return error;
    }

    /**
     * {@code true} if the request resulted in an error. See {@link #error()} for the cause.
     */
    public boolean isError() {
        return error != null;
    }

    @Override
    public String toString() {
        if (error != null) {
            return "OkResult{isError=true, error=\"" + error + "\"}";
        }
        return "OkResult{isError=false, response=" + response + '}';
    }
}
