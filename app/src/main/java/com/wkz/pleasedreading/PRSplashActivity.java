package com.wkz.pleasedreading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

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
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public int getLayoutId() {
        return R.layout.pr_activity_splash;
    }

    @NonNull
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
                        .build()
        );
        progressDrawable.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtils.startActivity(mContext, PRMainActivity.class);
                finish();
            }
        }, SPLASH_TIME);
    }

    @Override
    public void initListener() {
        mDataBinding.prRlSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivity(mContext, PRMainActivity.class);
                finish();
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
