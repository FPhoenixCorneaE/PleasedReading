package com.wkz.pleasedreading.main.toutiao;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wkz.framework.utils.GlideUtils;
import com.wkz.framework.utils.ScreenUtils;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.recycleradapter.FRCommonRecyclerAdapter;
import com.wkz.framework.widgets.recycleradapter.FRRecyclerViewHolder;
import com.wkz.pleasedreading.R;
import com.wkz.pleasedreading.databinding.PrAdapterToutiaoChildRecyclerBinding;
import com.wkz.videoplayer.constant.FRConstantKeys;
import com.wkz.videoplayer.controller.FRVideoPlayerController;

import java.util.List;

public class PRTouTiaoChildRecyclerAdapter extends FRCommonRecyclerAdapter<PRTouTiaoVideoBean.DataBean> {

    public PRTouTiaoChildRecyclerAdapter(Context context, List<PRTouTiaoVideoBean.DataBean> datas, boolean isOpenLoadMore) {
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
    protected void convert(FRRecyclerViewHolder holder, PRTouTiaoVideoBean.DataBean data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.mDataBinding != null) {
            //迫使数据立即绑定而不是在下一帧的时候才绑定
            //假设没使用executePendingBindings()方法，由于在下一帧的时候才会绑定，
            //view就会绑定错误的data，测量也会出错。
            //因此，executePendingBindings()是很重要的。
            viewHolder.mDataBinding.executePendingBindings();

            viewHolder.setVideoInfo(data);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pr_adapter_toutiao_child_recycler;
    }

    private static class ViewHolder extends FRRecyclerViewHolder {

        private PrAdapterToutiaoChildRecyclerBinding mDataBinding;
        private FRVideoPlayerController mController;
        private Gson mGson = new Gson();

        /**
         * 构造方法
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mDataBinding = DataBindingUtil.bind(itemView);

            //创建视频控制器，只要创建一次就可以了呢
            mController = new FRVideoPlayerController(itemView.getContext());

            if (mDataBinding != null) {
                //设置宽高比例为16:9
                ViewGroup.LayoutParams layoutParams = mDataBinding.prVpVideo.getLayoutParams();
                layoutParams.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30f);
                layoutParams.height = (int) (layoutParams.width * 9f / 16f);
                mDataBinding.prVpVideo.setLayoutParams(layoutParams);

                mDataBinding.prVpVideo.setController(mController);
            }
        }

        public void setVideoInfo(PRTouTiaoVideoBean.DataBean data) {
            PRTouTiaoVideoBean.DataBean.ContentBean content = mGson.fromJson(data.getContent(), PRTouTiaoVideoBean.DataBean.ContentBean.class);
            //设置视频标题
            mController.setTitle(content.getTitle());
            //设置加载动画
            mController.setLoadingType(FRConstantKeys.Loading.LOADING_RING);
            //显示中心播放按钮
            mController.setCenterPlayer(true, 0);
            //视频封面
            if (content.getVideo_detail_info() != null &&
                    content.getVideo_detail_info().getDetail_video_large_image() != null) {
                GlideUtils.setupImagePlaceColorRes(
                        mController.imageView(),
                        content.getVideo_detail_info().getDetail_video_large_image().getUrl(),
                        R.color.fr_color_black_translucent50
                );
            }
            if (!TextUtils.isEmpty(content.getDisplay_url())) {
                mDataBinding.prVpVideo.setUp(content.getDisplay_url().replace("http://", "https://"), null);
            } else if (!TextUtils.isEmpty(content.getUrl())) {
                mDataBinding.prVpVideo.setUp(content.getUrl(), null);
            } else if (!TextUtils.isEmpty(content.getArticle_url())) {
                mDataBinding.prVpVideo.setUp(content.getArticle_url(), null);
            }
        }
    }
}
