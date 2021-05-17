package com.shengjiangbo.databindingadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sheng.mvvm.BaseBindActivity;
import com.sheng.mvvm.BaseKTXBindActivity;
import com.sheng.mvvm.BaseViewModel;
import com.sheng.mvvm.Bind;
import com.shengjiangbo.databindingadapter.databinding.ActivityMainBinding;

@Bind(layoutId = R.layout.activity_main, viewModel = {BaseViewModel.class})
public class MainActivity extends BaseBindActivity<BaseViewModel,ActivityMainBinding> {


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
    }
}
