package com.wkz.pleasedreading.main;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;

public class PRMainPresenter extends BasePresenter implements PRMainContract.IMainPresenter {

    public PRMainPresenter(BaseView view, LifecycleProvider provider) {
        super(view, provider);
    }
}
