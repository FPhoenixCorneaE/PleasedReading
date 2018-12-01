package com.wkz.framework.widgets.ninegridimagelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wkz.framework.R;
import com.wkz.framework.utils.SizeUtils;
import com.wkz.framework.widgets.glideimageview.FRGlideImageView;

import java.util.Arrays;
import java.util.List;

/**
 * 九宫格显示多图或item，仿照QQ空间图片列表和百度贴吧图片列表
 */
@SuppressWarnings("ALL")
public class FRMultiImageLayout extends ViewGroup {

    private String TAG = FRMultiImageLayout.class.getSimpleName();
    private static final int DEFAULT_DIVIDE_SPACE = SizeUtils.dp2px(4f);
    private static final int DEFAULT_PLACE_HOLDER = -1;
    private static final int TYPE_QQSpace = 0;
    private static final int TYPE_BaiDuPostBar = 1;

    private boolean isDataFromAdapter = false;

    private int childWidth, childHeight;
    private int divideSpace = DEFAULT_DIVIDE_SPACE;
    private int placeholder = DEFAULT_PLACE_HOLDER;
    private int mType = TYPE_QQSpace;
    private int mCount;
    private FRMultiImageAdapter mFRMultiImageAdapter;
    private List<String> mData;
    private int childCount;

    public FRMultiImageLayout(Context context) {
        super(context, null);
    }

