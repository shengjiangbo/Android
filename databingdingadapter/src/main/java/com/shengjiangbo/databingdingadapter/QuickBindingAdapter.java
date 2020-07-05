package com.shengjiangbo.databingdingadapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 品智.
 * User: 波
 * Date: 2020/6/16
 * Time: 16:41
 */
public class QuickBindingAdapter extends RecyclerView.Adapter<BaseBindHolder> {

    private List<BaseBindBean> mData = new ArrayList<>();
    private List<Integer> mType = new ArrayList<>();
    private SparseIntArray layouts = new SparseIntArray();
    private SparseIntArray BRs = new SparseIntArray();
    private static final int TYPE_NOT_FOUND = -404;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView mRecyclerView;
    private final int LOAD_MORE_TYPE = 999;
    private OnRequestLoadMoreListener mListener;
    private LoadMoreView mLoadMoreView;
    private boolean mLoading;
    private OnQuickConvertListener mOnQuickConvertListener;
    private int[] mIds = new int[]{};
    private int[] mLongIds = new int[]{};

    public static QuickBindingAdapter Create() {
        return new QuickBindingAdapter();
    }

    /**
     * 加载多布局或者单布局 多布局就重复调用该方法
     *
     * @param type           布局type 用于区分布局 getItemViewType
     * @param layoutResId    布局id
     * @param bindVariableId DataBinding BR
     */
    public QuickBindingAdapter bindingItem(@IntRange(from = 0, to = 998) int type, @LayoutRes int layoutResId, int bindVariableId) {
        layouts.put(type, layoutResId);
        mType.add(type);
        BRs.put(type, bindVariableId);
        return this;
    }


    public QuickBindingAdapter addOnClickListener(@IdRes final int... viewIds) {
        mIds = viewIds;
        return this;
    }

    public QuickBindingAdapter addOnLongClickListener(@IdRes final int... viewIds) {
        mLongIds = viewIds;
        return this;
    }


