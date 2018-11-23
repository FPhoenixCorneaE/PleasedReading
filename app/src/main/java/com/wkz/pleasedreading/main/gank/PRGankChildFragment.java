package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.DividerDecoration;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrFragmentGankChildBinding;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;

import java.util.List;

public class PRGankChildFragment extends BaseFragment implements IGankView {

    private PRGankPresenter mPresenter;
    private PrFragmentGankChildBinding mDataBinding;
    private PRGankChildRecyclerAdapter mPRGankChildRecyclerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_gank_child;
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
        mDataBinding = (PrFragmentGankChildBinding) mViewDataBinding;
        DividerDecoration dividerDecoration = new DividerDecoration(
                ResourceUtils.getColor(R.color.fr_divider_Oxffededed),
                SizeUtils.dp2px(5f)
        );
        mDataBinding.prRvGankChild.addItemDecoration(dividerDecoration);
        mDataBinding.prRvGankChild.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBinding.prRvGankChild.setAdapter(mPRGankChildRecyclerAdapter =
                new PRGankChildRecyclerAdapter(mContext,
                        null,
                        true)
        );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDataByType("Android", 10, 1);
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        mPRGankChildRecyclerAdapter.setLoadMoreData((List<PRGankBean.ResultsBean>) data);
    }
}
