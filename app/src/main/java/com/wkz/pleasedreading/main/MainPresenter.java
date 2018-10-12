package com.wkz.pleasedreading.main;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;

public class MainPresenter extends BasePresenter implements MainContract.IMainPresenter {

    public MainPresenter(BaseView view, LifecycleProvider provider) {
        super(view, provider);
    }
}
