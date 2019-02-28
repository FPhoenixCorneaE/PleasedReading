package com.wkz.okgo.exception;

public class OkStorageException extends Exception {

    private static final long serialVersionUID = 3971711351734988564L;

    public OkStorageException() {
    }

    public OkStorageException(String detailMessage) {
        super(detailMessage);
    }

    public OkStorageException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OkStorageException(Throwable throwable) {
        super(throwable);
    }

    public static OkStorageException NOT_AVAILABLE() {
        return new OkStorageException("SDCard isn't available, please check SD card and permission: WRITE_EXTERNAL_STORAGE, and you must pay attention to Android6.0 RunTime Permissions!");
    }
}
