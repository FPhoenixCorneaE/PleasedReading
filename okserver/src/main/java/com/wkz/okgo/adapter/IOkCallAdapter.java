package com.wkz.okgo.adapter;

/**
 * 返回值的适配器
 */
public interface IOkCallAdapter<T, R> {

    /** call执行的代理方法 */
    R adapt(IOkCall<T> call, OkAdapterParams param);
}
