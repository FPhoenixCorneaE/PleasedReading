package com.wkz.framework.functions.web;

import android.webkit.JavascriptInterface;

import com.wkz.framework.base.FRBaseActivity;

public class FRCommonJSInterface {

    public static final String NAME = "FRFramework";

    private FRBaseActivity mContext;

    public FRCommonJSInterface(FRBaseActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    @JavascriptInterface
    public String toString() {
        return NAME;
    }
}