    public FRMultiImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FRMultiImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FRMultiImageLayout);
            divideSpace = (int) typedArray.getDimension(R.styleable.FRMultiImageLayout_fr_mil_divideSpace, DEFAULT_DIVIDE_SPACE);
            placeholder = typedArray.getResourceId(R.styleable.FRMultiImageLayout_fr_mil_placeholder, DEFAULT_PLACE_HOLDER);
            mType = typedArray.getInt(R.styleable.FRMultiImageLayout_fr_mil_type, TYPE_QQSpace);
            typedArray.recycle();
        }

        if (TYPE_QQSpace == mType) {
            mCount = 9;
        } else {
            mCount = 3;
        }
    }

    //测量自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        childCount = getChildCount();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;

        if (TYPE_QQSpace == mType) {//QQ空间图片列表
            if (childCount == 1) {
                childWidth = width;
                childHeight = height = width / 2;
            } else if (childCount == 2) {
                childWidth = (width - divideSpace) / 2;
                childHeight = height = childWidth;
            } else if (childCount == 4) {
                childWidth = (width - divideSpace) / 2;
                height = childWidth * 2 + divideSpace;
                childHeight = childWidth;
            } else {
                //九宫格模式
                childWidth = (width - divideSpace * 2) / 3;
                if (childCount < mCount) {
                    if (childCount % 3 == 0) {
                        height = childWidth * childCount / 3 + divideSpace * (childCount / 3 - 1);
                    } else {
                        height = childWidth * (childCount / 3 + 1) + divideSpace * (childCount / 3);
                    }
                } else {
                    height = width;
                }
                childHeight = childWidth;
            }
        } else {//百度贴吧图片列表
            if (childCount == 1) {
                childWidth = width;
                childHeight = height = width / 2;
            } else {
                childWidth = (width - divideSpace * 2) / 3;
                childHeight = height = childWidth;
            }
        }

        /**
         * 全所有的child都用AT_MOST模式，而child的width和height仅仅只是建议
         */
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (TYPE_QQSpace == mType) {//QQ空间图片列表
            if (childCount == 1) {
                getChildAt(0).layout(
                        0,
                        0,
                        childWidth,
                        childHeight
                );
            } else if (childCount == 2) {
                getChildAt(0).layout(
                        0,
                        0,
                        childWidth,
                        childHeight
                );
                getChildAt(1).layout(
                        childWidth + divideSpace,
                        0,
                        childWidth * 2 + divideSpace,
                        childHeight
                );
            } else if (childCount == 4) {
                for (int i = 0; i < 4; i++) {
                    getChildAt(i).layout(
                            childWidth * (i % 2) + divideSpace * (i % 2),
                            childHeight * (i / 2) + divideSpace * (i / 2),
                            childWidth * (i % 2 + 1) + divideSpace * (i % 2),
                            childHeight * (i / 2 + 1) + divideSpace * (i / 2)
                    );
                }
            } else {
                //九宫格模式
                for (int i = 0; i < (childCount <= mCount ? childCount : mCount); i++) {
                    getChildAt(i).layout(
                            childWidth * (i % 3) + divideSpace * (i % 3),
                            childHeight * (i / 3) + divideSpace * (i / 3),
                            childWidth * (i % 3 + 1) + divideSpace * (i % 3),
                            childHeight * (i / 3 + 1) + divideSpace * (i / 3)
                    );
                }
                if (childCount > mCount) {
                    getChildAt(mCount).layout(
                            childWidth * 2 + divideSpace * 2,
                            childHeight * 2 + divideSpace * 2,
                            childWidth * 3 + divideSpace * 2,
                            childHeight * 3 + divideSpace * 2
                    );
                }
            }
        } else {//百度贴吧图片列表
            if (childCount == 1) {
                getChildAt(0).layout(
                        0,
                        0,
                        childWidth,
                        childHeight
                );
            } else {
                for (int i = 0; i < (childCount <= mCount ? childCount : mCount); i++) {
                    getChildAt(i).layout(
                            childWidth * (i % 3) + divideSpace * (i % 3),
                            childHeight * (i / 3) + divideSpace * (i / 3),
                            childWidth * (i % 3 + 1) + divideSpace * (i % 3),
                            childHeight * (i / 3 + 1) + divideSpace * (i / 3)
                    );
                }
                if (childCount > mCount) {
                    getChildAt(mCount).layout(
                            childWidth * 2 + divideSpace * 2,
                            0,
                            childWidth * 3 + divideSpace * 2,
                            childHeight
                    );
                }
            }
        }
    }

    /**
     * 设置adapter，同时设置注册MessageNotify
     */
    public void setAdapter(FRMultiImageAdapter mFRMultiImageAdapter) {
        isDataFromAdapter = true;
        this.mFRMultiImageAdapter = mFRMultiImageAdapter;
        addViews();
        mFRMultiImageAdapter.attachView(this);
    }

    /**
     * 添加adapter中所有的view
     */
    public void addViews() {
        if (isDataFromAdapter) {
            if (mFRMultiImageAdapter.getCount() > mCount) {
                for (int i = 0; i < mCount; i++) {
                    configView(i);
                }
                addOverNumView(mCount);
            } else {
                for (int i = 0; i < mFRMultiImageAdapter.getCount(); i++) {
                    configView(i);
                }
            }
        } else {
            setImages(mData);
        }
    }

    public void configView(final int position) {
        View item;
        addView(item = mFRMultiImageAdapter.getView(this, position));
        mFRMultiImageAdapter.setData(mFRMultiImageAdapter.getDatas().get(position));
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFRMultiImageAdapter.setOnItemClick(mFRMultiImageAdapter.getDatas(), position);
            }
        });
    }

    public void addView(int position) {
        if (isDataFromAdapter) {
            if (position > mCount - 1) {
                addOverNumView(mCount);
                return;
            }
            addView(mFRMultiImageAdapter.getView(this, position));
            mFRMultiImageAdapter.setData(mFRMultiImageAdapter.getDatas().get(position));
        }
    }

    /**
     * 同上
     */
    public void setImages(List<String> data) {
        isDataFromAdapter = false;
        this.mData = data;
        removeAllViews();
        if (data.size() > mCount) {
            for (int i = 0; i < mCount; i++) {
                addView(getImageView(data.get(i), i));
            }
            addOverNumView(mCount);
        } else {
            for (int i = 0; i < data.size(); i++) {
                addView(getImageView(data.get(i), i));
            }
        }
    }

    /**
     * 当数据是死数据时：推荐使用此方法
     */
    public void setImages(String[] data) {
        setImages(Arrays.asList(data));
    }

    /**
     * 设置最后一个view
     */
    public void addOverNumView(int position) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        textView.setTextSize(24);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.parseColor("#33000000"));
        textView.setGravity(Gravity.CENTER);
        if (isDataFromAdapter) {
            textView.setText("+" + (mFRMultiImageAdapter.getCount() - mCount));
        } else
            textView.setText("+" + (mData.size() - mCount));

        addView(textView, position);
    }

    /**
     * 获取一个ImageView
     */
    public FRGlideImageView getImageView(String url, final int position) {
        FRGlideImageView itemView = new FRGlideImageView(getContext());
        itemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        itemView.loadImage(url, placeholder);
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //item点击
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(mData, itemView, position);
                }
            }
        });
        return itemView;
    }

    public interface OnItemClickListener {
        void onClick(List<String> mDatas, ImageView itemView, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public int getCount() {
        return mCount;
    }
}
