package com.shengjiangbo.databindingadapter;

import android.content.Intent;
import android.view.View;

import com.sheng.mvvm.BaseBindActivity;
import com.sheng.mvvm.BaseViewModel;
import com.shengjiangbo.databindingadapter.databinding.ActivityMainBinding;

public class MainActivity extends BaseBindActivity<BaseViewModel, ActivityMainBinding> {


    @Override
    protected void initView() {
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DemoActivity.class));
            }
        });

        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PagerSnapHelperActivity.class));
            }
        });
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(TextKTXActivity.class);
            }
        });

        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(TextKTXActivity.class);
            }
        });
        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(BounceActivity.class);
            }
        });

        binding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FlowLayoutActivity.class);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
