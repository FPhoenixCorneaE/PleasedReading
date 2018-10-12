package com.wkz.pleasedreading.gank;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public class GankPresenter extends BasePresenter implements GankContract.IGankPresenter {

    private GankContract.IGankView mView;
    private GankContract.IGankModel mModel;

    public GankPresenter(BaseView view, LifecycleProvider provider) {
        super(view, provider);
        mView = (GankContract.IGankView) getView();
        mModel = (GankContract.IGankModel) getModel();
    }

    @Override
    public void getDataByType(String type, int pageNum, int pageCount) {
        mModel.getDataByType(type, pageNum, pageCount, bindUntilFragmentEvent(), new OnFRHttpCallback<GankBean>() {
            @Override
            public void onSuccess(GankBean data) {
                Logger.i(data.toString());
            }

            @Override
            public void onFailure(String msg) {
                Logger.e(msg);
            }
        });
    }
}
