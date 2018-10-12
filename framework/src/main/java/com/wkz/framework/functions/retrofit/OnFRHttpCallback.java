package com.wkz.framework.functions.retrofit;

public interface OnFRHttpCallback<M> {
    void onSuccess(M data);

    void onFailure(String msg);
}
