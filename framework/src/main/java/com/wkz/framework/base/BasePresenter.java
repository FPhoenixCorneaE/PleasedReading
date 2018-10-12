package com.wkz.framework.base;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

public class BasePresenter {

    private BaseView mView;
    private BaseModel mModel;
    private LifecycleProvider mProvider;

    public BasePresenter(BaseView view, LifecycleProvider provider) {
        this.mView = view;
        this.mProvider = provider;
        this.mModel = mView.createModel();
    }

    /**
     * 在Activity里取消订阅
     *
     * @return
     */
    public LifecycleTransformer bindUntilActivityEvent() {
        return mProvider.bindUntilEvent(ActivityEvent.DESTROY);
    }

    /**
     * 在Fragment里取消订阅
     *
     * @return
     */
    public LifecycleTransformer bindUntilFragmentEvent() {
        return mProvider.bindUntilEvent(FragmentEvent.DESTROY);
    }

    public BaseView getView() {
        return mView;
    }

    public BaseModel getModel() {
        return mModel;
    }

    public LifecycleProvider getProvider() {
        return mProvider;
    }
}
