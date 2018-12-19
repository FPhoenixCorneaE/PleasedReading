package com.wkz.pleasedreading;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wkz.bannerlayout.annotation.FRProgressShapeMode;
import com.wkz.bannerlayout.widget.FRProgressDrawable;
import com.wkz.framework.base.BaseActivity;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.utils.IntentUtils;
import com.wkz.framework.utils.ResourceUtils;
import com.wkz.pleasedreading.databinding.PrActivitySplashBinding;
import com.wkz.pleasedreading.main.PRMainActivity;

public class PRSplashActivity extends BaseActivity {

    private static final long SPLASH_TIME = 4000;
    private PrActivitySplashBinding mDataBinding;

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_splash;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseModel createModel() {
        return null;
    }

    @Override
    public void initView() {
        mDataBinding = (PrActivitySplashBinding) mViewDataBinding;
        FRProgressDrawable progressDrawable;
        mDataBinding.prTvProgress.setBackground(progressDrawable =
                new FRProgressDrawable.Builder(mContext)
                        .setShapeMode(FRProgressShapeMode.RING)
                        .setRadius(18f)
                        .setRingThickness(1.5f)
                        .setBackgroundColor(ResourceUtils.getColor(R.color.fr_color_white))
                        .setProgressColor(ResourceUtils.getColor(R.color.fr_color_light_red))
                        .setDuration(SPLASH_TIME)
                        .setAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                IntentUtils.startActivity(mContext, PRMainActivity.class);
                                finish();
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
        progressDrawable.start();
    }

    @Override
    public void initListener() {
        mDataBinding.prRlSkip.setOnClickListener(v -> {
            IntentUtils.startActivity(mContext, PRMainActivity.class);
            finish();
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}
