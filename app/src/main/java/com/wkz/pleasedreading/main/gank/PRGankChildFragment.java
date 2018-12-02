package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.DividerDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrFragmentGankChildBinding;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;

import java.util.List;

public class PRGankChildFragment extends BaseFragment implements IGankView, FRBaseRecyclerAdapter.OnLoadMoreListener, OnRefreshListener {

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
        initSmartRefreshLayout();
        initRecyclerView();
    }

    private void initSmartRefreshLayout() {
        //内容跟随偏移
        mDataBinding.prSrlRefresh.setEnableHeaderTranslationContent(true);
        //颜色主题
        mDataBinding.prSrlRefresh.setPrimaryColorsId(R.color.pr_actionBar, R.color.fr_color_white);
        //设置Header为 弹出圆圈 样式
        mDataBinding.prSrlRefresh.setRefreshHeader(new BezierCircleHeader(mContext));
        //刷新监听
        mDataBinding.prSrlRefresh.setOnRefreshListener(this);
        //自动刷新
        mDataBinding.prSrlRefresh.autoRefresh();
    }

    private void initRecyclerView() {
        DividerDecoration dividerDecoration = new DividerDecoration(
                ResourceUtils.getColor(R.color.fr_divider_Oxffededed),
                SizeUtils.dp2px(5f)
        );
        mDataBinding.prRvGankChild.addItemDecoration(dividerDecoration);
        mDataBinding.prRvGankChild.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBinding.prRvGankChild.setAdapter(mPRGankChildRecyclerAdapter =
                ((PRGankChildRecyclerAdapter)
                        new PRGankChildRecyclerAdapter(mContext, null, true)
                                .setOnLoadMoreListener(this))
                        .setFRImageViewer(mDataBinding.prIvViewer)
        );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDataByType("Android");
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        if (RefreshState.Refreshing == mDataBinding.prSrlRefresh.getState()) {
            mDataBinding.prSrlRefresh.finishRefresh();
            mPRGankChildRecyclerAdapter.setNewData((List<PRGankBean.ResultsBean>) data);
        } else {
            mPRGankChildRecyclerAdapter.setLoadMoreData((List<PRGankBean.ResultsBean>) data);
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
        if (RefreshState.Refreshing == mDataBinding.prSrlRefresh.getState()) {
            mDataBinding.prSrlRefresh.finishRefresh();
        }
    }

    @Override
    public void onLoadMore(boolean isReload) {
        mPresenter.onLoadMoreDataByType("Android");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.onRefreshDataByType("Android");
    }
}
