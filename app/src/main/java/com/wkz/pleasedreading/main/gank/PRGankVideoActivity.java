package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.listeners.OnFRVideoControlListener;
import com.wkz.framework.utils.GlideUtils;
import com.wkz.utils.NetworkUtils;
import com.wkz.utils.ScreenUtils;
import com.wkz.okserver.OkDownload;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constants.PRConstant;
import com.wkz.pleasedreading.databinding.PrActivityGankVideoBinding;
import com.wkz.videoplayer.constant.FRConstantKeys;
import com.wkz.videoplayer.controller.FRVideoPlayerController;
import com.wkz.videoplayer.dialog.FRVideoClarity;
import com.wkz.videoplayer.inter.listener.OnCompletedListener;
import com.wkz.videoplayer.inter.listener.OnPlayOrPauseListener;
import com.wkz.videoplayer.inter.listener.OnVideoBackListener;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;
import com.wkz.videoplayer.window.FRFloatPlayerView;
import com.wkz.videoplayer.window.FRFloatWindow;
import com.wkz.videoplayer.window.FRMoveType;
import com.wkz.videoplayer.window.FRWindowScreen;

import java.util.ArrayList;

public class PRGankVideoActivity extends FRBaseActivity<PRGankContract.IGankPresenter> implements PRGankContract.IGankView {

    private PrActivityGankVideoBinding mDataBinding;
    private String mVideoUrl;
    private String mVideoTitle;

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

        //设置宽高比例为16:9
        ViewGroup.LayoutParams layoutParams = mDataBinding.prVpVideo.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth();
        layoutParams.height = (int) (layoutParams.width * 9f / 16f);
        mDataBinding.prVpVideo.setLayoutParams(layoutParams);

        if (FRFloatWindow.get() != null) {
            FRFloatWindow.destroy();
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //从数据库中恢复下载数据
        OkDownload.restore();

        if (getIntent() != null) {
            PRGankBean.ResultsBean prGankBean = (PRGankBean.ResultsBean) getIntent().getSerializableExtra(PRConstant.PR_GANK_VIDEO_INFO);
            mPresenter.getVideoInfo(prGankBean);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //为了小窗口视频播放完毕能隐藏掉小窗口,这里不释放播放器资源
//        FRVideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (FRVideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void finish() {
        startWindow();
        super.finish();
    }

    /**
     * 小窗口视频播放
     */
    private void startWindow() {
        if (FRFloatWindow.get() != null || mDataBinding.prVpVideo.isCompleted()) {
            return;
        }
        FRFloatPlayerView.setUrl(mVideoUrl);
        FRFloatPlayerView.setOnCompletedListener(new OnCompletedListener() {
            @Override
            public void onCompleted() {
                FRFloatWindow.destroy();
            }
        });
        FRFloatPlayerView floatPlayerView = new FRFloatPlayerView(getApplicationContext());
        FRFloatWindow
                .with(getApplicationContext())
                .setView(floatPlayerView)
                //这个是设置位置
                .setX(FRWindowScreen.width, 0.8f)
                .setY(FRWindowScreen.height, 0.5f)
                .setMoveType(FRMoveType.slide)
                //用于指定在哪些界面显示悬浮窗，默认全部界面都显示
                .setFilter(false)
                .setMoveStyle(500, new BounceInterpolator())
                .build();
        FRFloatWindow.get().show();
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
        //网络视频地址
        mVideoUrl = prGankBean.getPlayAddr();
        //网络视频标题
        mVideoTitle = prGankBean.getDesc();
        //设置播放类型
        mDataBinding.prVpVideo.setPlayerType(FRConstantKeys.IjkPlayerType.TYPE_IJK);
        //设置视频地址和请求头部
        mDataBinding.prVpVideo.setUp(mVideoUrl, null);
        //是否从上一次的位置继续播放
        mDataBinding.prVpVideo.continueFromLastPosition(false);
        //设置播放速度
        mDataBinding.prVpVideo.setSpeed(1.0f);

        //创建视频控制器，只要创建一次就可以了呢
        FRVideoPlayerController controller = new FRVideoPlayerController(mContext);
        //设置视频标题
        controller.setTitle(mVideoTitle);
        //设置加载动画
        controller.setLoadingType(FRConstantKeys.Loading.LOADING_QQ);
        //显示中心播放按钮
        controller.setCenterPlayer(true, 0);
        //显示下载、分享等功能
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
                add(new FRVideoClarity("标清", "270P", mVideoUrl));
                add(new FRVideoClarity("高清", "480P", mVideoUrl));
                add(new FRVideoClarity("超清", "720P", mVideoUrl));
                add(new FRVideoClarity("蓝光", "1080P", mVideoUrl));
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
        //设置视屏控制监听
        controller.setOnVideoControlListener(new OnFRVideoControlListener(mVideoUrl, mVideoTitle, R.mipmap.pr_ic_launcher));
        //设置视频控制器
        mDataBinding.prVpVideo.setController(controller);

        if (NetworkUtils.isWifiConnected()) {
            //开始播放
            if (mDataBinding.prVpVideo.isIdle()) {
                mDataBinding.prVpVideo.start();
            }
            //进入竖屏播放
            mDataBinding.prVpVideo.enterVerticalScreenScreen();
        } else if (NetworkUtils.isMobileConnected()) {
            //TODO 手机网络连接，弹窗提示用户是否用流量播放
        }
    }
}
