package com.wkz.framework.bases;

import androidx.annotation.NonNull;

public class FRBasePresenter<V extends IFRBaseView, M extends IFRBaseModel> {

    protected V mView;
    protected M mModel;

    public FRBasePresenter(@NonNull V view) {
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
