package com.wkz.pleasedreading.main.gank;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.model.FRBundle;
import com.wkz.framework.utils.ScreenUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.SpaceDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constant.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentGankChildBinding;
import com.wkz.pleasedreading.main.gank.PRGankContract.IGankView;
import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.listener.OnPreviewStatusListener;
import com.wkz.viewer.widget.FRScaleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 福利
 */
public class PRGankWelfareFragment extends BaseFragment implements IGankView, FRBaseRecyclerAdapter.OnLoadMoreListener, OnRefreshListener, FRBaseRecyclerAdapter.OnItemClickListener<String> {

    private PRGankPresenter mPresenter;
    private PrFragmentGankChildBinding mDataBinding;
    private PRGankWelfareRecyclerAdapter mPRGankWelfareRecyclerAdapter;
    private String mTitle;
    private List<FRViewData> mViewList = new ArrayList<>();

    public static PRGankWelfareFragment create(String title) {
        PRGankWelfareFragment gankWelfareFragment = new PRGankWelfareFragment();
        gankWelfareFragment.setArguments(new FRBundle().putString(PRConstant.PR_FRAGMENT_TITLE, title).create());
        return gankWelfareFragment;
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
        SpaceDecoration spaceDecoration = new SpaceDecoration(SizeUtils.dp2px(10f));
        mDataBinding.prRvGankChild.addItemDecoration(spaceDecoration);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);//定义瀑布流管理器，第一个参数是列数，第二个是方向。
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
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
                staggeredGridLayoutManager.invalidateSpanAssignments();//这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
            }
        });
        staggeredGridLayoutManager.scrollToPositionWithOffset(0, 0);
        mDataBinding.prIvViewer.setOnPreviewStatusListener(new OnPreviewStatusListener() {
            @Override
            public void onPreviewStatus(int state, FRScaleImageView imagePager) {
                if (state == FRImageViewerState.STATE_READY_CLOSE) {
                    int top = getTop(mDataBinding.prIvViewer.getCurrentPosition());
                    FRViewData viewData = mViewList.get(mDataBinding.prIvViewer.getCurrentPosition());
                    viewData.setTargetY(top);
                    mViewList.set(mDataBinding.prIvViewer.getCurrentPosition(), viewData);
                    mDataBinding.prIvViewer.setViewData(mViewList);
                    staggeredGridLayoutManager.scrollToPositionWithOffset(mDataBinding.prIvViewer.getCurrentPosition(), top);
                }
            }
        });
    }

    private int getTop(int position) {
        int top = 0;
        // 当前图片的高度
        float imgH = mViewList.get(position).getTargetHeight();
        // 图片距离 imageViewer 的上下边距
        int dis = (int) ((mDataBinding.prIvViewer.getHeight() - imgH) / 2);
        // 如果图片高度大于等于 imageViewer 的高度
        if (dis <= 0) {
            return top + dis;
        } else {
            float th1 = 0;
            float th2 = 0;
            // 计算当前图片上方所有 Item 的总高度
            for (int i = 0; i < position; i++) {
                // SizeUtils.dp2px(240f + 20 * (position % 4)) 是 Item 的高度
                th1 += SizeUtils.dp2px(240f + 20 * (position % 4));
            }
            // 计算当前图片下方所有 Item 的总高度
            for (int i = position + 1; i < mViewList.size(); i++) {
                // SizeUtils.dp2px(240f + 20 * (position % 4)) 是 Item 的高度
                th2 += SizeUtils.dp2px(240f + 20 * (position % 4));
            }
            if (th1 >= dis && th2 >= dis) {
                return top + dis;
            } else if (th1 < dis) {
                return (int) (top + th1);
            } else if (th2 < dis) {
                return (int) (mDataBinding.prRvGankChild.getHeight() - imgH);
            }
        }
        return 0;
    }

    @Override
    public void initListener() {
        mDataBinding.prIvViewer.setImageLoader(new IImageLoader<String>() {
            @Override
            public void displayImage(int position, String srcUrl, ImageView imageView) {
                final FRScaleImageView scaleImageView = (FRScaleImageView) imageView.getParent();
                Glide.with(imageView.getContext())
                        .load(srcUrl)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(new ColorDrawable(Color.BLACK))
                        )
                        .into(new ImageViewTarget<Drawable>(imageView) {

                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                scaleImageView.showProgess();
                                imageView.setImageDrawable(placeholder);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                scaleImageView.removeProgressView();
                                imageView.setImageDrawable(errorDrawable);
                            }

                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                scaleImageView.removeProgressView();
                                imageView.setImageDrawable(resource);
                            }
                        });

            }
        });
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
                        for (PRGankBean.ResultsBean bean : (List<PRGankBean.ResultsBean>) data) {
                            add(bean.getUrl());
                            mViewList.add(new FRViewData().setTargetWidth(ScreenUtils.getScreenWidth()));
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
                            mViewList.add(new FRViewData().setTargetWidth(ScreenUtils.getScreenWidth()));
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
            mViewList.add(viewData);

            mDataBinding.prIvViewer.setImageData(mPRGankWelfareRecyclerAdapter.getAllData());
            mDataBinding.prIvViewer.setViewData(mViewList);
            mDataBinding.prIvViewer.setStartPosition(position);
            mDataBinding.prIvViewer.watch();
        }
    }
}
