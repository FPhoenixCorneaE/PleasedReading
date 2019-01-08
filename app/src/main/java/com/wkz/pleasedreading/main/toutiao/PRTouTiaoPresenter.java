package com.wkz.pleasedreading.main.toutiao;

import android.util.Base64;

import com.google.gson.Gson;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;
import com.wkz.framework.utils.TimeUtils;

import java.util.Random;
import java.util.zip.CRC32;

public class PRTouTiaoPresenter extends FRBasePresenter<PRTouTiaoContract.ITouTiaoView, PRTouTiaoContract.ITouTiaoModel>
        implements PRTouTiaoContract.ITouTiaoPresenter {

    private String mTime;

    public PRTouTiaoPresenter(PRTouTiaoContract.ITouTiaoView view) {
        super(view);
    }

    /**
     * 刷新数据
     *
     * @param category
     */
    public void onRefreshData(String category) {
        mTime = TimeUtils.getNowString();
        getVideoList(category, mTime);
    }

    /**
     * 加载更多数据
     *
     * @param category
     */
    public void onLoadMoreData(String category) {
        getVideoList(category, mTime);
    }

    @Override
    public void getVideoList(String category, String maxBehotTime) {
        mModel.getVideoList(category, maxBehotTime, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoBean data) {
                if (mView != null) {
                    Gson gson = new Gson();
                    PRTouTiaoVideoBean.DataBean.ContentBean contentBean = gson.fromJson(data.getData().get(data.getData().size() - 1).getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
                    mTime = contentBean.getBehot_time() + "";
                    mView.onSuccess(data.getData());

                    for (PRTouTiaoVideoBean.DataBean dataBean : data.getData()) {
                        PRTouTiaoVideoBean.DataBean.ContentBean contentBean1 = gson.fromJson(dataBean.getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
                        String url = getVideoContentApi(contentBean1.getVideo_id());
                        getVideoContent(dataBean, url);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (mView != null) {
                    mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
                }
            }
        });
    }

    @Override
    public void getVideoContent(PRTouTiaoVideoBean.DataBean dataBean, String url) {
        mModel.getVideoContent(url, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoContentBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoContentBean data) {
                if (dataBean != null) {
                    PRTouTiaoVideoContentBean.DataBean.VideoListBean videoListBean = data.getData().getVideo_list();
                    String videoUrl = "";
                    if (videoListBean.getVideo_3() != null) {
                        String base64 = videoListBean.getVideo_3().getMain_url();
                        videoUrl = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    }

                    if (videoListBean.getVideo_2() != null) {
                        String base64 = videoListBean.getVideo_2().getMain_url();
                        videoUrl = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    }

                    if (videoListBean.getVideo_1() != null) {
                        String base64 = videoListBean.getVideo_1().getMain_url();
                        videoUrl = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    }
                    dataBean.setVideoUrl(videoUrl);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private static String getVideoContentApi(String videoId) {
        String VIDEO_HOST = "http://ib.365yg.com";
        String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
        String r = getRandom();
        String s = String.format(VIDEO_URL, videoId, r);
        // 将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()} 进行crc32加密
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        String crcString = crc32.getValue() + "";
        return VIDEO_HOST + s + "&s=" + crcString;
    }

    private static String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
