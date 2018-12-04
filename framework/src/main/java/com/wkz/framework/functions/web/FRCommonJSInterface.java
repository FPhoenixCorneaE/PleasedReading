package com.wkz.framework.functions.web;

import android.webkit.JavascriptInterface;

import com.wkz.framework.base.BaseActivity;

public class FRCommonJSInterface {

    public static final String NAME = "FRFramework";

    private BaseActivity mContext;

    public FRCommonJSInterface(BaseActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    @JavascriptInterface
    public String toString() {
        return NAME;
    }
}
