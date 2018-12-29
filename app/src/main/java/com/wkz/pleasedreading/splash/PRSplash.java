package com.wkz.pleasedreading.splash;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface PRSplash {

    /**
     * 获取每日一图
     * http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<PRSplashBean> getDailyImage();
}
