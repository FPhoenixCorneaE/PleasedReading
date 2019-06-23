package com.wkz.bannerlayout.widget;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wkz.bannerlayout.listener.FRBannerModelCallBack;
import com.wkz.bannerlayout.listener.FRImageDisplayManager;
import com.wkz.bannerlayout.listener.OnBannerImageClickListener;

import java.util.List;

public class FRBannerAdapter extends PagerAdapter {

    private FRImageDisplayManager imageLoaderManage;
    private OnBannerImageClickListener imageClickListener;
    private List<FRBannerModelCallBack> imageList;
    private boolean isGuide;

    public int getCount() {
        return this.isGuide ? this.imageList.size() : Integer.MAX_VALUE;
    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView img = imageLoaderManage.display(container, this.imageList.get(this.getPosition(position)));
        img.setLayoutParams(new FRBannerLayout.LayoutParams(FRBannerLayout.LayoutParams.MATCH_PARENT, FRBannerLayout.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (imageClickListener != null) {
                    imageClickListener.onBannerClick(v, getPosition(position), imageList.get(getPosition(position)));
                }

            }
        });
        container.addView((View) img);
        return img;
    }

    private int getPosition(int position) {
        return position % this.imageList.size();
    }

    public void setImageClickListener(@NonNull OnBannerImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public FRBannerAdapter(@NonNull List imageList, @Nullable FRImageDisplayManager imageLoaderManage, Drawable errorDrawable, Drawable placeholderDrawable, boolean isGuide) {
        super();
        this.imageList = imageList;
        this.isGuide = isGuide;
        if (imageLoaderManage == null) {
            final RequestOptions requestOptions = new RequestOptions()
                    .placeholder(placeholderDrawable)
                    .error(errorDrawable)
                    .centerCrop();
            this.imageLoaderManage = new FRImageDisplayManager() {
                @NonNull
                public ImageView display(@NonNull ViewGroup container, @NonNull FRBannerModelCallBack model) {
                    ImageView imageView = new ImageView(container.getContext());
                    Glide.with(imageView.getContext())
                            .load(model.getBannerUrl())
                            .apply(requestOptions)
                            .into(imageView);
                    return imageView;
                }
            };
        } else {
            this.imageLoaderManage = imageLoaderManage;
        }

    }
}
