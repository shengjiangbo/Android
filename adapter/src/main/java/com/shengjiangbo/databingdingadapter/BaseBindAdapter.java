package com.shengjiangbo.databingdingadapter;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

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

import com.shengjiangbo.databingdingadapter.animation.AlphaInAnimation;
import com.shengjiangbo.databingdingadapter.animation.BaseAnimation;
import com.shengjiangbo.databingdingadapter.animation.ScaleInAnimation;
import com.shengjiangbo.databingdingadapter.animation.SlideInBottomAnimation;
import com.shengjiangbo.databingdingadapter.animation.SlideInLeftAnimation;
import com.shengjiangbo.databingdingadapter.animation.SlideInRightAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 波.
 * User: 波
 * Date: 2020/6/11
 * Time: 11:52
 */
public abstract class BaseBindAdapter extends RecyclerView.Adapter<BaseBindHolder> {

    protected List<BaseBindBean> mData = new ArrayList<>();
    private SparseIntArray layouts = new SparseIntArray();
    private SparseIntArray BRs = new SparseIntArray();
    private Map<Integer, Integer> headPosition = new HashMap<>();
    private SparseBooleanArray isRow = new SparseBooleanArray();
    private static final int TYPE_NOT_FOUND = -404;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView mRecyclerView;
    protected Context mContext;
    private final int LOAD_MORE_TYPE = -1;
    public OnRequestLoadMoreListener mListener;
    public LoadMoreView mLoadMoreView;
    private boolean mLoading;
    private boolean mNextLoadEnable;


    /**
     * 加载多布局或者单布局 多布局就重复调用该方法
     * <p>
     * 数据Bean 实现 BindingAdapterType BaseBindBean 默认 itemType == 0
     *
     * @param layoutResId    布局id
     * @param bindVariableId DataBinding BR
     */
    protected BaseBindAdapter addItemType(@LayoutRes int layoutResId, int bindVariableId) {
        return addItemType(0, layoutResId, bindVariableId);
    }


    /**
     * 加载多布局或者单布局 多布局就重复调用该方法
     *
     * @param type           数据Bean 实现 BindingAdapterType
     * @param layoutResId    布局id
     * @param bindVariableId DataBinding BR
     */
    protected BaseBindAdapter addItemType(@IntRange(from = 0) int type, @LayoutRes int layoutResId, int bindVariableId) {
        layouts.put(type, layoutResId);
        BRs.put(type, bindVariableId);
        isRow.put(type, false);
        return this;
    }

    /**
     * @param type           数据Bean 实现 BindingAdapterType
     * @param layoutResId    布局id
     * @param bindVariableId DataBinding BR
     * @param position       是直接加载到指定位置的布局 todo 只支持第一页数据指定位置插入 而且不能超过第一页数据size
     * @return
     */
    protected BaseBindAdapter addItemType(@IntRange(from = 0, to = 998) int type, @LayoutRes int layoutResId, int bindVariableId, int position) {
        layouts.put(type, layoutResId);
        BRs.put(type, bindVariableId);
        headPosition.put(position, type);
        isRow.put(type, false);
        return this;
    }

    /**
     * @param type
     * @param layoutResId
     * @param bindVariableId
     * @param position       是直接加载到指定位置的布局 todo 只支持第一页数据指定位置插入 而且不能超过第一页数据size
     * @param isRow          如果是StaggeredGridLayoutManager or GridLayoutManager 要现实一行的话 设置为true
     * @return
     */
    protected BaseBindAdapter addItemType(@IntRange(from = 0, to = 998) int type, @LayoutRes int layoutResId, int bindVariableId, int position, boolean isRow) {
        layouts.put(type, layoutResId);
        BRs.put(type, bindVariableId);
        headPosition.put(position, type);
        this.isRow.put(type, isRow);
        return this;
    }


    /**
     * @param type
     * @param layoutResId
     * @param bindVariableId
     * @param isRow          如果是StaggeredGridLayoutManager or GridLayoutManager 要现实一行的话 设置为true
     * @return
     */
    protected BaseBindAdapter addItemType(@IntRange(from = 0, to = 998) int type, @LayoutRes int layoutResId, int bindVariableId, boolean isRow) {
        layouts.put(type, layoutResId);
        BRs.put(type, bindVariableId);
        this.isRow.put(type, isRow);
        return this;
    }


    /**
     * 设置下拉视图
     *
     * @param loadingView 加载视图  null则移除加载更多
     */
    public void setLoadMoreView(LoadMoreView loadingView) {
        if (loadingView == null) {
            this.mLoadMoreView = null;
            notifyDataSetChanged();
        } else {
            layouts.put(LOAD_MORE_TYPE, loadingView.getLayoutId());
            BRs.put(LOAD_MORE_TYPE, loadingView.getBindVariableId());
            this.mLoadMoreView = loadingView;
        }
    }

