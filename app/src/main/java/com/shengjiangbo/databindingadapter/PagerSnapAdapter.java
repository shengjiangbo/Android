package com.shengjiangbo.databindingadapter;

import androidx.databinding.ViewDataBinding;

import com.shengjiangbo.databindingadapter.databinding.PagerItemBinding;
import com.shengjiangbo.databingdingadapter.BaseBindBean;
import com.shengjiangbo.databingdingadapter.BaseBindHolder;
import com.shengjiangbo.databingdingadapter.BaseBindingAdapter;

public class PagerSnapAdapter extends BaseBindingAdapter {
    public PagerSnapAdapter() {
        addItemType(0, R.layout.pager_item, BR.data);
    }

    @Override
    protected void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item) {
        if (binding instanceof PagerItemBinding) {
            PagerItemBinding itemBinding = (PagerItemBinding) binding;
            itemBinding.tv.setText("item:" + holder.getLayoutPosition());
        }
    }
}
