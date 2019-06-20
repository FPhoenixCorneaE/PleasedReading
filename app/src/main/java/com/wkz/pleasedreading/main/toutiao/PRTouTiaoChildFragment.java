package com.wkz.pleasedreading.main.toutiao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wkz.utils.ResourceUtils;
import com.wkz.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.FRDividerDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constants.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentToutiaoChildBinding;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;

import java.util.List;

public class PRTouTiaoChildFragment extends PRTouTiaoFragment implements PRTouTiaoContract.ITouTiaoView, OnRefreshListener, FRBaseRecyclerAdapter.OnLoadMoreListener {

    private PrFragmentToutiaoChildBinding mDataBinding;
    private PRTouTiaoChildRecyclerAdapter mPRTouTiaoChildRecyclerAdapter;
    private String mCatagoryId;

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_toutiao_child;
    }

    @Override
    public void initView() {
        mDataBinding = (PrFragmentToutiaoChildBinding) mViewDataBinding;
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
        FRDividerDecoration dividerDecoration = new FRDividerDecoration(
                ResourceUtils.getColor(R.color.fr_color_transparent),
                SizeUtils.dp2px(0f)
        );
        mDataBinding.prRvGankChild.addItemDecoration(dividerDecoration);
        mDataBinding.prRvGankChild.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBinding.prRvGankChild.setAdapter(
                mPRTouTiaoChildRecyclerAdapter = ((PRTouTiaoChildRecyclerAdapter)
                        new PRTouTiaoChildRecyclerAdapter(mContext, null, true)
                                .setIgnorePageNum(true)
                                .setOnLoadMoreListener(this)
                )
        );
        mDataBinding.prRvGankChild.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder viewHolder) {
                //释放视频资源
                if (viewHolder instanceof PRTouTiaoChildRecyclerAdapter.ViewHolder) {
                    PRTouTiaoChildRecyclerAdapter.ViewHolder holder = (PRTouTiaoChildRecyclerAdapter.ViewHolder) viewHolder;
                    if (holder.mDataBinding.prVpVideo == FRVideoPlayerManager.instance().getCurrentVideoPlayer()) {
                        FRVideoPlayerManager.instance().releaseVideoPlayer();
                    }
                }
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCatagoryId = getArguments().getString(PRConstant.PR_TOUTIAO_VIDEO_CATAGORY_ID);
        } else {
            mCatagoryId = "";
        }
        mPresenter.setCategoryId(mCatagoryId);
    }

    @Override
    public void onEmptyChildClick(View view) {
        showContent();
        mPresenter.onRefreshData(mCatagoryId);
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        if (RefreshState.Refreshing == mDataBinding.prSrlRefresh.getState()) {
            mDataBinding.prSrlRefresh.finishRefresh();
            //清空数据
            mPresenter.clear();
            mPRTouTiaoChildRecyclerAdapter.setNewData((List<PRTouTiaoVideoBean.DataBean>) data);
        } else {
            mPRTouTiaoChildRecyclerAdapter.setLoadMoreData((List<PRTouTiaoVideoBean.DataBean>) data);
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.onRefreshData(mCatagoryId);
    }

    @Override
    public void onLoadMore(boolean isReload) {
        mPresenter.onLoadMoreData(mCatagoryId);
    }

    @Override
    public void onGetVideoContentSuccess(int position, String videoUrl) {
        mPRTouTiaoChildRecyclerAdapter.getData(position).setVideoUrl(videoUrl);
        mPRTouTiaoChildRecyclerAdapter.notifyItemChanged(position);
    }

    @Override
    public void onGetVideoContentFailure(String errorMsg) {

    }
}
