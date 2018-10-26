package com.wkz.pleasedreading.main.gank;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PRGank {

    /**
     * 根据数据类型获取干货数据
     *
     * @param type      数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param pageNum   请求个数： 数字，大于0
     * @param pageCount 第几页：数字，大于0
     * @return
     */
    @GET("/api/data/{type}/{number}/{page}")
    Observable<PRGankBean> getDataByType(@Path("type") String type, @Path("number") int pageNum, @Path("page") int pageCount);
}
