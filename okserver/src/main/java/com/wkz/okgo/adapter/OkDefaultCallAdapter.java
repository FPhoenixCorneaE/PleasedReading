package com.wkz.okgo.adapter;

/**
 * 默认的工厂处理,不对返回值做任何操作
 */
public class OkDefaultCallAdapter<T> implements IOkCallAdapter<T, IOkCall<T>> {

    @Override
    public IOkCall<T> adapt(IOkCall<T> call, OkAdapterParams param) {
        return call;
    }
}
