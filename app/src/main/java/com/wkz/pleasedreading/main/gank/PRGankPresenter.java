package com.wkz.pleasedreading.main.gank;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.FRHttpError;
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
        mView.showLoading();
        mModel.getDataByType(type, pageNum, pageCount, bindUntilFragmentEvent(), new OnFRHttpCallback<PRGankBean>() {
            @Override
            public void onSuccess(PRGankBean data) {
                if (mView != null) {
                    if (!data.isError()) {
                        mView.onSuccess(data.getResults());
                    } else {
                        mView.onFailure(FRHttpError.ERROR_UNKNOWN, FRHttpError.MESSAGE_UNKNOWN);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (mView != null) {
                    mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
                }
            }
        });
    }
}
