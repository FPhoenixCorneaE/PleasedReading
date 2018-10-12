package com.wkz.pleasedreading.gank;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BaseView;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;

public interface GankContract {

    interface IGankView extends BaseView {

    }

    interface IGankPresenter {
        void getDataByType(String type, int pageNum, int pageCount);
    }

    interface IGankModel extends BaseModel {
        void getDataByType(String type, int pageNum, int pageCount, LifecycleTransformer lifecycleTransformer, OnFRHttpCallback<GankBean> callback);
    }
}
