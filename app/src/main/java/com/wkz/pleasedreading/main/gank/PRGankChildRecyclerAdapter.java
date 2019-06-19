package com.wkz.pleasedreading.main.gank;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wkz.framework.utils.ScreenUtils;
import com.wkz.framework.widgets.ninegridimagelayout.FRMultiImageLayout;
import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterGankChildRecyclerBinding;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.widget.FRImageViewer;

import java.util.ArrayList;
import java.util.List;

public class PRGankChildRecyclerAdapter extends FRCommonRecyclerAdapter<PRGankBean.ResultsBean> {

    private FRImageViewer mFRImageViewer;

    public PRGankChildRecyclerAdapter(Context context, List<PRGankBean.ResultsBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @NonNull
    @Override
    public FRRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(FRRecyclerViewHolder holder, PRGankBean.ResultsBean data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.setPrGankBean(data);
            //迫使数据立即绑定而不是在下一帧的时候才绑定
            //假设没使用executePendingBindings()方法，由于在下一帧的时候才会绑定，
            //view就会绑定错误的data，测量也会出错。
            //因此，executePendingBindings()是很重要的。
            viewHolder.mDataBinding.executePendingBindings();
            if (data.getImages() != null) {
                viewHolder.mDataBinding.prMilImages.setVisibility(View.VISIBLE);
                viewHolder.mDataBinding.prMilImages.setImages(data.getImages());
                viewHolder.mDataBinding.prMilImages.setOnItemClickListener(new FRMultiImageLayout.OnItemClickListener() {
                    @Override
                    public void onClick(List<String> mDatas, ImageView itemView, int position) {
                        //图片大图浏览
                        goToImageViewer(mDatas, position);
                    }

                    private void goToImageViewer(List<String> mDatas, int position) {
                        List<FRViewData> mViewList = new ArrayList<>();
                        for (int i = 0; i < mDatas.size(); i++) {
                            FRViewData viewData = new FRViewData();
                            View childView = viewHolder.mDataBinding.prMilImages.getChildAt(i);
                            if (childView != null) {
                                int[] location = new int[2];
                                // 获取在整个屏幕内的绝对坐标
                                childView.getLocationOnScreen(location);
                                viewData.setTargetX(location[0])
                                        // 此处注意，获取 Y 轴坐标时，需要根据实际情况来处理《状态栏》的高度，判断是否需要计算进去
                                        .setTargetY(location[1])
                                        .setTargetWidth(ScreenUtils.getScreenWidth())
                                        .setTargetHeight(ScreenUtils.getScreenHeight());
                            }
                            mViewList.add(viewData);
                        }
                        mFRImageViewer.setImageData(data.getImages())
                                .setViewData(mViewList)
                                .setStartPosition(position)
                                .watch();
                    }
                });
            } else {
                viewHolder.mDataBinding.prMilImages.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pr_adapter_gank_child_recycler;
    }

    private static class ViewHolder extends FRRecyclerViewHolder {


        private final PrAdapterGankChildRecyclerBinding mDataBinding;

        /**
         * 私有构造方法
         *
         * @param itemView
         */
        private ViewHolder(View itemView) {
            super(itemView);
            mDataBinding = DataBindingUtil.bind(itemView);
        }
    }

    public PRGankChildRecyclerAdapter setFRImageViewer(FRImageViewer frImageViewer) {
        this.mFRImageViewer = frImageViewer;
        return this;
    }
}
