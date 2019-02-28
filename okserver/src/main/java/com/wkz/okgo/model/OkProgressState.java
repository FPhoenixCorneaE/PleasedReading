package com.wkz.okgo.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 状态
 */
@IntDef({
        OkProgressState.NONE,
        OkProgressState.WAITING_WIFI,
        OkProgressState.WAITING,
        OkProgressState.LOADING,
        OkProgressState.PAUSE,
        OkProgressState.ERROR,
        OkProgressState.FINISH
})
@Retention(RetentionPolicy.SOURCE)
public @interface OkProgressState {
    int NONE = 0;         //无状态
    int WAITING_WIFI = 1; //等待Wifi
    int WAITING = 2;      //等待
    int LOADING = 3;      //下载中
    int PAUSE = 4;        //暂停
    int ERROR = 5;        //错误
    int FINISH = 6;       //完成
}
