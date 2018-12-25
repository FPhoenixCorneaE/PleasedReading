package com.wkz.framework.base;

public class FRBasePresenter<V extends IFRBaseView, M extends IFRBaseModel> {

    protected V mView;
    protected M mModel;

    public FRBasePresenter(V view) {
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
