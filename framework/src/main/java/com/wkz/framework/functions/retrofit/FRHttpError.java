package com.wkz.framework.functions.retrofit;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FRHttpError.ERROR_CONNECT,
        FRHttpError.ERROR_HTTP,
        FRHttpError.ERROR_PARSE,
        FRHttpError.ERROR_SERVER,
        FRHttpError.ERROR_UNKNOWN
})
@Retention(RetentionPolicy.SOURCE)
public @interface FRHttpError {
    String MESSAGE_CONNECT = "连接错误";
    String MESSAGE_HTTP = "网络错误";
    String MESSAGE_PARSE = "数据解析错误";
    String MESSAGE_SERVER = "服务器错误";
    String MESSAGE_UNKNOWN = "未知错误";
    int ERROR_CONNECT = 1001;
    int ERROR_HTTP = 1002;
    int ERROR_PARSE = 1003;
    int ERROR_SERVER = 1004;
    int ERROR_UNKNOWN = -1;
}
