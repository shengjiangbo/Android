package com.shengjiangbo.databindingadapter;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sheng.mvvm.BaseBindActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivityMotionLayoutBinding;
import com.shengjiangbo.databingdingadapter.BaseBindAdapter;
import com.shengjiangbo.databingdingadapter.BaseBindBean;
import com.shengjiangbo.databingdingadapter.BaseBindHolder;

/**
 * 创建人：Bobo
 * 创建时间：2022/1/13 11:27
 * 类描述：
 */
public class MotionLayoutActivity extends BaseBindActivity<ActivityMotionLayoutBinding> {
    @Override
    protected void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Adapter adapter = new Adapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
        adapter.addData(new DemoBean("1"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_motion_layout;
    }

    public class Adapter extends BaseBindAdapter {

        public Adapter() {
            addItemType(R.layout.item, BR.data);
        }

        @Override
        protected void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item) {

        }


    }
}
