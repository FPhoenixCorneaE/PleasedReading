package com.wkz.pleasedreading.main;

import com.wkz.framework.base.FRBasePresenter;

public class PRMainPresenter extends FRBasePresenter<PRMainContract.IMainView, PRMainContract.IMainModel> implements PRMainContract.IMainPresenter {

    public PRMainPresenter(PRMainContract.IMainView view) {
        super(view);
    }
}
