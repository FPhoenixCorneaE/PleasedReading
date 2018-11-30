package com.wkz.viewer.widget;


import android.view.View;
import android.widget.TextView;

import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.dragger.FRImageDraggerType;
import com.wkz.viewer.listener.OnImageChangedListener;
import com.wkz.viewer.listener.OnItemClickListener;
import com.wkz.viewer.listener.OnItemLongClickListener;
import com.wkz.viewer.listener.OnPreviewStatusListener;

import java.util.List;


public interface IImageViewer {

    /**
     * 获取图片索引的 view
     */
    TextView getIndexView();

    /**
     * 设置起始位置
     *
     * @param position
     */
    FRImageViewer setStartPosition(int position);

    /**
     * 设置图片资源
     *
     * @param list
     */
    FRImageViewer setImageData(List list);

    /**
     * 设置目标 view 的相关数据
     *
     * @param list
     */
    FRImageViewer setViewData(List<FRViewData> list);

    /**
     * 设置图片加载器
     *
     * @param loader
     */
    FRImageViewer setImageLoader(IImageLoader loader);

    /**
     * 是否显示图片索引
     *
     * @param show
     */
    FRImageViewer showIndex(boolean show);

    /**
     * 是否允许拖拽图片
     *
     * @param isDo
     */
    FRImageViewer doDrag(boolean isDo);

    /**
     * 设置拖拽模式
     *
     * @param type
     */
    FRImageViewer setDragType(@FRImageDraggerType int type);

    /**
     * 是否使用进场动画
     *
     * @param isDo
     */
    FRImageViewer doEnterAnim(boolean isDo);

    /**
     * 是否使用退场动画
     *
     * @param isDo
     */
    FRImageViewer doExitAnim(boolean isDo);

    /**
     * 设置进场与退场动画的执行时间
     *
     * @param duration
     */
    FRImageViewer setDuration(int duration);

    /**
     * 设置图片的切换事件监听
     *
     * @param listener
     */
    FRImageViewer setOnImageChangedListener(OnImageChangedListener listener);

    /**
     * 设置图片的单击事件监听
     *
     * @param listener
     */
    FRImageViewer setOnItemClickListener(OnItemClickListener listener);

    /**
     * 设置图片的长按事件监听
     *
     * @param listener
     */
    FRImageViewer setOnItemLongClickListener(OnItemLongClickListener listener);

    /**
     * 设置图片预览器的预览状态监听
     *
     * @param listener
     */
    FRImageViewer setOnPreviewStatusListener(OnPreviewStatusListener listener);

    /**
     * 打开图片预览器
     */
    void watch();

    /**
     * 关闭图片预览器
     */
    void close();

    /**
     * 清除所有数据
     */
    void clear();

    /**
     * 获取图片预览器的当前状态
     *
     * @return {@link FRImageViewerState}
     */
    @FRImageViewerState
    int getViewState();

    /**
     * 是否允许图片缩放
     *
     * @param scaleable
     */
    FRImageViewer setImageScaleable(boolean scaleable);

    /**
     * 图片是否可缩放
     *
     * @return
     */
    boolean isImageScaleable();

    /**
     * 获取图片当前的缩放等级
     *
     * @return
     */
    float getImageScale();

    /**
     * 设置图片的最大缩放等级
     *
     * @param maxScale
     */
    FRImageViewer setImageMaxScale(float maxScale);

    /**
     * 获取图片的最大缩放等级
     *
     * @return
     */
    float getImageMaxScale();

    /**
     * 设置图片的最小缩放等级
     *
     * @param minScale
     */
    FRImageViewer setImageMinScale(float minScale);

    /**
     * 获取图片的最小缩放等级
     *
     * @return
     */
    float getImageMinScale();

    /**
     * 获取当前的 view
     *
     * @return
     */
    View getCurrentView();

    /**
     * 获取当前的 view 的位置
     *
     * @return
     */
    int getCurrentPosition();
}
