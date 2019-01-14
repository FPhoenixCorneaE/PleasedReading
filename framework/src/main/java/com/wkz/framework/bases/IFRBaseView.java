package com.wkz.framework.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface IFRBaseView {
    /**
     * 获取布局id
     *
     * @return
     */
    int getLayoutId();

    /**
     * 构造presenter
     */
    FRBasePresenter createPresenter();

    /**
     * 构造model
     *
     * @return
     */
    IFRBaseModel createModel();

    /**
     * 绑定生命周期,取消订阅
     */
    <T> LifecycleTransformer<T> bindToLife();

    /**
     * 初始化布局
     */
    void initView();

    /**
     * 初始化监听器
     */
    void initListener();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(@Nullable Bundle savedInstanceState);

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载动画
     */
    void hideLoading();

    /**
     * 显示数据内容
     */
    void showContent();

    /**
     * 显示空数据
     */
    void showEmpty();

    /**
     * 显示错误
     */
    void showError();

    /**
     * 获取数据成功
     *
     * @param data 数据
     */
    void onSuccess(@Nullable Object data);

    /**
     * 获取数据失败
     *
     * @param code 失败code
     * @param msg  失败信息
     */
    void onFailure(int code, String msg);
}
