package com.wkz.okgo.exception;

public class OkFileException extends Exception {

    private static final long serialVersionUID = -2079713855989533870L;

    public OkFileException(String detailMessage) {
        super(detailMessage);
    }

    public static OkFileException UNKNOWN() {
        return new OkFileException("unknown exception!");
    }

    public static OkFileException BREAKPOINT_NOT_EXIST() {
        return new OkFileException("breakpoint file does not exist!");
    }

    public static OkFileException BREAKPOINT_EXPIRED() {
        return new OkFileException("breakpoint file has expired!");
    }
}
