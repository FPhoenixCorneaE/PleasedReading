package com.wkz.pleasedreading.main;

import com.wkz.framework.base.BasePresenter;

public class PRMainPresenter extends BasePresenter<PRMainContract.IMainView, PRMainContract.IMainModel> implements PRMainContract.IMainPresenter {

    public PRMainPresenter(PRMainContract.IMainView view) {
        super(view);
    }
}
