package com.wkz.framework.base;

public class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V mView;
    protected M mModel;

    public BasePresenter(V view) {
        this.mView = view;
        this.mModel = (M) mView.createModel();
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }
}
