package com.wkz.pleasedreading.main.gank;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.orhanobut.logger.Logger;
import com.wkz.framework.bases.FRBaseFragment;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.models.FRBundle;
import com.wkz.framework.widgets.tab.FRColorTrackTabLayout;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constants.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentGankBinding;
import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.widget.FRImageViewer;
import com.wkz.viewer.widget.FRScaleImageView;

import java.util.ArrayList;

public class PRGankFragment extends FRBaseFragment<PRGankContract.IGankPresenter> implements PRGankContract.IGankView {

    private PrFragmentGankBinding mDataBinding;
    private FRImageViewer mFrImageViewer;

    private <T extends PRGankFragment> T create(String title, Class<T> tClass) {
        T childFragment = null;
        try {
            //class.newInstance()是通过无参构造函数实例化的，一个对象默认是有一个无参构造函数，
            //如果有一个有参构造函数，无参构造函数就不存在了，在通过反射获得对象会抛出 java.lang.InstantiationException 异常。
            childFragment = tClass.newInstance();
            childFragment.setArguments(new FRBundle().putString(PRConstant.PR_FRAGMENT_TITLE, title).create());
            if (childFragment instanceof PRGankChildFragment) {
                ((PRGankChildFragment) childFragment).setImageViewer(mFrImageViewer);
            } else if (childFragment instanceof PRGankWelfareFragment) {
                ((PRGankWelfareFragment) childFragment).setImageViewer(mFrImageViewer);
            }
        } catch (IllegalAccessException e) {
            Logger.e(e.toString());
        } catch (java.lang.InstantiationException e) {
            Logger.e(e.toString());
        }
        return childFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_gank;
    }

    @Override
    public FRBasePresenter createPresenter() {
        initImageViewer();
        return new PRGankPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRGankModel.class);
    }

    @Override
    public void initView() {
        mDataBinding = ((PrFragmentGankBinding) mViewDataBinding);
        mDataBinding.prCttlTab.init(
                new FRColorTrackTabLayout.Builder()
                        .with(mDataBinding.prVpPager)
                        .setFragmentManager(getChildFragmentManager())
                        .setPageFragments(new ArrayList<Fragment>() {
                            private static final long serialVersionUID = 5955263809472083516L;

                            {
                                add(create("Android", PRGankChildFragment.class));
                                add(create("iOS", PRGankChildFragment.class));
                                add(create("前端", PRGankChildFragment.class));
                                add(create("App", PRGankChildFragment.class));
                                add(create("休息视频", PRGankChildFragment.class));
                                add(create("福利", PRGankWelfareFragment.class));
                                add(create("拓展资源", PRGankChildFragment.class));
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
