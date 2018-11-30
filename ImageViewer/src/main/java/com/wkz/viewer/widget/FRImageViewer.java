package com.wkz.viewer.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wkz.viewer.IImageLoader;
import com.wkz.viewer.FRImageViewerState;
import com.wkz.viewer.FRViewData;
import com.wkz.viewer.listener.OnImageChangedListener;
import com.wkz.viewer.listener.OnItemClickListener;
import com.wkz.viewer.listener.OnItemLongClickListener;
import com.wkz.viewer.listener.OnPreviewStatusListener;

import java.util.List;


public class FRImageViewer extends FrameLayout implements IImageViewer {
    private FRImageViewerAttacher mAttacher;

    public FRImageViewer(@NonNull Context context) {
        super(context);
        init(null);
    }

    public FRImageViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FRImageViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mAttacher = new FRImageViewerAttacher(this, attrs);
    }

    @Override
    public TextView getIndexView() {
        return mAttacher.getIndexView();
    }

    @Override
    public FRImageViewer setStartPosition(int position) {
        mAttacher.setStartPosition(position);
        return this;
    }

    @Override
    public FRImageViewer setImageData(List list) {
        mAttacher.setImageData(list);
        return this;
    }

    @Override
    public FRImageViewer setViewData(List<FRViewData> list) {
        mAttacher.setViewData(list);
        return this;
    }

    @Override
    public FRImageViewer setImageLoader(IImageLoader loader) {
        mAttacher.setImageLoader(loader);
        return this;
    }

    @Override
    public FRImageViewer showIndex(boolean show) {
        mAttacher.showIndex(show);
        return this;
    }

    @Override
    public FRImageViewer doDrag(boolean isDo) {
        mAttacher.doDrag(isDo);
        return this;
    }

    @Override
    public FRImageViewer setDragType(int type) {
        mAttacher.setDragType(type);
        return this;
    }

    @Override
    public FRImageViewer doEnterAnim(boolean isDo) {
        mAttacher.doEnterAnim(isDo);
        return this;
    }

    @Override
    public FRImageViewer doExitAnim(boolean isDo) {
        mAttacher.doExitAnim(isDo);
        return this;
    }

    @Override
    public FRImageViewer setDuration(int duration) {
        mAttacher.setDuration(duration);
        return this;
    }

    @Override
    public FRImageViewer setOnImageChangedListener(OnImageChangedListener listener) {
        mAttacher.setOnImageChangedListener(listener);
        return this;
    }

    @Override
    public FRImageViewer setOnItemClickListener(OnItemClickListener listener) {
        mAttacher.setOnViewClickListener(listener);
        return this;
    }

    @Override
    public FRImageViewer setOnItemLongClickListener(OnItemLongClickListener listener) {
        mAttacher.setOnItemLongClickListener(listener);
        return this;
    }

    @Override
    public FRImageViewer setOnPreviewStatusListener(OnPreviewStatusListener listener) {
        mAttacher.setOnPreviewStatusListener(listener);
        return this;
    }

    @Override
    public void watch() {
        mAttacher.watch();
    }

    @Override
    public void close() {
        mAttacher.close();
    }

    @Override
    public void clear() {
        mAttacher.clear();
    }

    @Override
    public int getViewState() {
        return mAttacher.getViewState();
    }

    @Override
    public FRImageViewer setImageScaleable(boolean scaleable) {
        mAttacher.setImageScaleable(scaleable);
        return this;
    }

    @Override
    public boolean isImageScaleable() {
        return mAttacher.isImageScaleable();
    }

    @Override
    public float getImageScale() {
        return mAttacher.getImageScale();
    }

    @Override
    public FRImageViewer setImageMaxScale(float maxScaleLevel) {
        mAttacher.setImageMaxScale(maxScaleLevel);
        return this;
    }

    @Override
    public float getImageMaxScale() {
        return mAttacher.getImageMaxScale();
    }

    @Override
    public FRImageViewer setImageMinScale(float minScaleLevel) {
        mAttacher.setImageMinScale(minScaleLevel);
        return this;
    }

    @Override
    public float getImageMinScale() {
        return mAttacher.getImageMinScale();
    }

    @Override
    public int getCurrentPosition() {
        return mAttacher.getCurrentPosition();
    }

    @Override
    public View getCurrentView() {
        return mAttacher.getCurrentView();
    }

    /**
     * 如果本方法未执行，则是因为图片浏览器为获取到焦点，可在外部手动获取焦点
     * 建议在外部手动调动本方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mAttacher.isImageAnimRunning()) {
                if (getViewState() == FRImageViewerState.STATE_WATCHING) {
                    close();
                    // 消费返回键点击事件，不传递出去
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }
}
