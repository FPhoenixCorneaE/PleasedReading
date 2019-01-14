package com.wkz.pleasedreading.main.toutiao;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.gson.Gson;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.OnFRHttpCallback;
import com.wkz.framework.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.CRC32;

@SuppressWarnings("ConstantConditions")
public class PRTouTiaoPresenter extends FRBasePresenter<PRTouTiaoContract.ITouTiaoView, PRTouTiaoContract.ITouTiaoModel>
        implements PRTouTiaoContract.ITouTiaoPresenter {

    private String mTime;
    private HashMap<String, ArrayList<PRTouTiaoVideoBean.DataBean>> mDataMap;
    private String mCategoryId;

    public PRTouTiaoPresenter(@NonNull PRTouTiaoContract.ITouTiaoView view) {
        super(view);
        mDataMap = new HashMap<>();
    }

    @Override
    public void setCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
        this.mDataMap.put(mCategoryId, new ArrayList<>());
    }

    /**
     * 刷新数据
     *
     * @param category
     */
    @Override
    public void onRefreshData(String category) {
        mTime = TimeUtils.getNowString();
        getVideoList(category, mTime);
    }

    /**
     * 加载更多数据
     *
     * @param category
     */
    @Override
    public void onLoadMoreData(String category) {
        getVideoList(category, mTime);
    }

    @Override
    public void getVideoList(String category, String maxBehotTime) {
        mModel.getVideoList(category, maxBehotTime, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoBean data) {
                Gson gson = new Gson();
                PRTouTiaoVideoBean.DataBean.ContentBean contentBean = gson.fromJson(data.getData().get(data.getData().size() - 1).getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
                mTime = contentBean.getBehot_time() + "";
                mView.onSuccess(data.getData());

                int index = 0;
                for (PRTouTiaoVideoBean.DataBean dataBean : data.getData()) {
                    PRTouTiaoVideoBean.DataBean.ContentBean contentBean1 = gson.fromJson(dataBean.getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
                    String url = getVideoContentApi(contentBean1.getVideo_id());
                    if (mDataMap.containsKey(mCategoryId) && mDataMap.get(mCategoryId) != null) {
                        getVideoContent(mDataMap.get(mCategoryId).size() + index, url);
                    }
                    index++;
                }

                if (mDataMap.containsKey(mCategoryId) && mDataMap.get(mCategoryId) != null) {
                    mDataMap.get(mCategoryId).addAll(data.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.onFailure(FRHttpError.ERROR_UNKNOWN, msg);
            }
        });
    }

    @Override
    public void getVideoContent(int position, String url) {
        mModel.getVideoContent(url, mView.bindToLife(), new OnFRHttpCallback<PRTouTiaoVideoContentBean>() {
            @Override
            public void onSuccess(PRTouTiaoVideoContentBean data) {
                if (data == null || data.getData() == null) return;
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
                mView.onGetVideoContentSuccess(position, videoUrl);
            }

            @Override
            public void onFailure(String msg) {
                mView.onGetVideoContentFailure(msg);
            }
        });
    }

    @Override
    public void clear() {
        if (mDataMap.containsKey(mCategoryId) && mDataMap.get(mCategoryId) != null) {
            mDataMap.get(mCategoryId).clear();
        }
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
