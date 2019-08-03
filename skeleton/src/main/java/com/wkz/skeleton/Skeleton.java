package com.wkz.skeleton;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class Skeleton {

    public static SkeletonRecyclerViewScreen.Builder bind(RecyclerView recyclerView) {
        return new SkeletonRecyclerViewScreen.Builder(recyclerView);
    }

    public static SkeletonViewScreen.Builder bind(View view) {
        return new SkeletonViewScreen.Builder(view);
    }

}
