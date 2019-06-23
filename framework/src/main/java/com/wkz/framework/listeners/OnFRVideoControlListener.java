package com.wkz.framework.listeners;

import androidx.annotation.DrawableRes;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wkz.framework.constants.FRFilesDirectory;
import com.wkz.framework.managers.FRNotificationManager;
import com.wkz.utils.NetworkUtils;
import com.wkz.utils.ToastUtils;
import com.wkz.okgo.OkGo;
import com.wkz.okgo.model.OkProgress;
import com.wkz.okgo.model.OkProgressState;
import com.wkz.okgo.request.OkGetRequest;
import com.wkz.okserver.OkDownload;
import com.wkz.okserver.download.OkDownloadTask;
import com.wkz.okserver.download.OkDownloadType;
import com.wkz.okserver.download.OnOkDownloadListener;
import com.wkz.videoplayer.constant.FRConstantKeys;
import com.wkz.videoplayer.inter.listener.OnVideoControlListener;

import java.io.File;
import java.util.Random;

public class OnFRVideoControlListener implements OnVideoControlListener {
    private String mVideoUrl;
    private String mVideoTitle;
    @DrawableRes
    private int icon;

    public OnFRVideoControlListener(String mVideoUrl, String mVideoTitle, int icon) {
        this.mVideoUrl = mVideoUrl;
        this.mVideoTitle = mVideoTitle;
        this.icon = icon;
    }

    @Override
    public void onVideoControlClick(int type) {
        switch (type) {
            case FRConstantKeys.VideoControl.DOWNLOAD:
                if (TextUtils.isEmpty(mVideoUrl)) {
                    Logger.e("视频地址为空！");
                    ToastUtils.showShort("视频地址为空！");
                    return;
                }
                if (TextUtils.isEmpty(mVideoTitle)) {
                    Logger.e("视频标题为空！");
                    ToastUtils.showShort("视频标题为空！");
                    return;
                }

                if (OkDownload.getInstance().getTask(mVideoUrl) != null) {
                    OkDownloadTask downloadTask = OkDownload.getInstance().getTask(mVideoUrl);
                    if (downloadTask.progress.status == OkProgressState.FINISH) {
                        File videoPath = new File(FRFilesDirectory.DIR_DOWNLOAD_VIDEOS, downloadTask.progress.fileName);
                        if (videoPath.exists()) {
                            ToastUtils.showShort("该视频已下载完成");
                        } else {
                            //重新下载
                            downloadTask.restart();
                        }
                    } else {
                        ToastUtils.showShort("已添加到下载队列");
                    }
                } else {
                    if (NetworkUtils.isWifiConnected()) {
                        //开始下载
                        startDownload();
                    } else if (NetworkUtils.isMobileConnected()) {
                        //TODO 手机网络连接，弹窗提示用户是否用流量下载
                    }
                }
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

    /**
     * 开始下载
     */
    private void startDownload() {
        OkGetRequest<File> request = OkGo.get(mVideoUrl);

        OkDownload.request(mVideoUrl, request)
                .folder(FRFilesDirectory.DIR_DOWNLOAD_VIDEOS.getAbsolutePath())
                .fileName(mVideoTitle + ".mp4")
                .save()
                .register(new OnOkDownloadListener(mVideoUrl) {
                    private int mDotCount;
                    private int mManageId = new Random().nextInt(Integer.MAX_VALUE);

                    @Override
                    public void onStart(OkProgress progress) {
                        ToastUtils.showShort("开始下载视频");
                        FRNotificationManager.getInstance().showNotification(mVideoTitle, "下载开始！", mManageId, OkDownloadType.Video, (int) (progress.fraction * 100), 100, icon);
                    }

                    @Override
                    public void onProgress(OkProgress progress) {
                        FRNotificationManager.getInstance().showNotification(mVideoTitle, "下载中" + "......".substring(0, mDotCount++ % 6 + 1), mManageId, OkDownloadType.Video, (int) (progress.fraction * 100), 100, icon);
                    }

                    @Override
                    public void onError(OkProgress progress) {
                        FRNotificationManager.getInstance().showNotification(mVideoTitle, "下载出错了！", mManageId, OkDownloadType.Video, (int) (progress.fraction * 100), 100, icon);
                    }

                    @Override
                    public void onFinish(File file, OkProgress progress) {
                        ToastUtils.showShort("视频下载完成");
                        FRNotificationManager.getInstance().showNotification(mVideoTitle, "下载完成！", mManageId, OkDownloadType.Video, (int) (progress.fraction * 100), 100, icon);
                    }

                    @Override
                    public void onRemove(OkProgress progress) {

                    }
                })
                .start();
    }
}
