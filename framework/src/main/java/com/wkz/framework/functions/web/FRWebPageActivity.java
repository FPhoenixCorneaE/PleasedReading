package com.wkz.framework.functions.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wkz.framework.R;
import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.constants.FRConstant;
import com.wkz.framework.databinding.FrActivityWebPageBinding;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.utils.SizeUtils;
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
                .setProgressColor(ResourceUtils.getColor(R.color.fr_statusBar))
                .setRectRound(SizeUtils.dp2px(2f))
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择相册图片返回结果
        if (mDataBinding.frWvWebPage != null) {
            mDataBinding.frWvWebPage.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        //判断webview是否可以返回上一页
        if (mDataBinding.frWvWebPage != null && mDataBinding.frWvWebPage.canGoBack()) {
            mDataBinding.frWvWebPage.goBack();
            return;
        }
        super.onBackPressed();
    }

    private void addImageClickListener() {
        //遍历页面中所有img的节点，因为节点里面的图片的url即objs[i].src，保存所有图片的src.
        //为每个图片设置点击事件，objs[i].onclick
        mDataBinding.frWvWebPage.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "window." + FRCommonJSInterface.NAME + ".clearImage(); " +
                "for(var i=0;i<objs.length;i++) " +
                "{" +
                "window." + FRCommonJSInterface.NAME + ".setImageUrl(objs[i].src); " +
                " objs[i].onclick=function() " +
                " { " +
                " window." + FRCommonJSInterface.NAME + ".clickImage(this.src); " +
                " } " +
                "}" +
                "})()");
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
        //当页面加载完成，就调用我们的addImageClickListener()函数
        addImageClickListener();
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
