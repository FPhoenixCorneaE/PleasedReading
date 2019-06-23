package com.wkz.pleasedreading.main.gank.welfare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wkz.parallaxrecycler.AbstractParallaxRecyclerAdapter;
import com.wkz.parallaxrecycler.ParallaxImageView;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterGankWelfareParallaxRecyclerBinding;

import java.util.List;

public class PRGankWelfareParallaxRecyclerAdapter extends AbstractParallaxRecyclerAdapter<String> {


    public PRGankWelfareParallaxRecyclerAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    @NonNull
    public AbstractParallaxRecyclerAdapter.ParallaxViewHolder onCreateViewHolder(@NonNull ViewGroup var1, int var2) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(this.getLayoutId(), var1, false));
    }

    @Override
    public int getLayoutId() {
        return R.layout.pr_adapter_gank_welfare_parallax_recycler;
    }

    @Override
    public void setData(ParallaxViewHolder holder, String data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.setWelfareUrl(data);
            //迫使数据立即绑定而不是在下一帧的时候才绑定
            //假设没使用executePendingBindings()方法，由于在下一帧的时候才会绑定，
            //view就会绑定错误的data，测量也会出错。
            //因此，executePendingBindings()是很重要的。
            viewHolder.mDataBinding.executePendingBindings();
        }
    }

    public static class ViewHolder extends AbstractParallaxRecyclerAdapter.ParallaxViewHolder {


        final PrAdapterGankWelfareParallaxRecyclerBinding mDataBinding;

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
        public static void loadWelfare(ParallaxImageView prIvWelfare, String welfareUrl) {
            Glide.with(prIvWelfare)
                    .load(welfareUrl)
                    .apply(new RequestOptions()
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.pr_shape_placeholder_grey)
                            .error(R.drawable.pr_shape_placeholder_grey))
                    .into(prIvWelfare);
        }
    }
}
