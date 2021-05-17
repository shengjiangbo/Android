package com.shengjiangbo.databindingadapter

import com.sheng.mvvm.BaseKTXBindActivity
import com.sheng.mvvm.BaseViewModel
import com.sheng.mvvm.Bind
import com.shengjiangbo.databindingadapter.databinding.ActivityTestBinding

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 12:08
 * 类描述：
 */
@Bind(layoutId = R.layout.activity_test, viewModel = [BaseViewModel::class])
class TextKTXActivity  : BaseKTXBindActivity<BaseViewModel, ActivityTestBinding>() {
    override fun initView() {

    }
}