package com.wkz.framework.functions.web;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.orhanobut.logger.Logger;
import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.utils.ToastUtils;

import java.util.ArrayList;

public class FRCommonJSInterface {

    public static final String NAME = "FRFramework";
    private ArrayList<String> mImgList;

    private FRBaseActivity mContext;

    public FRCommonJSInterface(FRBaseActivity mContext) {
        this.mContext = mContext;
        this.mImgList = new ArrayList<>();
    }

    /**
     * 把所有图片的url保存在SparseArray<String>中
     *
     * @param imgUrl
     */
    @JavascriptInterface
    public void setImageUrl(String imgUrl) {
        Logger.t("webimage").i(imgUrl);
        mImgList.add(imgUrl);
    }

    /**
     * 点击图片所调用到的函数
     *
     * @param imgUrl
     */
    @JavascriptInterface
    public void clickImage(String imgUrl) {
        Logger.t("webimage").i(mImgList.toString());
        for (int i = 0; i < mImgList.size(); i++) {
            if (!TextUtils.isEmpty(imgUrl) && imgUrl.equals(mImgList.get(i))) {
                ToastUtils.showShort("点击了第" + i + "张图片");
                break;
            }
        }
    }

    @JavascriptInterface
    public void clearImage() {
        mImgList.clear();
    }

    @Override
    @JavascriptInterface
    public String toString() {
        return NAME;
    }
}
