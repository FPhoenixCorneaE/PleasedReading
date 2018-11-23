package com.wkz.framework.widgets.recycleradapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class FRCommonRecyclerAdapter<T> extends FRBaseRecyclerAdapter<T> {
    private OnItemClickListener<T> mItemClickListener;

    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    private ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public FRCommonRecyclerAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    protected abstract void convert(FRRecyclerViewHolder holder, T data, int position);

    protected abstract int getItemLayoutId();

    @NonNull
    @Override
    public FRRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return FRRecyclerViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position - getHeaderCount());
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final FRRecyclerViewHolder fRRecyclerViewHolder = (FRRecyclerViewHolder) holder;
        convert(fRRecyclerViewHolder, getAllData().get(position), position);

        fRRecyclerViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(fRRecyclerViewHolder, getAllData().get(position), position);
                }
            }
        });

        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (fRRecyclerViewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                fRRecyclerViewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(fRRecyclerViewHolder, getAllData().get(position), position);
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    /**
     * item点击事件
     *
     * @param itemClickListener
     */
    public FRCommonRecyclerAdapter<T> setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    /**
     * item子view点击事件
     *
     * @param viewId
     * @param itemChildClickListener
     */
    public FRCommonRecyclerAdapter<T> setOnItemChildClickListener(@IdRes int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
        return this;
    }

}
