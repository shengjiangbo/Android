package com.shengjiangbo.databindingadapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shengjiangbo.databindingadapter.databinding.ActivityDemo1Binding;
import com.shengjiangbo.databindingadapter.databinding.Item1Binding;
import com.shengjiangbo.databindingadapter.databinding.ItemBinding;
import com.shengjiangbo.databingdingadapter.BaseBindBean;
import com.shengjiangbo.databingdingadapter.BaseBindHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 波
 * Date: 2020/6/17
 * Time: 11:19
 */
public class Demo1Activity extends AppCompatActivity implements QuickBindingAdapter.OnRequestLoadMoreListener {

    private QuickBindingAdapter mQuickAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDemo1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_demo1, null, false);
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = QuickBindingAdapter.Create()
                .bindingItem(0, R.layout.item, BR.data)//添加布局
                .bindingItem(1, R.layout.item1, BR.data)//添加第二个布局布局
                .setLoadMoreView(new MainLoadMoreView())//设置上拉加载更多布局  继承 LoadMoreView
                .setOnLoadMoreListener(this, binding.recyclerView)//上拉加载更多监听
                .addOnClickListener(R.id.msg, R.id.img)//设置控件的点击事件
                .addOnLongClickListener(R.id.msg, R.id.img)//设置控件长按事件
                .setOnQuickConvertListener(new QuickBindingAdapter.OnQuickConvertListener() {//用于自定义更多功能
                    @Override
                    public void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item) {
                        int type = holder.getItemViewType();
                        Log.d("", "convert: " + type);
                        if (binding instanceof ItemBinding) {
                            DemoBean bean = (DemoBean) item;
                            ItemBinding itemBinding = (ItemBinding) binding;
                            TextView msg = itemBinding.msg;
                            Log.e("", "onItemChildClick: position:" + holder.getLayoutPosition() + "type:" + bean.getItemType());
                        } else if (binding instanceof Item1Binding) {
                            Demo1Bean bean = (Demo1Bean) item;
                            Item1Binding itemBinding = (Item1Binding) binding;
                            ImageView img = itemBinding.img;
                            Log.e("", "onItemChildClick: position:" + holder.getLayoutPosition() + "type:" + bean.getItemType());
                        }
                    }
                })
                .setOnItemChildClickListener(new QuickBindingAdapter.OnItemChildClickListener() {//实现item子控件点击监听
                    @Override
                    public void onItemChildClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                        if(view.getId() == R.id.img){

                        }else if(view.getId() == R.id.msg){

                        }
                        //...
                        if (binding instanceof ItemBinding) {
                            DemoBean bean = (DemoBean) adapter.getData().get(position);
                            Log.e("", "onItemChildClick: " + position + "type:" + bean.getItemType());
                        } else if (binding instanceof Item1Binding) {
                            Demo1Bean bean = (Demo1Bean) adapter.getData().get(position);
                            Log.e("", "onItemChildClick: " + position + "type:" + bean.getItemType());
                        }
                    }
                }).setOnItemClickListener(new QuickBindingAdapter.OnItemClickListener() {//实现item控件点击监听
                    @Override
                    public void onItemClick(ViewDataBinding binding, View v, int position) {
                        Log.e("", "onItemClick: " + position);
                    }
                }).setOnItemLongClickListener(new QuickBindingAdapter.OnItemLongClickListener() {//实现item控件长按监听
                    @Override
                    public boolean onItemLongClick(ViewDataBinding binding, View v, int position) {
                        Log.e("", "onItemLongClick: " + position);
                        return true;
                    }
                }).setOnItemChildLongClickListener(new QuickBindingAdapter.OnItemChildLongClickListener() {//实现item子控件长按监听
                    @Override
                    public boolean onItemChildLongClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                        Log.e("", "onItemLongClick: " + position);
                        return true;
                    }
                });

        binding.recyclerView.setAdapter(mQuickAdapter);
        getData();
        mQuickAdapter.setNewData(list);//设置新数据
    }

//    private List<DemoBean> list = new ArrayList<>();//只是单类型

    private List<BaseBindBean> list = new ArrayList<>();//如果说多类型直接用 BaseBean


    private void getData() {
        list.clear();
        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                Demo1Bean bean1 = new Demo1Bean();
                bean1.setType(1);
                list.add(bean1);
            } else {
                DemoBean bean = new DemoBean();
                bean.setMsg("item" + i);
                bean.setType(0);
                list.add(bean);
            }
        }
    }

    int page = 0;

    @Override
    public void onLoadMoreRequested() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                page += 1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 1) {
                            getData();
                            mQuickAdapter.addData(list);
                            mQuickAdapter.loadMoreComplete();
                        } else if (page == 2) {
                            page -= 1;
                            mQuickAdapter.loadMoreFail();
                        } else {
                            page -= 1;
                            mQuickAdapter.loadMoreEnd();
                        }
                    }
                });

            }
        }).start();
    }
}
