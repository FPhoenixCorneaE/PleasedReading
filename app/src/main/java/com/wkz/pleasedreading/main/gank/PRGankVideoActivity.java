package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.utils.GlideUtils;
import com.wkz.framework.utils.ScreenUtils;
import com.wkz.framework.utils.ToastUtils;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constant.PRConstant;
import com.wkz.pleasedreading.databinding.PrActivityGankVideoBinding;
import com.wkz.videoplayer.constant.FRConstantKeys;
import com.wkz.videoplayer.controller.FRVideoPlayerController;
import com.wkz.videoplayer.dialog.FRVideoClarity;
import com.wkz.videoplayer.inter.listener.OnPlayOrPauseListener;
import com.wkz.videoplayer.inter.listener.OnVideoBackListener;
import com.wkz.videoplayer.inter.listener.OnVideoControlListener;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;

import java.util.ArrayList;

public class PRGankVideoActivity extends FRBaseActivity<PRGankPresenter> implements PRGankContract.IGankView {

    private PrActivityGankVideoBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_gank_video;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRGankPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRGankModel.class);
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
            //初始化视频播放器
            initVideoPlayer(prGankBean);
        }
    }

    /**
     * 初始化视频播放器
     *
     * @param prGankBean
     */
    private void initVideoPlayer(PRGankBean.ResultsBean prGankBean) {
        //设置播放类型
        mDataBinding.prVpVideo.setPlayerType(FRConstantKeys.IjkPlayerType.TYPE_IJK);
        //网络视频地址
        String videoUrl = prGankBean.getPlayAddr();
        //设置视频地址和请求头部
        mDataBinding.prVpVideo.setUp(videoUrl, null);
        //是否从上一次的位置继续播放
        mDataBinding.prVpVideo.continueFromLastPosition(false);
        //设置播放速度
        mDataBinding.prVpVideo.setSpeed(1.0f);

        //创建视频控制器，只要创建一次就可以了呢
        FRVideoPlayerController controller = new FRVideoPlayerController(mContext);
        //设置视频标题
        controller.setTitle(prGankBean.getDesc());
        //设置加载动画
        controller.setLoadingType(FRConstantKeys.Loading.LOADING_QQ);
        //显示中心播放按钮
        controller.setCenterPlayer(true, 0);
        controller.setTopVisibility(true);
        //视频封面
        GlideUtils.setupImagePlaceColorRes(
                controller.imageView(),
                prGankBean.getCover(),
                R.color.fr_color_black_translucent50
        );
        //设置视频清晰度
        controller.setClarity(new ArrayList<FRVideoClarity>() {
            private static final long serialVersionUID = -2418882412837550237L;

            {
                add(new FRVideoClarity("标清", "270P", prGankBean.getPlayAddr()));
                add(new FRVideoClarity("高清", "480P", prGankBean.getPlayAddr()));
                add(new FRVideoClarity("超清", "720P", prGankBean.getPlayAddr()));
                add(new FRVideoClarity("蓝光", "1080P", prGankBean.getPlayAddr()));
            }
        }, 0);
        controller.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
                onBackPressed();
            }
        });
        controller.setOnPlayOrPauseListener(new OnPlayOrPauseListener() {
            @Override
            public void onPlayOrPauseClick(boolean isPlaying) {

            }
        });
        controller.setOnVideoControlListener(new OnVideoControlListener() {
            @Override
            public void onVideoControlClick(int type) {
                switch (type) {
                    case FRConstantKeys.VideoControl.DOWNLOAD:
                        ToastUtils.showShort("下载音视频");
                        break;
                    case FRConstantKeys.VideoControl.AUDIO:
                        ToastUtils.showShort("切换音频");
                        break;
                    case FRConstantKeys.VideoControl.SHARE:
                        ToastUtils.showShort("分享内容");
                        break;
                    default:
                        break;
                }
            }
        });
        //设置视频控制器
        mDataBinding.prVpVideo.setController(controller);

        //进入竖屏播放
        if (mDataBinding.prVpVideo.isIdle()) {
            mDataBinding.prVpVideo.start();
        }
        mDataBinding.prVpVideo.enterVerticalScreenScreen();
    }
}
