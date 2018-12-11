package com.wkz.pleasedreading.main.gank;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.widgets.tab.FRColorTrackTabLayout;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrFragmentGankBinding;
import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.widget.FRImageViewer;
import com.wkz.viewer.widget.FRScaleImageView;

import java.util.ArrayList;

public class PRGankFragment extends BaseFragment implements PRGankContract.IGankView {

    private PrFragmentGankBinding mDataBinding;
    private PRGankPresenter mPresenter;
    private FRImageViewer mFrImageViewer;

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_gank;
    }

    @NonNull
    @Override
    public BasePresenter createPresenter() {
        return mPresenter = new PRGankPresenter(this, this);
    }

    @Override
    public BaseModel createModel() {
        return ModelFactory.createModel(PRGankModel.class);
    }

    @Override
    public void initView() {
        mDataBinding = ((PrFragmentGankBinding) mViewDataBinding);
        initImageViewer();
        mDataBinding.prCttlTab.init(
                new FRColorTrackTabLayout.Builder()
                        .with(mDataBinding.prVpPager)
                        .setFragmentManager(getChildFragmentManager())
                        .setPageFragments(new ArrayList<Fragment>() {
                            private static final long serialVersionUID = 5955263809472083516L;

                            {
                                add(PRGankChildFragment.create("Android").setFrImageViewer(mFrImageViewer));
                                add(PRGankChildFragment.create("iOS").setFrImageViewer(mFrImageViewer));
                                add(PRGankChildFragment.create("前端").setFrImageViewer(mFrImageViewer));
                                add(PRGankChildFragment.create("App").setFrImageViewer(mFrImageViewer));
                                add(PRGankChildFragment.create("休息视频").setFrImageViewer(mFrImageViewer));
                                add(PRGankWelfareFragment.create("福利").setFrImageViewer(mFrImageViewer));
                                add(PRGankChildFragment.create("拓展资源").setFrImageViewer(mFrImageViewer));
                            }
                        })
                        .setPageTitles(new ArrayList<String>() {
                            private static final long serialVersionUID = -2403141955833935233L;

                            {
                                add("Android");
                                add("iOS");
                                add("前端");
                                add("App");
                                add("休息视频");
                                add("福利");
                                add("拓展资源");
                            }
                        })
        );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    private void initImageViewer() {
        mFrImageViewer = new FRImageViewer(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFrImageViewer.setLayoutParams(layoutParams);
        mFrImageViewer.setBackgroundColor(Color.BLACK);
        mFrImageViewer.setImageLoader(new IImageLoader<String>() {
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
        });
        mFrImageViewer.setVisibility(View.GONE);
        ((ViewGroup) mContext.getWindow().getDecorView()).addView(mFrImageViewer);
    }
}
