package com.wkz.pleasedreading.main.gank;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wkz.framework.utils.GlideUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterGankVideoRecyclerBinding;
import com.wkz.videoplayer.controller.FRVideoPlayerController;

import java.util.List;

public class PRGankVideoRecyclerAdapter extends FRCommonRecyclerAdapter<PRGankBean.ResultsBean> {

    public PRGankVideoRecyclerAdapter(Context context, List<PRGankBean.ResultsBean> datas, boolean isOpenLoadMore) {
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
            if (data.getUrl() != null) {
                viewHolder.mDataBinding.prVpVideo.setVisibility(View.VISIBLE);
                viewHolder.mFrVideoPlayerController.setTitle(data.getDesc());
                GlideUtils.setupRoundedImagePlaceColorRes(viewHolder.mFrVideoPlayerController.imageView(), data.getCover(), SizeUtils.dp2px(5f), R.color.fr_color_black_translucent50);
                viewHolder.mDataBinding.prVpVideo.setUp(data.getPlayAddr(), null);
            } else {
                viewHolder.mDataBinding.prVpVideo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pr_adapter_gank_video_recycler;
    }

    public static class ViewHolder extends FRRecyclerViewHolder {

        final PrAdapterGankVideoRecyclerBinding mDataBinding;
        private FRVideoPlayerController mFrVideoPlayerController;

        /**
         * 私有构造方法
         *
         * @param itemView
         */
        private ViewHolder(View itemView) {
            super(itemView);
            mDataBinding = DataBindingUtil.bind(itemView);

            if (mDataBinding != null) {
                //创建视频播放控制器，只要创建一次就可以呢
                mFrVideoPlayerController = new FRVideoPlayerController(itemView.getContext());
                mDataBinding.prVpVideo.setController(mFrVideoPlayerController);
            }
        }
    }
}
