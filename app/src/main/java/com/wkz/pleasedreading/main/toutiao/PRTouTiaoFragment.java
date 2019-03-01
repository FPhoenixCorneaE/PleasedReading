package com.wkz.pleasedreading.main.toutiao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.wkz.framework.bases.FRBaseFragment;
import com.wkz.framework.bases.FRBasePresenter;
import com.wkz.framework.bases.IFRBaseModel;
import com.wkz.framework.factorys.FRModelFactory;
import com.wkz.framework.models.FRBundle;
import com.wkz.framework.widgets.tab.FRColorTrackTabLayout;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.constants.PRConstant;
import com.wkz.pleasedreading.databinding.PrFragmentToutiaoBinding;

import java.util.ArrayList;

public class PRTouTiaoFragment extends FRBaseFragment<PRTouTiaoContract.ITouTiaoPresenter> implements PRTouTiaoContract.ITouTiaoView {

    private PrFragmentToutiaoBinding mDataBinding;
    private String[] mPageTitles = new String[]{
            "推荐", "音乐", "搞笑", "社会", "小品", "生活",
            "影视", "娱乐", "呆萌", "游戏", "原创", "开眼"
    };
    private String[] mCatagoryIds = new String[]{
            "video", "subv_voice", "subv_funny", "subv_society", "subv_comedy", "subv_life",
            "subv_movie", "subv_entertainment", "subv_cute", "subv_game", "subv_boutique", "subv_broaden_view"
    };

    private <T extends PRTouTiaoFragment> T create(String catagory, Class<T> tClass) {
        T childFragment = null;
        try {
            //class.newInstance()是通过无参构造函数实例化的，一个对象默认是有一个无参构造函数，
            //如果有一个有参构造函数，无参构造函数就不存在了，在通过反射获得对象会抛出 java.lang.InstantiationException 异常。
            childFragment = tClass.newInstance();
            childFragment.setArguments(new FRBundle().putString(PRConstant.PR_TOUTIAO_VIDEO_CATAGORY_ID, catagory).create());
        } catch (IllegalAccessException e) {
            Logger.e(e.toString());
        } catch (java.lang.InstantiationException e) {
            Logger.e(e.toString());
        }
        return childFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pr_fragment_toutiao;
    }

    @Override
    public FRBasePresenter createPresenter() {
        return new PRTouTiaoPresenter(this);
    }

    @Override
    public IFRBaseModel createModel() {
        return FRModelFactory.createModel(PRTouTiaoModel.class);
    }

    @Override
    public void initView() {
        mDataBinding = (PrFragmentToutiaoBinding) mViewDataBinding;
        mDataBinding.prCttlTab.init(
                new FRColorTrackTabLayout.Builder()
                        .with(mDataBinding.prVpPager)
                        .setFragmentManager(getChildFragmentManager())
                        .setPageFragments(new ArrayList<Fragment>() {
                            private static final long serialVersionUID = 5955263809472083516L;

                            {
                                for (String catagory : mCatagoryIds) {
                                    add(create(catagory, PRTouTiaoChildFragment.class));
                                }
                            }
                        })
                        .setPageTitles(mPageTitles)
        );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onGetVideoContentSuccess(int position, String videoUrl) {

    }

    @Override
    public void onGetVideoContentFailure(String errorMsg) {

    }
}
