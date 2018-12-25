package com.wkz.framework.functions.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wkz.framework.R;
import com.wkz.framework.base.FRBaseActivity;
import com.wkz.framework.base.IFRBaseModel;
import com.wkz.framework.base.FRBasePresenter;
import com.wkz.framework.constant.FRConstant;
import com.wkz.framework.databinding.FrActivityWebPageBinding;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.widgets.FRProgressBar;

public class FRWebPageActivity extends FRBaseActivity implements FRWebView.Listener {

    private FrActivityWebPageBinding mDataBinding;
    private String mUrl;

    @Override
    public int getLayoutId() {
        return R.layout.fr_activity_web_page;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return null;
    }

    @Override
    public IFRBaseModel createModel() {
        return null;
    }

    @Override
    public void initView() {
        mDataBinding = (FrActivityWebPageBinding) mViewDataBinding;
        initProgressBar();
    }

    @Override
    public void initListener() {
        mDataBinding.frWvWebPage.setListener(mContext, this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra(FRConstant.WEB_URL);
            if (!mUrl.startsWith("https://") && !mUrl.startsWith("http://")) {
                mUrl = String.format("http://%s", mUrl);
            }
        }
        mDataBinding.frWvWebPage.addJavascriptInterface(new FRCommonJSInterface(mContext), FRCommonJSInterface.NAME);
        mDataBinding.frWvWebPage.loadUrl(mUrl);
    }

    /**
     * 初始化进度条
     */
    private void initProgressBar() {
        mDataBinding.frPbProgress.setOrientation(FRProgressBar.STYLE_HORIZONTAL)
                .setProgressBarBgColor(ResourceUtils.getColor(R.color.fr_background_Oxffededed))
                .setProgressColor(ResourceUtils.getColor(R.color.fr_color_light_blue))
                .setMax(100);
    }

    /**
     * 显示加载进度条
     */
    private void showLoadingProgressBar() {
        if (mDataBinding.frPbProgress != null && View.GONE == mDataBinding.frPbProgress.getVisibility()) {
            mDataBinding.frPbProgress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新加载进度条
     */
    private void updateLoadingProgressBar(int progress) {
        if (mDataBinding.frPbProgress != null) {
            mDataBinding.frPbProgress.setProgress(progress);
        }
    }

    /**
     * 隐藏加载进度条
     */
    private void hideLoadingProgressBar() {
        if (mDataBinding.frPbProgress != null && View.VISIBLE == mDataBinding.frPbProgress.getVisibility()) {
            mDataBinding.frPbProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLoadingProgressBar();
    }

    @Override
    public void onPageLoading(int progress) {
        updateLoadingProgressBar(progress);
    }

    @Override
    public void onPageFinished(String url) {
        hideLoadingProgressBar();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        showError();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
