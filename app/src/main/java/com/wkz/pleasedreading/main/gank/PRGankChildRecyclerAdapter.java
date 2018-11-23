package com.wkz.pleasedreading.main.gank;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterGankChildRecyclerBinding;

import java.util.List;

public class PRGankChildRecyclerAdapter extends FRCommonRecyclerAdapter<PRGankBean.ResultsBean> {

    public PRGankChildRecyclerAdapter(Context context, List<PRGankBean.ResultsBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @NonNull
    @Override
    public FRRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false));
    }

    @Override
    protected void convert(FRRecyclerViewHolder holder, PRGankBean.ResultsBean data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.setPrGankBean(data);
            viewHolder.mDataBinding.prMilImages.setImages(data.getImages());
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
}
