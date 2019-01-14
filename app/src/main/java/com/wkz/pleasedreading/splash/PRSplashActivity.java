package com.wkz.pleasedreading.splash;

import android.animation.Animator;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding2.view.RxView;
import com.wkz.bannerlayout.annotation.FRProgressShapeMode;
import com.wkz.bannerlayout.widget.FRProgressDrawable;
import com.wkz.framework.bases.FRBaseActivity;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.utils.IntentUtils;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrActivitySplashBinding;
import com.wkz.pleasedreading.main.PRMainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PRSplashActivity extends FRBaseActivity<PRSplashContract.ISplashPresenter> implements PRSplashContract.ISplashView {

    private static final long SPLASH_TIME = 4000;
    private PrActivitySplashBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_splash;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRSplashPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRSplashModel.class);
    }

    @Override
    public void initView() {
        mDataBinding = (PrActivitySplashBinding) mViewDataBinding;
        FRProgressDrawable mProgressDrawable;
        mDataBinding.prTvProgress.setBackground(mProgressDrawable =
                new FRProgressDrawable.Builder(mContext)
                        .setShapeMode(FRProgressShapeMode.RING)
                        .setRadius(18f)
                        .setRingThickness(1.5f)
                        .setBackgroundColor(ResourceUtils.getColor(R.color.fr_color_transparent))
                        .setProgressColor(ResourceUtils.getColor(R.color.fr_color_light_red))
                        .setReverse(true)
                        .setDuration(SPLASH_TIME)
                        .setAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (!mContext.isFinishing()) {
                                    IntentUtils.startActivity(mContext, PRMainActivity.class);
                                    finish();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .build()
        );
        mProgressDrawable.start();
    }

    @Override
    public void initListener() {
        RxView.clicks(mDataBinding.prRlSkip)
                //throttleFirst操作符,设置间隔1秒才能发送下一个事件
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        IntentUtils.startActivity(mContext, PRMainActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDailyImage();
    }

    @Override
    public void onSuccess(@Nullable Object data) {
        super.onSuccess(data);
        mDataBinding.setSplashUrl((String) data);
    }

    @BindingAdapter({"splashUrl"})
    public static void setSplashUrl(FRGlideImageView prIvSplash, String splashUrl) {
        prIvSplash.loadImage(splashUrl, R.mipmap.pr_img_splash);
    }
}
