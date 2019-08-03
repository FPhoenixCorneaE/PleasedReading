package com.wkz.skeleton;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShimmerViewHolder extends RecyclerView.ViewHolder {

    public ShimmerViewHolder(LayoutInflater inflater, ViewGroup parent, int innerViewResId) {
        super(inflater.inflate(R.layout.skeleton_layout_shimmer, parent, false));
        ViewGroup layout = (ViewGroup) itemView;
        View view = inflater.inflate(innerViewResId, layout, false);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            layout.setLayoutParams(lp);
        }
        layout.addView(view);
    }
}
