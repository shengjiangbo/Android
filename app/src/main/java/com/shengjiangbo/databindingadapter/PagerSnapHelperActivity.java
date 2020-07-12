package com.shengjiangbo.databindingadapter;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.shengjiangbo.databindingadapter.databinding.ActivityPagerBinding;
import com.shengjiangbo.databingdingadapter.BaseBindBean;
import com.shengjiangbo.databingdingadapter.BaseBindAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerSnapHelperActivity extends AppCompatActivity implements BaseBindAdapter.OnRequestLoadMoreListener {

    private PagerSnapAdapter pagerSnapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPagerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_pager, null, false);
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new PagerSnapHelper().attachToRecyclerView(binding.recyclerView);
        pagerSnapAdapter = new PagerSnapAdapter();
        binding.recyclerView.setAdapter(pagerSnapAdapter);
        pagerSnapAdapter.setPagerSnapLoadMore();
        pagerSnapAdapter.setLoadMoreView(new MainLoadMoreView());
        pagerSnapAdapter.setOnLoadMoreListener(this, binding.recyclerView);
        for (int i = 0; i < 5; i++) {
            DemoBean demoBean = new DemoBean();
            list.add(demoBean);
        }
        pagerSnapAdapter.setNewData(list);
    }

    private List<BaseBindBean> list = new ArrayList<>();

    @Override
    public void onLoadMoreRequested() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i = 0; i < 5; i++) {
                    DemoBean demoBean = new DemoBean();
                    list.add(demoBean);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pagerSnapAdapter.addData(list);
                        pagerSnapAdapter.loadMoreComplete();
                    }
                });

            }
        }).start();
    }
}
