package com.shengjiangbo.databingdingadapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by 品智.
 * User: 波
 * Date: 2020/6/11
 * Time: 11:52
 */
public abstract class BaseBindingAdapter extends RecyclerView.Adapter<BaseBindHolder> {

    private List<BaseBindBean> mData = new ArrayList<>();
    private SparseIntArray layouts = new SparseIntArray();
    private SparseIntArray BRs = new SparseIntArray();
    private static final int TYPE_NOT_FOUND = -404;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView mRecyclerView;
    private final int LOAD_MORE_TYPE = -1;
    // 当前加载状态，默认为加载完成
    private int loadState = 1000;
    // 正在加载
    public final int LOADING = 1001;
    // 加载完成
    public final int LOADING_COMPLETE = 1002;
    // 加载到底
    public final int LOADING_END = 1003;
    private OnRequestLoadMoreListener mListener;
    private LoadMoreView mLoadMoreView;
    private boolean mLoading;
    private boolean mOpenAnimationEnable;

    /**
     * 加载多布局或者单布局 多布局就重复调用该方法
     *
     * @param type           数据Bean 实现 BindingAdapterType
     * @param layoutResId    布局id
     * @param bindVariableId DataBinding BR
     */
    protected BaseBindingAdapter addItemType(@IntRange(from = 0) int type, @LayoutRes int layoutResId, int bindVariableId) {
//        if (type == LOAD_MORE_TYPE && mListener != null)
//            throw new NullPointerException("不能使用当前type,请换一个type值");
        layouts.put(type, layoutResId);
        BRs.put(type, bindVariableId);
        return this;
    }


    /**
     * 设置下拉视图
     *
     * @param loadingView 加载视图
     */
    public void setLoadMoreView(LoadMoreView loadingView) {
        layouts.put(LOAD_MORE_TYPE, loadingView.getLayoutId());
        BRs.put(LOAD_MORE_TYPE, loadingView.getBindVariableId());
        this.mLoadMoreView = loadingView;
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
                    if (holder.getLayoutPosition() == mData.size() && !mLoading && mListener != null && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
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
     */
    public void setOnLoadMoreListener(OnRequestLoadMoreListener onRequestLoadMoreListener, RecyclerView recyclerView) {
        mListener = onRequestLoadMoreListener;
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
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

    private boolean isFullScreen(LinearLayoutManager layoutManager) {
        return (layoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount() ||
                layoutManager.findFirstCompletelyVisibleItemPosition() != 0;
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

    /**
     * 设置失败点击 加载更多
     */
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
        mLoadMoreView.setLoadMoreEndGone(false);
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
        notifyItemRemoved(getLoadMoreViewPosition());
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
        void onItemChildClick(BaseBindingAdapter adapter, ViewDataBinding binding, View view, int position);
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
        boolean onItemChildLongClick(BaseBindingAdapter adapter, ViewDataBinding binding, View view, int position);
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
        int type = holder.getItemViewType();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (type == LOAD_MORE_TYPE) {
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
            if (mCustomAnimation == null) {
                mCustomAnimation = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.2f, 1f);
            }
            startAnim(mCustomAnimation);
        }
    }

    private Animator mCustomAnimation;
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
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(Animator animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    /**
     * 将动画设置为在加载时开始
     *
     * @param anim
     */
    protected void startAnim(Animator anim) {
        anim.setInterpolator(mInterpolator);
        anim.setDuration(mDuration).start();
    }

    /**
     * To close the animation when loading
     */
    public void closeLoadAnimation() {
        this.mOpenAnimationEnable = false;
    }

    private void checkNotNull() {
        if (getRecyclerView() == null) {
            throw new RuntimeException("please bind recyclerView first!");
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
        BaseBindHolder viewHolder = (BaseBindHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.mBinding;
    }
}
