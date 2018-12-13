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
import com.wkz.framework.constant.FRConstant;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.functions.web.FRWebPageActivity;
import com.wkz.framework.model.FRBundle;
import com.wkz.framework.utils.IntentUtils;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.DividerDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constant.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentGankChildBinding;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;
import com.wkz.viewer.widget.FRImageViewer;

import java.util.List;

public class PRGankChildFragment extends BaseFragment implements IGankView, FRBaseRecyclerAdapter.OnLoadMoreListener, OnRefreshListener, FRBaseRecyclerAdapter.OnItemClickListener<PRGankBean.ResultsBean> {

    private PRGankPresenter mPresenter;
    private PrFragmentGankChildBinding mDataBinding;
    private PRGankChildRecyclerAdapter mPRGankChildRecyclerAdapter;
    private String mTitle;
    private FRImageViewer mFrImageViewer;

    public static PRGankChildFragment create(String title) {
        PRGankChildFragment gankChildFragment = new PRGankChildFragment();
        gankChildFragment.setArguments(new FRBundle().putString(PRConstant.PR_FRAGMENT_TITLE, title).create());
        return gankChildFragment;
    }

    public PRGankChildFragment setFrImageViewer(FRImageViewer mFrImageViewer) {
        this.mFrImageViewer = mFrImageViewer;
        return this;
    }

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
                ResourceUtils.getColor(R.color.fr_color_transparent),
                SizeUtils.dp2px(0f)
        );
        mDataBinding.prRvGankChild.addItemDecoration(dividerDecoration);
        mDataBinding.prRvGankChild.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBinding.prRvGankChild.setAdapter(mPRGankChildRecyclerAdapter =
                ((PRGankChildRecyclerAdapter)
                        new PRGankChildRecyclerAdapter(mContext, null, true)
                                .setOnLoadMoreListener(this))
                        .setFRImageViewer(mFrImageViewer)
        );
    }

    @Override
    public void initListener() {
        mPRGankChildRecyclerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitle = getArguments().getString(PRConstant.PR_FRAGMENT_TITLE);
        } else {
            mTitle = "";
        }
        mPresenter.getDataByType(mTitle);
    }

    @SuppressWarnings("unchecked")
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
        mPresenter.onLoadMoreDataByType(mTitle);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.onRefreshDataByType(mTitle);
    }

    @Override
    public void onItemClick(FRRecyclerViewHolder fRRecyclerViewHolder, PRGankBean.ResultsBean data, int position) {
        IntentUtils.startActivity(mContext, FRWebPageActivity.class, new FRBundle().putString(FRConstant.WEB_URL, data.getUrl()).create());
    }

    @Override
    public boolean onBackPressed() {
        return mFrImageViewer != null && mFrImageViewer.onBackPressed();
    }
}