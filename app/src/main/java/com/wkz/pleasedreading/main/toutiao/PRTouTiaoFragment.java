package com.wkz.pleasedreading.main.toutiao;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wkz.framework.base.FRBaseFragment;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.pleasedreading.R;

public class PRTouTiaoFragment extends FRBaseFragment<PRTouTiaoPresenter> implements PRTouTiaoContract.ITouTiaoView {

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_toutiao;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRTouTiaoPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRTouTiaoModel.class);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}
