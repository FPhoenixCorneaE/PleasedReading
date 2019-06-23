package com.wkz.pleasedreading.main.gank.welfare;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wkz.utils.SizeUtils;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;
import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterGankWelfareRecyclerBinding;

import java.util.List;

public class PRGankWelfareRecyclerAdapter extends FRCommonRecyclerAdapter<String> {

    public PRGankWelfareRecyclerAdapter(Context context, List<String> datas, boolean isOpenLoadMore) {
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
    protected void convert(FRRecyclerViewHolder holder, String data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.setWelfareUrl(data);
            //迫使数据立即绑定而不是在下一帧的时候才绑定
            //假设没使用executePendingBindings()方法，由于在下一帧的时候才会绑定，
            //view就会绑定错误的data，测量也会出错。
            //因此，executePendingBindings()是很重要的。
            viewHolder.mDataBinding.executePendingBindings();

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(viewHolder.mDataBinding.prIvWelfare.getLayoutParams());
            lp.height = SizeUtils.dp2px(240f + 20 * (position % 4));
            viewHolder.mDataBinding.prIvWelfare.setLayoutParams(lp);

            viewHolder.mDataBinding.prIvWelfare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(viewHolder, data, position);
                    }
                }
            });
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pr_adapter_gank_welfare_recycler;
    }

    public static class ViewHolder extends FRRecyclerViewHolder {


        final PrAdapterGankWelfareRecyclerBinding mDataBinding;

        /**
         * 私有构造方法
         *
         * @param itemView
         */
        private ViewHolder(View itemView) {
            super(itemView);
            mDataBinding = DataBindingUtil.bind(itemView);
        }

        /**
         * @param prIvWelfare
         * @param welfareUrl
         */
        @BindingAdapter({"welfareUrl"})
        public static void loadWelfare(FRGlideImageView prIvWelfare, String welfareUrl) {
            prIvWelfare.loadImage(welfareUrl, R.drawable.pr_shape_placeholder_grey);
        }
    }
}
