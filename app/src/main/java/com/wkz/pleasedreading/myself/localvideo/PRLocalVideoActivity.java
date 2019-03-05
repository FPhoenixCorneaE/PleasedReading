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
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.itemdecoration.FRSpaceDecoration;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivityLocalVideoBinding;

import java.util.List;

public class PRLocalVideoActivity extends FRBaseActivity<PRLocalVideoContract.ILocalVideoPresenter> implements PRLocalVideoContract.ILocalVideoView {

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
}
