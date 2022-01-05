package com.shengjiangbo.databindingadapter;

import com.sheng.mvvm.BaseBindVMActivity;
import com.sheng.mvvm.BaseViewModel;
import com.shengjiangbo.databindingadapter.databinding.ActivityFishBinding;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/29 11:42
 * 类描述：
 */
public class FishActivity extends BaseBindVMActivity<BaseViewModel, ActivityFishBinding> {
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fish;
    }
}
