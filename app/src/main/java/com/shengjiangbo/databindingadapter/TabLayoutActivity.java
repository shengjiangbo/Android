package com.shengjiangbo.databindingadapter;

import com.sheng.mvvm.BaseBindVMActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivityTabLayoutBinding;
import com.shengjiangbo.databindingadapter.paging.PagingViewModel;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/15 17:01
 * 类描述：
 */
public class TabLayoutActivity extends BaseBindVMActivity<PagingViewModel, ActivityTabLayoutBinding> {
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tab_layout;
    }
}
