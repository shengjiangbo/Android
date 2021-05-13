package com.shengjiangbo.databingdingadapter;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 波.
 * User: 波
 * Date: 2020/6/16
 * Time: 11:06
 */
public class BaseBindHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    public ViewDataBinding mBinding;
    private BaseBindAdapter mAdapter;

    public BaseBindHolder(@NonNull ViewDataBinding itemView) {
        super(itemView.getRoot());
        mBinding = itemView;
        views = new SparseArray<>();
    }

    public void setAdapter(BaseBindAdapter adapter) {
        mAdapter = adapter;
    }


    public BaseBindHolder addOnClickListener(@IdRes final int... viewIds) {
        for (int viewId : viewIds) {
            final View view = getView(viewId);
            if (view != null) {
                if (!view.isClickable()) {
                    view.setClickable(true);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAdapter != null && mAdapter.getOnItemChildClickListener() != null) {
                            mAdapter.getOnItemChildClickListener().onItemChildClick(mAdapter, mBinding, v, getLayoutPosition());
                        }
                    }
                });
            }
        }
        return this;
    }

    public BaseBindHolder addOnLongClickListener(@IdRes final int... viewIds) {
        for (int viewId : viewIds) {
            final View view = getView(viewId);
            if (view != null) {
                if (!view.isClickable()) {
                    view.setClickable(true);
                }
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mAdapter != null) {
                            return mAdapter.getOnItemChildLongClickListener() != null &&
                                    mAdapter.getOnItemChildLongClickListener().onItemChildLongClick(mAdapter, mBinding, v, getLayoutPosition());
                        }
                        return false;
                    }

                });
            }
        }
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseBindHolder setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
}
