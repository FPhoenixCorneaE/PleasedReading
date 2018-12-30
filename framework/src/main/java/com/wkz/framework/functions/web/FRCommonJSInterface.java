package com.wkz.framework.functions.web;

import android.util.SparseArray;
import android.webkit.JavascriptInterface;

import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.utils.ToastUtils;

public class FRCommonJSInterface {

    public static final String NAME = "FRFramework";
    private SparseArray<String> mImgList;

    private FRBaseActivity mContext;

    public FRCommonJSInterface(FRBaseActivity mContext) {
        this.mContext = mContext;
        this.mImgList = new SparseArray<>();
    }

    /**
     * 把所有图片的url保存在SparseArray<String>中
     *
     * @param imgUrl
     */
    @JavascriptInterface
    public void setImageUrl(String imgUrl) {
        mImgList.put(mImgList.size(), imgUrl);
    }

    /**
     * 点击图片所调用到的函数
     *
     * @param imgUrl
     */
    @JavascriptInterface
    public void clickImage(String imgUrl) {
        ToastUtils.showShort("点击了第" + mImgList.indexOfValue(imgUrl) + "张图片");
    }

    @Override
    @JavascriptInterface
    public String toString() {
        return NAME;
    }
}
