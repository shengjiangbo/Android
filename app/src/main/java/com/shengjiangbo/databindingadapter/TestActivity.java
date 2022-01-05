package com.shengjiangbo.databindingadapter;

import com.sheng.mvvm.BaseBindVMActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivityTestBinding;
import com.shengjiangbo.databindingadapter.paging.PagingViewModel;

/**
 * 创建人：Bobo
 * 创建时间：2021/5/20 18:19
 * 类描述：
 */
public class TestActivity extends BaseBindVMActivity<PagingViewModel, ActivityTestBinding> {
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

}
