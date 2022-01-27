package com.shengjiangbo.databindingadapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.splashscreen.SplashScreen;

import com.sheng.mvvm.BaseBindVMActivity;
import com.sheng.mvvm.BaseViewModel;
import com.shengjiangbo.databindingadapter.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends BaseBindVMActivity<BaseViewModel, ActivityMainBinding> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "onCreate: ");
    }

    @Override
    protected void initView() {
        Log.e("", "initView: ");
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
        binding.btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FishActivity.class);
            }
        });

        binding.btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(BezierActivity.class);
            }
        });
        binding.btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MotionLayoutActivity.class);
            }
        });
        binding.btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EasyPermissionsActivity.class);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
