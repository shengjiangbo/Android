package com.shengjiangbo.databindingadapter;

import android.view.View;

import com.sheng.mvvm.BaseBindActivity;
import com.sheng.mvvm.BaseBindVMActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivityBezierBinding;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/29 14:45
 * 类描述：
 */
public class BezierActivity extends BaseBindActivity<ActivityBezierBinding> {
    @Override
    protected void initView() {

        binding.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bezierView.setControl(0);
            }
        });
        binding.tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bezierView.setControl(1);
            }
        });
        binding.tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bezierView.setControl(2);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bezier;
    }


}
