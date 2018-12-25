package com.wkz.pleasedreading.main.toutiao;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PRTouTiao {

    /**
     * 获取视频列表
     * http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13&category=subv_voice&max_behot_time=2018-12-25%2009:26:30
     */
    @GET("http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
    Observable<PRTouTiaoVideoBean> getVideoList(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime
    );
}
