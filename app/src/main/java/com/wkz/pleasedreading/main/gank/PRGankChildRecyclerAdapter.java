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
}