    /**
     * 当type没找到时，是否抛出异常
     */
    public boolean isThrowTypeNotFound() {
        return true;
    }

    @NonNull
    @Override
    public BaseBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutResId;
        if (isNoData) {
            layoutResId = mLoadMoreView.getBindNoDataId();
        } else {
            switch (viewType) {
                case LOAD_MORE_TYPE:
                    layoutResId = mLoadMoreView.getLayoutId();
                    break;
                default:
                    layoutResId = layouts.get(viewType, TYPE_NOT_FOUND);
                    break;
            }
        }
        if (layoutResId == TYPE_NOT_FOUND) {
            if (isThrowTypeNotFound()) {
                throw new NullPointerException("type:" + viewType + "未在adapter添加addItemType");
            } else {
                ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_type_not_found, parent, false);
                return new BaseBindHolder(viewDataBinding);
            }
        } else {
            ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
            final BaseBindHolder holder = new BaseBindHolder(viewDataBinding);
            if (viewType == LOAD_MORE_TYPE) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.getLayoutPosition() == mData.size() && mListener != null && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {//
                            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                            mLoadMoreView.convert(holder);
                            notifyItemChanged(getLoadMoreViewPosition());
                            if (!mLoading) {
                                mLoading = true;
                                mListener.onLoadMoreRequested();
                            }
                        }
                    }
                });
            } else {
                bindViewClickListener(holder.mBinding, holder);
            }
            return holder;
        }
    }

    private int isNull = -1;//数据为null

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
        } else if (type == TYPE_NOT_FOUND || type == isNull) {

        } else {
            holder.mBinding.setVariable(BRs.get(type), mData.get(position));
            holder.setAdapter(this);
            convert(holder, holder.mBinding, mData.get(position));
        }

    }


    /**
     * 实现此方法 操作数据绑定
     *
     * @param holder
     * @param binding 当前item所对应的DataBinding
     * @param item    当前item所对应的数据
     */
    protected abstract void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item);

    @Override
    public int getItemViewType(int position) {
        if (isNoData) {
            return TYPE_NOT_FOUND;
        }
        if (mListener != null && mData.size() > 0 && position == getItemCount() - 1) {
            //如果设置了加载更多功能，则最后一个为加载更多的布局
            return LOAD_MORE_TYPE;
        }
        if (mData.get(position) == null) {
            throw new NullPointerException("getItemViewType: adapter ——> mData.get(position) == null");
        } else {
            return mData.get(position).getItemType();
        }
    }

    /**
     * 设置加载更多监听
     *
     * @param onRequestLoadMoreListener
     * @param recyclerView
     */
    public void setOnLoadMoreListener(OnRequestLoadMoreListener onRequestLoadMoreListener, RecyclerView recyclerView) {
        mListener = onRequestLoadMoreListener;
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
    }

    public void setOnLoadMoreListener(OnRequestLoadMoreListener onRequestLoadMoreListener) {
        mListener = onRequestLoadMoreListener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        addOnScrollListener();
    }

    protected void addOnScrollListener() {
        if (mRecyclerView == null) {
            return;
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
//                if (mData.size() > 0 && getItemViewType(lastItemIndex) == LOAD_MORE_TYPE) {
//                }
            }
        });
    }

    public int getTheBiggestNumber(int[] numbers) {
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

    /**
     * 设置新数据
     *
     * @param data
     */
    public void setNewData(Collection<? extends BaseBindBean> data) {
        mData.clear();
        if (data != null && data.size() > 0) {//实现指定item添加指定布局  headPosition
            mData.addAll(data);
            isNoData = false;
            for (Map.Entry<Integer, Integer> entry : headPosition.entrySet()) {
                int position = entry.getKey();
                final int type = entry.getValue();
                if (mData.size() > position) {
                    BaseBindBean bean = new BaseBindBean() {
                        @Override
                        public int getItemType() {
                            return type;
                        }
                    };
                    mData.add(position, bean);
                }
            }
        }
        if (mLoadMoreView != null) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        notifyDataSetChanged();
    }

    private boolean isAutoNextPage = true;

    /**
     * @param isAutoNextPage 根据count 是否自动刷新下一页
     */
    public void isAutoNextPage(boolean isAutoNextPage) {
        this.isAutoNextPage = isAutoNextPage;
    }

    public int setNewData(Collection<? extends BaseBindBean> data, int page, int count) {
        return setNewData(data, page, count, true);
    }


    public int setData(Collection<? extends BaseBindBean> data, int page, int count) {
        if (page == 1) {
            return setNewData(data, page, count, true);
        } else {
            return addData(data, page, count);
        }
    }

    /**
     * 添加数据
     *
     * @param data
     * @param page
     * @param count
     * @param isShowNoData
     * @return
     */
    public int setData(Collection<? extends BaseBindBean> data, int page, int count, boolean isShowNoData) {
        if (page == 1) {
            return setNewData(data, page, count, isShowNoData);
        } else {
            return addData(data, page, count);
        }
    }

    public void setNewData(@NonNull BaseBindBean data) {
        mData.clear();
        mData.add(data);
        notifyDataSetChanged();
    }

    /**
     * @param data
     * @param page         当前上拉下标
     * @param count        每页数量
     * @param isShowNoData 是否显示无数据UI
     * @return
     */
    public int setNewData(Collection<? extends BaseBindBean> data, int page, int count, boolean isShowNoData) {
        mData.clear();
        if (data != null && data.size() > 0) {//实现指定item添加指定布局  headPosition
            mData.addAll(data);
            isNoData = false;
            for (Map.Entry<Integer, Integer> entry : headPosition.entrySet()) {
                int position = entry.getKey();
                final int type = entry.getValue();
                if (mData.size() > position) {
                    BaseBindBean bean = new BaseBindBean() {
                        @Override
                        public int getItemType() {
                            return type;
                        }
                    };
                    mData.add(position, bean);
                }
            }
        }
        if (mLoadMoreView != null) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        if (isAutoNextPage) {
            if (data == null || data.size() == 0) {
                if (isShowNoData) {
                    setNoData();
                } else {
                    loadMoreComplete();
                }
            } else if (data.size() < count) {
                loadMoreEnd();
            } else {
                page += 1;
                loadMoreComplete();
            }
        } else {
            page += 1;
            loadMoreComplete();
        }
        notifyDataSetChanged();
        return page;
    }


    /**
     * @param data
     * @param page  当前上拉下标
     * @param count 每页数量
     * @return
     */
    public int addData(@NonNull Collection<? extends BaseBindBean> data, int page, int count) {
        mData.addAll(data);
        isNoData = false;
        if (isAutoNextPage) {
            if (data.size() == 0) {
                loadMoreEnd();
            } else if (data.size() < count) {
                loadMoreEnd();
            } else {
                page += 1;
                loadMoreComplete();
            }
        } else {
            page += 1;
            loadMoreComplete();
        }
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
        return page;
    }

    /**
     * 获取所有数据 添加了指定位置的布局 需要做好判断
     *
     * @return
     */
    public List<? extends BaseBindBean> getData() {
        return mData;
    }

    public void setData(@IntRange(from = 0) int position, @NonNull BaseBindBean data) {
        isNoData = false;
        mData.add(position, data);
        notifyItemChanged(position);
    }

    /**
     * 替换 数据
     *
     * @param position
     * @param data
     */
    public void replace(@IntRange(from = 0) int position, @NonNull BaseBindBean data) {
        isNoData = false;
        mData.set(position, data);
        notifyItemChanged(position);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull BaseBindBean data) {
        isNoData = false;
        mData.add(position, data);
        notifyItemChanged(position);
    }

    public void addData(@NonNull BaseBindBean data) {
        isNoData = false;
        mData.add(data);
        notifyItemChanged(mData.size());
    }

    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends BaseBindBean> newData) {
        isNoData = false;
        mData.addAll(position, newData);
        notifyItemRangeInserted(position, newData.size());
    }

    public void addData(@NonNull Collection<? extends BaseBindBean> newData) {
        isNoData = false;
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size(), newData.size());
    }

    @Override
    public int getItemCount() {
        if (isNoData) {
            return 1;
        }
        if (mListener != null && mLoadMoreView != null && mData.size() > 0) {
            return mData.size() + 1;
        }
        return mData.size();
    }

    private boolean isNoData;


    public void setNoData() {
        isNoData = true;
        notifyDataSetChanged();
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(position, mData.size() - position);
    }

    /**
     * 如果变动的数据大小和实际数据大小一致，则刷新整个列表
     *
     * @param size 变动的数据大小
     */
    private void compatibilityDataSizeChanged(int size) {
        if (mData.size() == size) {
            notifyDataSetChanged();
        }
    }

    /**
     * @param isLoadMoreEnd true 则不显示LoadMoreEnd false则显示
     */
    public void isLoadMoreEnd(boolean isLoadMoreEnd) {
        if (mLoadMoreView != null) {
            mLoadMoreView.setLoadMoreEndGone(isLoadMoreEnd);
        }
    }

    /**
     * 结束刷新没有更多数据
     */
    public void loadMoreEnd() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        getRecyclerView().post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(getLoadMoreViewPosition());
            }
        });
    }

    private int getLoadMoreViewPosition() {
        return mData.size();
    }

    /**
     * 是否可以上拉，也就是说是否真正最后一条数据了
     */
    public boolean isNextLoadEnable() {
        return mNextLoadEnable;
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        getRecyclerView().post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(getLoadMoreViewPosition());
            }
        });
    }

    /**
     * 获取当前刷新状态
     *
     * @return
     */
    public final int getLoadMoreStatus() {
        if (mLoadMoreView == null) {
            return -1;
        }
        return mLoadMoreView.getLoadMoreStatus();
    }

    /**
     * 刷新失败
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        getRecyclerView().post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(getLoadMoreViewPosition());
            }
        });

    }

    /**
     * 刷新异常
     */
    public void loadMoreError() {
        if (mData.size() == 0) {
            setNoData();
        } else {
            mLoading = false;
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
            getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(getLoadMoreViewPosition());
                }
            });
        }
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
     * 使用PagerSnapHelper的时候 加载更多失败 点击不了 上拉重新加载
     * 不是使用PagerSnapHelper的时候也可以使用
     */
    public void setPagerSnapLoadMore() {
        isPagerSnapLoadMore = true;
    }

    private boolean isPagerSnapLoadMore;

    /**
     * 自动加载更多
     *
     * @param position
     */
    protected synchronized void autoLoadMore(int position) {
        if (getData().size() == 0 || position < getData().size() - 1) {
            return;
        }
        if (mListener == null || mLoadMoreView == null) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL && isPagerSnapLoadMore) {
            if (!mLoading) {
                mLoading = true;
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                        notifyItemChanged(getLoadMoreViewPosition());
                        mListener.onLoadMoreRequested();
                    }
                });

            }
            return;
        }

        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }


        if (!mLoading) {
            mLoading = true;
            getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                    notifyItemChanged(getLoadMoreViewPosition());
                    mListener.onLoadMoreRequested();
                }
            });
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
        void onItemChildClick(BaseBindAdapter adapter, ViewDataBinding binding, View view, int position);
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
        boolean onItemChildLongClick(BaseBindAdapter adapter, ViewDataBinding binding, View view, int position);
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
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
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
        setRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    boolean row = isRow.get(type);
                    return type == TYPE_NOT_FOUND || type == LOAD_MORE_TYPE || row ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    //用于StaggeredGridLayoutManager header footer只显示一行
    @Override
    public void onViewAttachedToWindow(@NonNull BaseBindHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int type = holder.getItemViewType();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean row = isRow.get(type);
            if (type == LOAD_MORE_TYPE || row || type == TYPE_NOT_FOUND) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                // 如果方向是纵向的，视图将充满整个宽度，方向为横向，视图将充满整个高度。
                params.setFullSpan(true);
            }
        }
        addAnimation(holder);
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    private int mLastPosition = -1;
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    /**
     * 设置动画时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * Set the view animation type.
     *
     * @param animationType One of
     */
    public void openLoadAnimation(@IntRange(from = 0, to = 4) int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case 0:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case 1:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case 2:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case 3:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case 4:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    private boolean mOpenAnimationEnable = false;

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    /**
     * To close the animation when loading
     */
    public void closeLoadAnimation() {
        this.mOpenAnimationEnable = false;
    }

    private void checkNotNull() {
        if (getRecyclerView() == null) {
            Log.e("BaseDataBindingAdapter", "适配器RecyclerView == null");
        }
    }

    /**
     * 获取指定位置中的 ViewDataBinding
     *
     * @param position
     * @return
     */
    @Nullable
    public ViewDataBinding getBindViewPosition(int position) {
        checkNotNull();
        return getBindViewPosition(getRecyclerView(), position);
    }

    @Nullable
    public ViewDataBinding getBindViewPosition(RecyclerView recyclerView, int position) {
        if (recyclerView == null) {
            return null;
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager != null) {
            View itemView = manager.findViewByPosition(position);
            if (itemView != null && null != mRecyclerView.getChildViewHolder(itemView)) {
                BaseBindHolder viewHolder = (BaseBindHolder) mRecyclerView.getChildViewHolder(itemView);
                if (viewHolder == null) {
                    return null;
                }
                return viewHolder.mBinding;
            }
        }
        return null;
    }
}
