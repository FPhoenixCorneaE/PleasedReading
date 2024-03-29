package com.wkz.pleasedreading.main.gank.welfare;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wkz.framework.constants.FRConstant;
import com.wkz.framework.functions.imageviewer.FRImageViewerActivity;
import com.wkz.framework.models.FRActivityAnimator;
import com.wkz.framework.models.FRBundle;
import com.wkz.framework.utils.IntentUtils;
import com.wkz.pleasedreading.main.gank.PRGankBean;
import com.wkz.pleasedreading.main.gank.PRGankFragment;
import com.wkz.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.FRSpaceDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constants.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentGankChildBinding;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.widget.FRImageViewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 福利
 */
public class PRGankWelfareFragment extends PRGankFragment implements IGankView, FRBaseRecyclerAdapter.OnLoadMoreListener, OnRefreshListener, FRBaseRecyclerAdapter.OnItemClickListener<String> {

    private PrFragmentGankChildBinding mDataBinding;
    private PRGankWelfareRecyclerAdapter mPRGankWelfareRecyclerAdapter;
    private String mTitle;
    private ArrayList<FRViewData> mViewList = new ArrayList<>();
    private FRImageViewer mFrImageViewer;

    public void setImageViewer(FRImageViewer mFrImageViewer) {
        this.mFrImageViewer = mFrImageViewer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_gank_child;
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
        FRSpaceDecoration spaceDecoration = new FRSpaceDecoration(SizeUtils.dp2px(10f));
        mDataBinding.prRvGankChild.addItemDecoration(spaceDecoration);
        //定义瀑布流管理器，第一个参数是列数，第二个是方向。
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //不设置的话，图片闪烁错位，有可能有整列错位的情况。
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mDataBinding.prRvGankChild.setLayoutManager(staggeredGridLayoutManager);
        mDataBinding.prRvGankChild.setAdapter(mPRGankWelfareRecyclerAdapter =
                ((PRGankWelfareRecyclerAdapter)
                        new PRGankWelfareRecyclerAdapter(mContext, null, true)
                                .setOnLoadMoreListener(this))
        );
        mDataBinding.prRvGankChild.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
                staggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });
        staggeredGridLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void initListener() {
        mPRGankWelfareRecyclerAdapter.setOnItemClickListener(this);
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
            mPRGankWelfareRecyclerAdapter.setNewData(new ArrayList<String>() {
                private static final long serialVersionUID = -9200608729711758350L;

                {
                    if (data != null) {
                        mViewList.clear();
                        for (PRGankBean.ResultsBean bean : (List<PRGankBean.ResultsBean>) data) {
                            add(bean.getUrl());
                            mViewList.add(new FRViewData());
                        }
                    }
                }
            });
        } else {
            mPRGankWelfareRecyclerAdapter.setLoadMoreData(new ArrayList<String>() {
                private static final long serialVersionUID = -2567374945723978378L;

                {
                    if (data != null) {
                        for (PRGankBean.ResultsBean bean : (List<PRGankBean.ResultsBean>) data) {
                            add(bean.getUrl());
                            mViewList.add(new FRViewData());
                        }
                    }
                }
            });
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
    public void onItemClick(FRRecyclerViewHolder fRRecyclerViewHolder, String data, int position) {
        PRGankWelfareRecyclerAdapter.ViewHolder viewHolder = (PRGankWelfareRecyclerAdapter.ViewHolder) fRRecyclerViewHolder;
        FRViewData viewData = mViewList.get(position);
        int[] location = new int[2];
        // 获取在整个屏幕内的绝对坐标
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.prIvWelfare.getLocationOnScreen(location);
            viewData.setTargetX(location[0])
                    // 此处注意，获取 Y 轴坐标时，需要根据实际情况来处理《状态栏》的高度，判断是否需要计算进去
                    .setTargetY(location[1])
                    .setTargetWidth(viewHolder.mDataBinding.prIvWelfare.getMeasuredWidth())
                    .setTargetHeight(viewHolder.mDataBinding.prIvWelfare.getMeasuredHeight());
            mViewList.set(position, viewData);

//            mFrImageViewer.setImageData(mPRGankWelfareRecyclerAdapter.getAllData());
//            mFrImageViewer.setViewData(mViewList);
//            mFrImageViewer.setStartPosition(position);
//            mFrImageViewer.watch();
            IntentUtils.startActivity(mContext,
                    FRImageViewerActivity.class,
                    new FRBundle()
                            .putParcelableArrayList(FRConstant.IMAGE_VIEWER_VIEW_DATA, mViewList)
                            .putSerializable(FRConstant.IMAGE_VIEWER_IMAGE_DATA, (Serializable) mPRGankWelfareRecyclerAdapter.getAllData())
                            .putInt(FRConstant.IMAGE_VIEWER_START_POSITION, position)
                            .create(),
                    FRActivityAnimator.Animator.SCALE_IN_SCALE_OUT
            );
        }
    }

    @Override
    public boolean onBackPressed() {
        return mFrImageViewer != null && mFrImageViewer.onBackPressed();
    }
}
