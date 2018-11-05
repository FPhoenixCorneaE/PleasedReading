package com.wkz.framework.widgets.recycleradapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wkz.framework.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class FRBaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int TYPE_COMMON_VIEW = 100001;//普通类型 Item
    private static final int TYPE_FOOTER_VIEW = 100002;//footer类型 Item
    private static final int TYPE_LOADING_VIEW = 100003;//empty view，即初始化加载时的提示View
    private static final int TYPE_NODATA_VIEW = 100004;//初次加载无数据的默认空白view
    private static final int TYPE_RELOAD_VIEW = 100005;//初次加载无数据的可重新加载或提示用户的view
    private static final int TYPE_BASE_HEADER_VIEW = 200000;

    @IntDef({
            Status.LOADING,
            Status.LOADING_END,
            Status.EMPTY,
            Status.RELOAD,
            Status.LOAD_MORE,
            Status.LOAD_FAILED,
            Status.LOAD_END,
            Status.RESET
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
        int LOADING = 0;//预加载
        int LOADING_END = 1;//预加载结束
        int EMPTY = 2;//无数据
        int RELOAD = 3;//预加载失败
        int LOAD_MORE = 4;//加载更多
        int LOAD_FAILED = 5;//加载更多失败
        int LOAD_END = 6;//加载结束,没有更多数据
        int RESET = 7;//重置
    }

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    private OnLoadMoreListener mLoadMoreListener;

    Context mContext;
    private List<T> mDatas;
    private boolean isOpenLoadMore;//是否开启加载更多
    private boolean isAutoLoadMore;//是否自动加载，当数据不满一屏幕会自动加载
    private boolean isAutoLoadMoreEnd;//自动加载更多是否已经结束

    private View mLoadMoreView; //分页加载更多view
    private View mLoadFailedView; //分页加载失败view
    private View mLoadEndView; //分页加载结束view
    private View mLoadingView; //首次预加载view
    private View mEmptyView; //无数据的view
    private View mReloadView; //首次预加载失败的view
    private RelativeLayout mFooterLayout;//FooterView

    @Status
    private int mStatus;//当前状态
    private boolean showHeaderView;//是否显示HeaderView
    private int mPageNum = 10;

    protected abstract int getViewType(int position, T data);

    FRBaseRecyclerAdapter(Context context) {
        this(context, null, true);
    }

    FRBaseRecyclerAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        this.mContext = context;
        this.isOpenLoadMore = isOpenLoadMore;
        if (datas == null) {
            this.mDatas = new ArrayList<>();
            this.mStatus = Status.LOADING;
        } else {
            this.mDatas = datas;
            this.mStatus = Status.LOADING_END;
        }
    }

    @NonNull
    @Override
    public FRRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mLoadMoreView == null) {
            mLoadMoreView = inflateLayout(mContext, R.layout.fr_layout_recycler_footer_loadmore, null);
        }
        if (mLoadFailedView == null) {
            mLoadFailedView = inflateLayout(mContext, R.layout.fr_layout_recycler_footer_loadfailed, null);
        }
        if (mLoadEndView == null) {
            mLoadEndView = inflateLayout(mContext, R.layout.fr_layout_recycler_footer_loadend, null);
        }
        if (mLoadingView == null) {
            mLoadingView = inflateLayout(mContext, R.layout.fr_layout_recycler_loading, parent);
        }
        if (mEmptyView == null) {
            mEmptyView = inflateLayout(mContext, R.layout.fr_layout_recycler_empty, parent);
        }
        if (mReloadView == null) {
            mReloadView = inflateLayout(mContext, R.layout.fr_layout_recycler_reload, parent);
        }

        if (showHeaderView && mHeaderViews.get(viewType) != null) {
            return FRRecyclerViewHolder.create(mHeaderViews.get(viewType));
        }

        FRRecyclerViewHolder viewHolder;
        switch (viewType) {
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = FRRecyclerViewHolder.create(mFooterLayout);
                break;
            case TYPE_LOADING_VIEW:
                viewHolder = FRRecyclerViewHolder.create(mLoadingView);
                break;
            case TYPE_NODATA_VIEW:
                viewHolder = FRRecyclerViewHolder.create(mEmptyView);
                break;
            case TYPE_RELOAD_VIEW:
                viewHolder = FRRecyclerViewHolder.create(mReloadView);
                break;
            default:
                viewHolder = FRRecyclerViewHolder.create(new View(mContext));
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (mDatas.isEmpty()) {
            if ((mStatus == Status.LOADING && mLoadingView != null) ||
                    (mStatus == Status.RELOAD && mReloadView != null)) {
                return 1;
            } else if ((mStatus == Status.EMPTY && mEmptyView != null)) {
                return showHeaderView ? 1 + getHeaderCount() : 1;
            }
        }
        return mDatas.size() + getHeaderCount() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty()) {
            if (mStatus == Status.LOADING && mLoadingView != null) {
                return TYPE_LOADING_VIEW;
            }

            if (mStatus == Status.RELOAD && mReloadView != null) {
                return TYPE_RELOAD_VIEW;
            }

            if (showHeaderView && isHeaderView(position)) {
                return mHeaderViews.keyAt(position);
            }

            return TYPE_NODATA_VIEW;
        }

        if (showHeaderView && isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        }

        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }


        return getViewType(position - getHeaderCount(), mDatas.get(position - getHeaderCount()));
    }

    /**
     * 根据position得到data
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        if (mDatas.isEmpty()) {
            return null;
        }
        return mDatas.get(position);
    }

    /**
     * 是否是FooterView
     *
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return isOpenLoadMore && position >= getItemCount() - 1;
    }

    protected boolean isCommonItemView(int viewType) {
        return viewType != TYPE_LOADING_VIEW &&
                viewType != TYPE_FOOTER_VIEW &&
                viewType != TYPE_NODATA_VIEW &&
                viewType != TYPE_RELOAD_VIEW &&
                !(viewType >= TYPE_BASE_HEADER_VIEW);
    }

    private boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    protected int getHeaderCount() {
        if (!showHeaderView) {
            return 0;
        }
        return mHeaderViews.size();
    }

    public FRBaseRecyclerAdapter<T> isShowHeaderView(boolean showHeaderView) {
        this.showHeaderView = showHeaderView;
        return this;
    }

    /**
     * 添加HeaderView
     *
     * @param view
     */
    public FRBaseRecyclerAdapter<T> addHeaderView(View view) {
        if (view != null) {
            showHeaderView = true;
            mHeaderViews.put(TYPE_BASE_HEADER_VIEW + getHeaderCount(), view);
        }
        return this;
    }

    /**
     * StaggeredGridLayoutManager模式时，HeaderView、FooterView可占据一行
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFooterView(position) || isHeaderView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * GridLayoutManager模式时， HeaderView、FooterView可占据一行，判断RecyclerView是否到达底部
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position) || isHeaderView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        startLoadMore(recyclerView, layoutManager);
    }


    /**
     * 判断列表是否滑动到底部
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!isOpenLoadMore || mLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isAutoLoadMoreEnd && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    if (mStatus == Status.LOADING ||
                            mStatus == Status.EMPTY ||
                            mStatus == Status.RELOAD ||
                            (mHeaderViews.size() > 0 && showHeaderView && mDatas.isEmpty())
                            ) {
                        return;
                    }
                    if (isAutoLoadMore && !isAutoLoadMoreEnd) {
                        scrollLoadMore();
                    } else if (!isAutoLoadMoreEnd) {
                        loadEnd();
                        isAutoLoadMoreEnd = true;
                    }
                } else {
                    isAutoLoadMoreEnd = true;
                }
            }
        });
    }

    /**
     * 到达底部开始刷新
     */
    private void scrollLoadMore() {
        if (mStatus == Status.RESET) {
            return;
        }

        if (mFooterLayout.getChildAt(0) == mLoadMoreView && mStatus != Status.LOADING) {
            if (mLoadMoreListener != null) {
                mStatus = Status.LOADING;
                mLoadMoreListener.onLoadMore(false);
            }
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    /**
     * StaggeredGridLayoutManager时，查找position最大的列
     *
     * @param lastVisiblePositions
     * @return
     */
    private int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 清空footer view
     */
    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    /**
     * 添加新的footer view
     *
     * @param footerView
     */
    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    /**
     * 获得列表数据个数
     *
     * @return
     */
    public int getDataCount() {
        return mDatas.size();
    }

    public List<T> getAllData() {
        return mDatas;
    }

    public int getPageNum() {
        return mPageNum;
    }

    public FRBaseRecyclerAdapter<T> setPageNum(int pageNum) {
        this.mPageNum = pageNum;
        return this;
    }

    /**
     * 刷新加载更多的数据
     *
     * @param datas
     */
    public FRBaseRecyclerAdapter<T> setLoadMoreData(List<T> datas) {
        if (datas != null && datas.size() == mPageNum) {
            mStatus = Status.LOADING_END;
            insert(datas, mDatas.size());
        } else {
            if (mDatas.isEmpty() && (datas == null || datas.isEmpty())) {
                showEmpty();
            } else {
                mStatus = Status.LOAD_END;
                insert(datas, mDatas.size());
                loadEnd();
            }
        }
        return this;
    }

    /**
     * 初次加载、或下拉刷新要替换全部旧数据时刷新数据
     *
     * @param datas
     */
    public FRBaseRecyclerAdapter<T> setNewData(List<T> datas) {
        if (datas != null && datas.size() == mPageNum) {
            mStatus = Status.LOADING_END;
            mDatas.clear();
            insert(datas, mDatas.size());
        } else {
            mDatas.clear();
            if (datas == null || datas.isEmpty()) {
                showEmpty();
            } else {
                mStatus = Status.LOAD_END;
                insert(datas, mDatas.size());
                loadEnd();
            }
        }
        return this;
    }

    /**
     * 删除某个位置的数据
     *
     * @param position
     */
    public FRBaseRecyclerAdapter<T> remove(int position) {
        if (position < mDatas.size() && position >= 0) {
            mDatas.remove(position);
            notifyItemRemoved(position + getHeaderCount());
            if (position != mDatas.size()) {
                notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
            } else {
                showEmpty();
            }
        }
        return this;
    }

    /**
     * 从某个位置开始添加若干个数据
     *
     * @param datas
     * @param position
     */
    public FRBaseRecyclerAdapter<T> insert(List<T> datas, int position) {
        if (datas != null) {
            mDatas.addAll(position, datas);
            if (position < mDatas.size() && position > 0) {
                notifyItemRangeInserted(position + getHeaderCount(), datas.size());
                notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
            } else {
                showEmpty();
            }
        } else {
            notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 给列表末尾追加多个数据
     *
     * @param datas
     */
    public FRBaseRecyclerAdapter<T> insert(List<T> datas) {
        insert(datas, mDatas.size());
        return this;
    }

    /**
     * 添加单个数据到指定位置
     *
     * @param data
     * @param position
     */
    public FRBaseRecyclerAdapter<T> insert(T data, int position) {
        if (data != null) {
            ArrayList<T> datas = new ArrayList<>();
            datas.add(data);
            insert(datas, position);
        }
        return this;
    }

    /**
     * 给列表末尾追加单个数据
     *
     * @param data
     */
    public FRBaseRecyclerAdapter<T> insert(T data) {
        insert(data, mDatas.size());
        return this;
    }


    /**
     * 更新某个位置的数据
     *
     * @param position
     */
    public FRBaseRecyclerAdapter<T> change(int position) {
        notifyItemChanged(position);
        return this;
    }

    /**
     * 初始化加载中布局
     *
     * @param loadMoreView
     */
    public FRBaseRecyclerAdapter<T> setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        addFooterView(mLoadMoreView);
        return this;
    }

    public FRBaseRecyclerAdapter<T> setLoadMoreView(@LayoutRes int loadMoreViewId) {
        setLoadMoreView(inflateLayout(mContext, loadMoreViewId, null));
        return this;
    }

    /**
     * 初始加载失败布局
     *
     * @param loadFailedView
     */
    public FRBaseRecyclerAdapter<T> setLoadFailedView(View loadFailedView) {
        mLoadFailedView = loadFailedView;
        return this;
    }

    public FRBaseRecyclerAdapter<T> setLoadFailedView(@LayoutRes int loadFailedViewId) {
        setLoadFailedView(inflateLayout(mContext, loadFailedViewId, null));
        return this;
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public FRBaseRecyclerAdapter<T> setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        return this;
    }

    public FRBaseRecyclerAdapter<T> setLoadEndView(@LayoutRes int loadEndViewId) {
        setLoadEndView(inflateLayout(mContext, loadEndViewId, null));
        return this;
    }

    /**
     * 初始化loadingView
     *
     * @param loadingView
     */
    public FRBaseRecyclerAdapter<T> setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        return this;
    }

    public FRBaseRecyclerAdapter<T> setLoadingView(@LayoutRes int loadingViewId, ViewGroup parent) {
        setLoadingView(inflateLayout(mContext, loadingViewId, parent));
        return this;
    }

    /**
     * 移除loadingView
     */
    public FRBaseRecyclerAdapter<T> removeLoadingView() {
        mLoadingView = null;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 初始化emptyView
     *
     * @param emptyView
     */
    public FRBaseRecyclerAdapter<T> setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        return this;
    }

    public FRBaseRecyclerAdapter<T> setEmptyView(@LayoutRes int emptyViewId, ViewGroup parent) {
        setEmptyView(inflateLayout(mContext, emptyViewId, parent));
        return this;
    }

    /**
     * 移除emptyView
     */
    public FRBaseRecyclerAdapter<T> removeEmptyView() {
        mEmptyView = null;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 初次预加载失败、或无数据可显示该view，进行重新加载或提示用户无数据
     *
     * @param reloadView
     */
    public FRBaseRecyclerAdapter<T> setReloadView(View reloadView) {
        mReloadView = reloadView;
        return this;
    }

    public FRBaseRecyclerAdapter<T> setReloadView(@LayoutRes int reloadViewId, ViewGroup parent) {
        setReloadView(inflateLayout(mContext, reloadViewId, parent));
        return this;
    }

    /**
     * 移除reloadView
     */
    public FRBaseRecyclerAdapter<T> removeReloadView() {
        mReloadView = null;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 返回 footer view数量
     *
     * @return
     */
    private int getFooterViewCount() {
        return isOpenLoadMore && !mDatas.isEmpty() ? 1 : 0;
    }

    public FRBaseRecyclerAdapter<T> setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        return this;
    }

    /**
     * 重置adapter，恢复到初始状态
     */
    public FRBaseRecyclerAdapter<T> reset() {
        if (mLoadMoreView != null) {
            addFooterView(mLoadMoreView);
        }
        mStatus = Status.RESET;
        isAutoLoadMoreEnd = false;
        mDatas.clear();
        return this;
    }

    public FRBaseRecyclerAdapter<T> showLoading() {
        mStatus = Status.LOADING;
        notifyDataSetChanged();
        return this;
    }

    public FRBaseRecyclerAdapter<T> showEmpty() {
        mStatus = Status.EMPTY;
        notifyDataSetChanged();
        return this;
    }

    public FRBaseRecyclerAdapter<T> showReload() {
        mStatus = Status.RELOAD;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 数据加载完成
     */
    public FRBaseRecyclerAdapter<T> loadEnd() {
        mStatus = Status.LOAD_END;
        if (mLoadEndView != null) {
            addFooterView(mLoadEndView);
        } else {
            addFooterView(new View(mContext));
        }
        return this;
    }

    /**
     * 数据加载失败
     */
    public FRBaseRecyclerAdapter<T> loadFailed() {
        mStatus = Status.LOAD_FAILED;
        if (mLoadFailedView != null) {
            addFooterView(mLoadFailedView);
            mLoadFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFooterView(mLoadMoreView);
                    if (mLoadMoreListener != null) {
                        mLoadMoreListener.onLoadMore(true);
                    }
                }
            });
        } else {
            addFooterView(new View(mContext));
        }
        return this;
    }

    /**
     * 开启初次数据不满一屏自动加载更多
     */
    public FRBaseRecyclerAdapter<T> openAutoLoadMore() {
        this.isAutoLoadMore = true;
        return this;
    }

    public FRBaseRecyclerAdapter<T> clearLoadView() {
        addFooterView(new View(mContext));
        return this;
    }

    private View inflateLayout(Context context, int layoutId, ViewGroup parent) {
        if (layoutId <= 0) {
            return null;
        }
        if (parent != null) {
            return LayoutInflater.from(context).inflate(layoutId, parent, false);
        }
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public interface OnLoadMoreListener {
        /**
         * 加载更多的回调方法
         *
         * @param isReload 是否是重新加载，只有加载失败后，点击重新加载时为true
         */
        void onLoadMore(boolean isReload);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(FRRecyclerViewHolder FRRecyclerViewHolder, T data, int position);
    }

    public interface OnItemChildClickListener<T> {
        void onItemChildClick(FRRecyclerViewHolder FRRecyclerViewHolder, T data, int position);
    }

    public interface OnMultiItemClickListener<T> {
        void onMultiItemClick(FRRecyclerViewHolder FRRecyclerViewHolder, T data, int position, int viewType);
    }
}
