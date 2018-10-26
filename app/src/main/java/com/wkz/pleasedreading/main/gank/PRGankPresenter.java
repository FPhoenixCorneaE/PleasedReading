package com.wkz.pleasedreading.main.gank;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class PRGankPresenter extends BasePresenter implements PRGankContract.IGankPresenter {

    private PRGankContract.IGankView mView;
    private PRGankContract.IGankModel mModel;

    public PRGankPresenter(BaseView view, LifecycleProvider provider) {
        super(view, provider);
        mView = (PRGankContract.IGankView) getView();
        mModel = (PRGankContract.IGankModel) getModel();
    }

    @Override
    public void getDataByType(String type, int pageNum, int pageCount) {
        mModel.getDataByType(type, pageNum, pageCount, bindUntilFragmentEvent(), new OnFRHttpCallback<PRGankBean>() {
            @Override
            public void onSuccess(PRGankBean data) {
                Logger.i(data.toString());
            }

            @Override
            public void onFailure(String msg) {
                Logger.e(msg);
            }
        });
    }
}
