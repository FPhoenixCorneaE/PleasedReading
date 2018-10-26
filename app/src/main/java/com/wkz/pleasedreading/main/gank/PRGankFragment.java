package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;

public class PRGankFragment extends BaseFragment implements IGankView {

    private PRGankPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_gank;
    }

    @NonNull
    @Override
    public BasePresenter createPresenter() {
        return mPresenter = new PRGankPresenter(this, this);
    }

    @Override
    public BaseModel createModel() {
        return ModelFactory.createModel(PRGankModel.class);
    }

    @Override
    public void initView() {
//        ((PrFragmentGankBinding)mViewDataBinding).setPrGankBean();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDataByType("Android", 10, 1);
    }
}
