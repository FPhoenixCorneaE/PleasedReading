package com.wkz.pleasedreading.myself.localvideo;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;
import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterLocalVideoBinding;

import java.util.List;

public class PRLocalVideoRecyclerAdapter extends FRCommonRecyclerAdapter<FRVideoBean> {

    public PRLocalVideoRecyclerAdapter(Context context, List<FRVideoBean> datas, boolean isOpenLoadMore) {
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
    protected void convert(FRRecyclerViewHolder holder, FRVideoBean data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            viewHolder.mDataBinding.setVideoBean(data);
            //迫使数据立即绑定而不是在下一帧的时候才绑定
            //假设没使用executePendingBindings()方法，由于在下一帧的时候才会绑定，
            //view就会绑定错误的data，测量也会出错。
            //因此，executePendingBindings()是很重要的。
            viewHolder.mDataBinding.executePendingBindings();
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pr_adapter_local_video;
    }

    private static class ViewHolder extends FRRecyclerViewHolder {


        private final PrAdapterLocalVideoBinding mDataBinding;

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

    @BindingAdapter({"setVideoThumbnail"})
    public static void setVideoThumbnail(FRGlideImageView glideImageView, Bitmap thumbnail) {
        glideImageView.loadImage(thumbnail, R.drawable.pr_shape_placeholder_grey, null);
    }
}
