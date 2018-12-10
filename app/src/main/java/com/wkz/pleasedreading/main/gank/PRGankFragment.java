package com.wkz.pleasedreading.main.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wkz.framework.base.BaseFragment;
import com.wkz.framework.base.BaseModel;
import com.wkz.framework.base.BasePresenter;
import com.wkz.framework.factorys.ModelFactory;
import com.wkz.framework.widgets.tab.FRColorTrackTabLayout;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrFragmentGankBinding;

import java.util.ArrayList;

public class PRGankFragment extends BaseFragment implements PRGankContract.IGankView {

    private PrFragmentGankBinding mDataBinding;
    private PRGankPresenter mPresenter;

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
        mDataBinding.prCttlTab.init(
                new FRColorTrackTabLayout.Builder()
                        .with(mDataBinding.prVpPager)
                        .setFragmentManager(getChildFragmentManager())
                        .setPageFragments(new ArrayList<Fragment>() {
                            private static final long serialVersionUID = 5955263809472083516L;

                            {
                                add(PRGankChildFragment.create("Android"));
                                add(PRGankChildFragment.create("iOS"));
                                add(PRGankChildFragment.create("前端"));
                                add(PRGankChildFragment.create("App"));
                                add(PRGankChildFragment.create("休息视频"));
                                add(PRGankWelfareFragment.create("福利"));
                                add(PRGankChildFragment.create("拓展资源"));
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
}
