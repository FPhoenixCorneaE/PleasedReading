package com.wkz.framework.functions.retrofit;

import java.io.Serializable;

public class FRHttpException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7111990242230796361L;
    private int mCode;
    private String mErrorMessage;
    private String mDetailMessage;

    public FRHttpException(int mCode, String mErrorMessage, String mDetailMessage) {
        this.mCode = mCode;
        this.mErrorMessage = mErrorMessage;
        this.mDetailMessage = mDetailMessage;
    }

    public int getCode() {
        return mCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getDetailMessage() {
        return mDetailMessage;
    }
}
