package com.wkz.pleasedreading.myself.localvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.constants.FRFilesDirectory;
import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.FRSpaceDecoration;
import com.wkz.framework.widgets.recycleradapter.FRBaseRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivityLocalVideoBinding;
import com.wkz.videoplayer.controller.FRVideoPlayerController;
import com.wkz.videoplayer.inter.listener.OnVideoBackListener;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;

import java.util.List;

public class PRLocalVideoActivity extends FRBaseActivity<PRLocalVideoContract.ILocalVideoPresenter> implements PRLocalVideoContract.ILocalVideoView, FRBaseRecyclerAdapter.OnItemClickListener<FRVideoBean>, FRBaseRecyclerAdapter.OnItemChildClickListener<FRVideoBean> {

    private PrActivityLocalVideoBinding mDataBinding;
    private PRLocalVideoRecyclerAdapter mLocalVideoRecyclerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_local_video;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRLocalVideoPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return new PRLocalVideoModel();
    }

    @Override
    public void initView() {
        mDataBinding = (PrActivityLocalVideoBinding) mViewDataBinding;

        mDataBinding.setTitle("本地视频");
        initRecyclerView();
    }

    private void initRecyclerView() {
        FRSpaceDecoration spaceDecoration =
                new FRSpaceDecoration(SizeUtils.dp2px(16f))
                        .setPaddingEdgeSide(true)
                        .setPaddingStart(true);
        mDataBinding.prRvLocalVideo.addItemDecoration(spaceDecoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return 2;
            }
        });
        mDataBinding.prRvLocalVideo.setLayoutManager(gridLayoutManager);
        mDataBinding.prRvLocalVideo.setHasFixedSize(true);
        mDataBinding.prRvLocalVideo.setAdapter(mLocalVideoRecyclerAdapter =
                new PRLocalVideoRecyclerAdapter(mContext, null, true));
    }

    @Override
    public void initListener() {
        mDataBinding.prToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLocalVideoRecyclerAdapter.setOnItemClickListener(this);
        mLocalVideoRecyclerAdapter.setOnItemChildClickListener(R.id.pr_iv_video_thumbnail, this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        mPresenter.getVideos(mContext, FRFilesDirectory.DIR_DOWNLOAD_VIDEOS.getPath());
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        mLocalVideoRecyclerAdapter.setNewData((List<FRVideoBean>) data);
    }

    @Override
    public void onItemChildClick(FRRecyclerViewHolder fRRecyclerViewHolder, FRVideoBean data, int position) {
        onItemClick(fRRecyclerViewHolder, data, position);
    }

    @Override
    public void onItemClick(FRRecyclerViewHolder fRRecyclerViewHolder, FRVideoBean data, int position) {
        mDataBinding.prFlVideo.setVisibility(View.VISIBLE);

        //创建视频控制器，只要创建一次就可以了呢
        FRVideoPlayerController controller = new FRVideoPlayerController(mContext);
        //设置视频标题
        controller.setTitle(data.getName());
        controller.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
                onBackPressed();
            }
        });
        //设置视频控制器
        mDataBinding.prVpVideo.setController(controller);

        //设置本地视频地址
        mDataBinding.prVpVideo.setUp(data.getPath(), null);
        //是否从上一次的位置继续播放
        mDataBinding.prVpVideo.continueFromLastPosition(false);

        //开始播放
        if (mDataBinding.prVpVideo.isIdle()) {
            mDataBinding.prVpVideo.start();
        }
    }

    @Override
    public void onBackPressed() {
        if (FRVideoPlayerManager.instance().onBackPressed()) {
            return;
        }
        if (mDataBinding.prFlVideo.getVisibility() == View.VISIBLE) {
            mDataBinding.prFlVideo.setVisibility(View.GONE);
            FRVideoPlayerManager.instance().releaseVideoPlayer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FRVideoPlayerManager.instance().releaseVideoPlayer();
    }
}
