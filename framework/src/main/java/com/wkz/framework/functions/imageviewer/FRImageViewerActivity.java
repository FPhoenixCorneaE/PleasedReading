package com.wkz.framework.functions.imageviewer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.wkz.framework.R;
import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.constants.FRConstant;
import com.wkz.framework.databinding.FrActivityImageViewerBinding;
import com.wkz.framework.utils.GlideDownloadImageUtils;
import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.listener.OnImageChangedListener;
import com.wkz.viewer.listener.OnItemClickListener;
import com.wkz.viewer.listener.OnItemLongClickListener;
import com.wkz.viewer.listener.OnPreviewStatusListener;
import com.wkz.viewer.widget.FRScaleImageView;

import java.util.List;

public class FRImageViewerActivity extends FRBaseActivity implements View.OnClickListener {

    private FrActivityImageViewerBinding mDataBinding;
    private boolean isHideCover;

    @Override
    public int getLayoutId() {
        return R.layout.fr_activity_image_viewer;
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
        mDataBinding = (FrActivityImageViewerBinding) mViewDataBinding;
    }

    @Override
    public void initListener() {
        mDataBinding.frImageCover.frTvCoverGoBack.setOnClickListener(this);
        mDataBinding.frImageCover.frIbCoverMore.setOnClickListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<FRViewData> viewDatas = getIntent().getParcelableArrayListExtra(FRConstant.IMAGE_VIEWER_VIEW_DATA);
        List<String> imageDatas = (List<String>) getIntent().getSerializableExtra(FRConstant.IMAGE_VIEWER_IMAGE_DATA);
        int startPosition = getIntent().getIntExtra(FRConstant.IMAGE_VIEWER_START_POSITION, 0);
        mDataBinding.frImageViewer
                .setViewData(viewDatas)
                .setImageData(imageDatas)
                .setStartPosition(startPosition)
                .setImageScaleable(true)
                .setImageMinScale(0.75f)
                .setImageMaxScale(3f)
                .doEnterAnim(false)
                .doExitAnim(false)
                .setImageLoader(new IImageLoader<String>() {
                    @Override
                    public void displayImage(int position, String srcUrl, ImageView imageView) {
                        final FRScaleImageView scaleImageView = (FRScaleImageView) imageView.getParent();
                        Glide.with(imageView.getContext())
                                .load(srcUrl)
                                .apply(new RequestOptions()
                                        .centerCrop()
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                )
                                .into(new ImageViewTarget<Drawable>(imageView) {

                                    @Override
                                    public void onLoadStarted(@Nullable Drawable placeholder) {
                                        super.onLoadStarted(placeholder);
                                        scaleImageView.showProgess();
                                        imageView.setImageDrawable(placeholder);
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        scaleImageView.removeProgressView();
                                        imageView.setImageDrawable(errorDrawable);
                                    }

                                    @Override
                                    protected void setResource(@Nullable Drawable resource) {
                                        scaleImageView.removeProgressView();
                                        imageView.setImageDrawable(resource);
                                    }
                                });

                    }
                })
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public boolean onItemClick(int i, View view) {
                        isHideCover = !isHideCover;
                        mDataBinding.setIsHideCover(isHideCover);
                        return true;
                    }
                })
                .setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int position, View view) {
                        GlideDownloadImageUtils.savePictureToLocalAlbum(mContext, imageDatas.get(position));
                    }
                })
                .setOnImageChangedListener(new OnImageChangedListener() {
                    @Override
                    public void onImageSelected(int i, FRScaleImageView frScaleImageView) {

                    }
                })
                .setOnPreviewStatusListener(new OnPreviewStatusListener() {
                    @Override
                    public void onPreviewStatus(@FRImageViewerState int state, FRScaleImageView frScaleImageView) {
                        switch (state) {
                            case FRImageViewerState.STATE_CLOSING:
                                break;
                            case FRImageViewerState.STATE_COMPLETE_CLOSE:
                                finish();
                                break;
                            case FRImageViewerState.STATE_COMPLETE_OPEN:
                                break;
                            case FRImageViewerState.STATE_COMPLETE_REBACK:
                                mDataBinding.setIsHideCover(false);
                                break;
                            case FRImageViewerState.STATE_DRAGGING:
                                mDataBinding.setIsHideCover(true);
                                break;
                            case FRImageViewerState.STATE_OPENING:
                                break;
                            case FRImageViewerState.STATE_READY_CLOSE:
                                break;
                            case FRImageViewerState.STATE_READY_OPEN:
                                break;
                            case FRImageViewerState.STATE_READY_REBACK:
                                break;
                            case FRImageViewerState.STATE_REBACKING:
                                break;
                            case FRImageViewerState.STATE_SILENCE:
                                break;
                            case FRImageViewerState.STATE_WATCHING:
                                break;
                        }
                    }
                })
                .watch()
        ;
    }

    @Override
    public void onClick(View v) {
        if (v == mDataBinding.frImageCover.frTvCoverGoBack) {
            finish();
        } else if (v == mDataBinding.frImageCover.frIbCoverMore) {

        }
    }
}
