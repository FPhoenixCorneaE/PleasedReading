package com.wkz.framework.base;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class BasePresenter<V extends BaseView> {

    public V mView;
    private LifecycleProvider<ActivityEvent> mProvider;


    public BasePresenter(V view, LifecycleProvider<ActivityEvent> provider) {
        this.mView = view;
        this.mProvider = provider;
    }

    public LifecycleProvider<ActivityEvent> getProvider() {
        return mProvider;
    }
}
