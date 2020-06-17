package com.shengjiangbo.databindingadapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shengjiangbo.databindingadapter.databinding.ActivityDemoBinding;
import com.shengjiangbo.databingdingadapter.BaseDataBindingBean;
import com.shengjiangbo.databingdingadapter.BaseBindingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 波
 * Date: 2020/6/17
 * Time: 11:19
 */
public class DemoActivity extends AppCompatActivity implements BaseBindingAdapter.OnRequestLoadMoreListener {

    private DemoAdapter mAdapter;
    private List<BaseDataBindingBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDemoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_demo, null, false);
        setContentView(binding.getRoot());
        mAdapter = new DemoAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mAdapter);
        getData();
        mAdapter.setNewData(list);
        mAdapter.setLoadMoreView(new MainLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, binding.recyclerView);
        mAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, View v, int position) {
                Log.e("", "onItemClick: " + position);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseBindingAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(ViewDataBinding binding, View v, int position) {
                Log.e("", "onItemClick: " + position);
                return true;
            }
        });
        mAdapter.setOnItemChildLongClickListener(new BaseBindingAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                Log.e("", "onItemChildLongClick: " + position);
                return true;
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseBindingAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                Log.e("", "onItemChildClick: " + position);
            }
        });
    }

    private void getData() {
        list.clear();
        for (int i = 0; i < 10; i++) {
            DemoBean bean = new DemoBean();
            bean.setMsg("item" + i);
            if (i % 3 == 0) {
                bean.setType(1);
            }
            list.add(bean);
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
                            mAdapter.addData(list);
                            mAdapter.loadMoreComplete();
                        } else if (page == 2) {
                            page -= 1;
                            mAdapter.loadMoreFail();
                        } else {
                            page -= 1;
                            mAdapter.loadMoreEnd();
                        }
                    }
                });

            }
        }).start();
    }
}
