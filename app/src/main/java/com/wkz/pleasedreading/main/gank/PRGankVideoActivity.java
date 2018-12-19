package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.wkz.framework.base.BaseActivity;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.utils.GlideUtils;
import com.wkz.framework.utils.ScreenUtils;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constant.PRConstant;
import com.wkz.pleasedreading.databinding.PrActivityGankVideoBinding;
import com.wkz.videoplayer.controller.FRVideoPlayerController;
import com.wkz.videoplayer.dialog.FRVideoClarity;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;

import java.util.ArrayList;

public class PRGankVideoActivity extends BaseActivity implements PRGankContract.IGankView {

    private PRGankPresenter mPresenter;
    private PrActivityGankVideoBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_gank_video;
    }

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
        mDataBinding = (PrActivityGankVideoBinding) mViewDataBinding;

        ViewGroup.LayoutParams layoutParams = mDataBinding.prVpVideo.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth();
        layoutParams.height = layoutParams.width * 9 / 16;
        mDataBinding.prVpVideo.setLayoutParams(layoutParams);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            PRGankBean.ResultsBean prGankBean = (PRGankBean.ResultsBean) getIntent().getSerializableExtra(PRConstant.PR_GANK_VIDEO_INFO);
            mPresenter.getVideoInfo(prGankBean);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FRVideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (FRVideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        if (data != null) {
            PRGankBean.ResultsBean prGankBean = (PRGankBean.ResultsBean) data;
            //创建视频播放控制器，只要创建一次就可以呢
            FRVideoPlayerController videoPlayerController = new FRVideoPlayerController(mContext);
            mDataBinding.prVpVideo.setController(videoPlayerController);
            videoPlayerController.setTitle(prGankBean.getDesc());
            videoPlayerController.setClarity(new ArrayList<FRVideoClarity>() {
                private static final long serialVersionUID = -2418882412837550237L;

                {
                    add(new FRVideoClarity("标清", "270P", prGankBean.getPlayAddr()));
                    add(new FRVideoClarity("高清", "480P", prGankBean.getPlayAddr()));
                    add(new FRVideoClarity("超清", "720P", prGankBean.getPlayAddr()));
                    add(new FRVideoClarity("蓝光", "1080P", prGankBean.getPlayAddr()));
                }
            }, 0);
            GlideUtils.setupImagePlaceColorRes(videoPlayerController.imageView(), prGankBean.getCover(), R.color.fr_color_black_translucent50);
            mDataBinding.prVpVideo.setUp(prGankBean.getPlayAddr(), null);

            if (mDataBinding.prVpVideo.isIdle()) {
                mDataBinding.prVpVideo.start();
            }
            mDataBinding.prVpVideo.enterVerticalScreenScreen();
        }
    }
}
