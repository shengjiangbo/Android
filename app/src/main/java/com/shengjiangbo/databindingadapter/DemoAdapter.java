package com.shengjiangbo.databindingadapter;

import android.util.Log;

import androidx.databinding.ViewDataBinding;

import com.shengjiangbo.databindingadapter.databinding.Item1Binding;
import com.shengjiangbo.databindingadapter.databinding.ItemBinding;
import com.shengjiangbo.databingdingadapter.BaseBindBean;
import com.shengjiangbo.databingdingadapter.BaseBindAdapter;
import com.shengjiangbo.databingdingadapter.BaseBindHolder;

/**
 * Created by 品智.
 * User: 波
 * Date: 2020/6/16
 * Time: 11:05
 */
public class DemoAdapter extends BaseBindAdapter {


    public DemoAdapter() {
        addItemType(0, R.layout.item, BR.data);
        addItemType(1, R.layout.item1, BR.data);
    }

    @Override
    protected void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item) {
        if (binding instanceof ItemBinding) {
            ItemBinding itemBinding = (ItemBinding) binding;
            DemoBean bean = (DemoBean) item;
            Log.e("", "convert: " + bean.getItemType());
            holder.addOnLongClickListener(R.id.msg);
            holder.addOnClickListener(R.id.msg);
        }
        if (binding instanceof Item1Binding) {
            Item1Binding item1Binding = (Item1Binding) binding;
            Demo1Bean bean = (Demo1Bean) item;
            Log.e("", "convert: " + bean.getItemType());
            holder.addOnLongClickListener(R.id.img);
            holder.addOnClickListener(R.id.img);
        }
    }
}