    public interface OnQuickConvertListener {
        void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item);
    }

    /**
     * 如果你只想用databinding来拿控件，其他的逻辑依然写在adapter中，那就实现这个,操作数据绑定
     *
     * @param onQuickConvertListener 用于自定义更多功能
     */
    public QuickBindingAdapter setOnQuickConvertListener(OnQuickConvertListener onQuickConvertListener) {
        mOnQuickConvertListener = onQuickConvertListener;
        return this;
    }

    /**
     * 设置下拉视图
     *
     * @param loadingView 加载视图
     */
    public QuickBindingAdapter setLoadMoreView(LoadMoreView loadingView) {
        layouts.put(LOAD_MORE_TYPE, loadingView.getLayoutId());
        BRs.put(LOAD_MORE_TYPE, loadingView.getBindVariableId());
        mType.add(LOAD_MORE_TYPE);
        this.mLoadMoreView = loadingView;
        return this;
    }

    @NonNull
    @Override
    public BaseBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        switch (viewType) {
            case LOAD_MORE_TYPE:
                layoutResId = mLoadMoreView.getLayoutId();
                break;
            default:
                layoutResId = layouts.get(viewType, TYPE_NOT_FOUND);
                break;
        }
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        BaseBindHolder holder = new BaseBindHolder(viewDataBinding);
        holder.addOnClickListener(mIds);//添加点击事件
        holder.addOnLongClickListener(mLongIds);//添加长按事件
        bindViewClickListener(holder.mBinding, holder);
        return holder;
    }

    /**
     * 绑定 item点击事件 长按事件
     *
     * @param dataBinding
     */
    private void bindViewClickListener(final ViewDataBinding dataBinding, final BaseBindHolder holder) {
        if (dataBinding == null) {
            return;
        }
        View view = dataBinding.getRoot();
        if (getOnItemClickListener() != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getLayoutPosition() == mData.size() && !mLoading && mListener != null && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {//点击加载失败
                        mLoading = true;
                        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                        notifyItemRemoved(getLoadMoreViewPosition());
                        mListener.onLoadMoreRequested();
                        return;
                    }
                    getOnItemClickListener().onItemClick(dataBinding, v, holder.getLayoutPosition());
                }
            });
        }
        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getOnItemLongClickListener().onItemLongClick(dataBinding, v, holder.getLayoutPosition());
                }
            });
        }
    }

    /**
     * 绑定不同类型的持有并解决不同的绑定事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseBindHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == LOAD_MORE_TYPE) {
            mLoadMoreView.convert(holder);
        } else {
            holder.mBinding.setVariable(BRs.get(mData.get(position).getItemType()), mData.get(position));
            holder.setAdapter(this);
            if (mOnQuickConvertListener != null) {
                mOnQuickConvertListener.convert(holder, holder.mBinding, mData.get(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mListener != null && mData.size() > 0 && position == getItemCount() - 1) {
            //如果设置了加载更多功能，则最后一个为加载更多的布局
            return LOAD_MORE_TYPE;
        }

        return mData.get(position).getItemType();
    }

    /**
     * 设置加载更多监听
     *
     * @param onRequestLoadMoreListener
     * @param recyclerView
     * @return
     */
    public QuickBindingAdapter setOnLoadMoreListener(OnRequestLoadMoreListener onRequestLoadMoreListener, RecyclerView recyclerView) {
        mListener = onRequestLoadMoreListener;
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
        return this;
    }

    private RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        addOnScrollListener();
    }

    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRecyclerView.getLayoutManager() == null) return;
                int lastItemIndex = 0;
                if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    if (layoutManager.getChildCount() < 1) return;
                    lastItemIndex = layoutManager.findLastVisibleItemPosition();
                } else if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                    if (layoutManager.getChildCount() < 1) return;
                    lastItemIndex = layoutManager.findLastVisibleItemPosition();
                } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(positions);
                    lastItemIndex = getTheBiggestNumber(positions);
                }
                autoLoadMore(lastItemIndex);
            }
        });
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    public interface OnRequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public void setNewData(Collection<? extends BaseBindBean> data) {
        mData.clear();
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<? extends BaseBindBean> getData() {
        return mData;
    }

    public void setData(@IntRange(from = 0) int position, @NonNull BaseBindBean data) {
        mData.add(position, data);
        notifyItemChanged(position);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull BaseBindBean data) {
        mData.add(position, data);
        notifyItemChanged(position);
    }

    public void addData(@NonNull BaseBindBean data) {
        mData.add(data);
        notifyItemChanged(mData.size());
    }

    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends BaseBindBean> newData) {
        mData.addAll(position, newData);
        notifyItemRangeInserted(position, newData.size());
    }

    public void addData(@NonNull Collection<? extends BaseBindBean> newData) {
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size(), newData.size());
    }

    @Override
    public int getItemCount() {
        if (mListener != null && mData.size() > 0) {
            return mData.size() + 1;
        }
        return mData.size();
    }



    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    private int getLoadMoreViewPosition() {
        return mData.size();
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    private int getLoadMoreViewCount() {
        if (mData.size() == 0 || mLoadMoreView == null) {
            return 0;
        }
        if (mLoadMoreView.getLayoutId() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 自动加载更多
     *
     * @param position
     */
    private void autoLoadMore(int position) {
        if (position < mData.size() - 1) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        notifyItemChanged(getLoadMoreViewPosition());
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onLoadMoreRequested();
                    }
                });
            } else {
                mListener.onLoadMoreRequested();
            }
        }
    }


    /**
     * Interface definition for a callback to be invoked when an itemchild in this
     * view has been clicked
     */
    public interface OnItemChildClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The view whihin the ItemView that was clicked
         * @param position The position of the view int the adapter
         */
        void onItemChildClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an childView in this
     * view has been clicked and held.
     */
    public interface OnItemChildLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The childView whihin the itemView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemChildLongClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemLongClick(ViewDataBinding binding, View v, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView itemView has been clicked.
     */
    public interface OnItemClickListener {

        void onItemClick(ViewDataBinding binding, View v, int position);

    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public QuickBindingAdapter setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     * @return
     */
    public QuickBindingAdapter setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
        return this;
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public QuickBindingAdapter setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public QuickBindingAdapter setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
        return this;
    }


    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been long clicked and held, or null id no callback as been set.
     */
    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been clicked and held, or null id no callback as been set.
     */
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);

                    return type == LOAD_MORE_TYPE ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    //用于StaggeredGridLayoutManager header footer只显示一行
    @Override
    public void onViewAttachedToWindow(@NonNull BaseBindHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (holder.getItemViewType() == LOAD_MORE_TYPE) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                // 如果方向是纵向的，视图将充满整个宽度，方向为横向，视图将充满整个高度。
                params.setFullSpan(true);
            }
        }
    }
}