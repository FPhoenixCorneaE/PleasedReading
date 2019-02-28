package com.wkz.okgo.adapter;


import com.wkz.okgo.callback.IOkCallback;
import com.wkz.okgo.model.OkResponse;
import com.wkz.okgo.request.base.OkRequest;

/**
 * 请求的包装类
 */
public interface IOkCall<T> {
    /**
     * 同步执行
     */
    OkResponse<T> execute() throws Exception;

    /**
     * 异步回调执行
     */
    void execute(IOkCallback<T> callback);

    /**
     * 是否已经执行
     */
    boolean isExecuted();

    /**
     * 取消
     */
    void cancel();

    /**
     * 是否取消
     */
    boolean isCanceled();

    IOkCall<T> clone();

    OkRequest getRequest();
}
