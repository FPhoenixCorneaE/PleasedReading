package com.wkz.videoplayer.window;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.wkz.videoplayer.R;
import com.wkz.videoplayer.constant.FRConstantKeys;
import com.wkz.videoplayer.controller.FRVideoPlayerController;
import com.wkz.videoplayer.inter.listener.OnCompletedListener;
import com.wkz.videoplayer.manager.FRVideoPlayerManager;
import com.wkz.videoplayer.player.FRVideoPlayer;
import com.wkz.videoplayer.utils.FRVideoLogUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/8/29
 *     desc  : 适配了悬浮窗的view
 *     revise:
 * </pre>
 */
public class FRFloatPlayerView extends FrameLayout {

    private FRVideoPlayer mVideoPlayer;

    public FRFloatPlayerView(Context context) {
        super(context);
        init();
    }

    public FRFloatPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FRFloatPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (inflater != null) {
            view = inflater.inflate(R.layout.fr_view_window_dialog, this);
            mVideoPlayer = view.findViewById(R.id.video_player);
            mVideoPlayer.setUp(path, null);
            mVideoPlayer.setPlayerType(FRConstantKeys.IjkPlayerType.TYPE_NATIVE);
            //创建视频控制器
            FRVideoPlayerController controller = new FRVideoPlayerController(getContext());
            controller.setTopVisibility(false);
            controller.setLoadingType(FRConstantKeys.Loading.LOADING_QQ);
            controller.imageView().setBackgroundColor(Color.BLACK);
            controller.setCenterPlayer(true, R.mipmap.fr_ic_player_center);
            controller.setOnCompletedListener(new OnCompletedListener() {
                @Override
                public void onCompleted() {
                    FRVideoPlayerManager.instance().releaseVideoPlayer();
                    if (mCompletedListener != null) {
                        mCompletedListener.onCompleted();
                    }
                }
            });
            mVideoPlayer.setController(controller);
            mVideoPlayer.start();
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FRVideoLogUtils.d("点击事件" + mVideoPlayer.getCurrentState());
                    if (mVideoPlayer.isPlaying()) {
                        mVideoPlayer.pause();
                    } else if (mVideoPlayer.isPaused()) {
                        mVideoPlayer.restart();
                    }
                    FRVideoLogUtils.d("点击事件" + mVideoPlayer.getCurrentState());
                }
            });
            view.setOnTouchListener(new FRSmallWindowTouch(view, 0, 0));
        }
    }

    private static String path;

    public static void setUrl(String url) {
        path = url;
    }

    /**
     * 监听视频播放完成事件
     */
    private static OnCompletedListener mCompletedListener;

    public static void setOnCompletedListener(OnCompletedListener listener) {
        mCompletedListener = listener;
    }

}
