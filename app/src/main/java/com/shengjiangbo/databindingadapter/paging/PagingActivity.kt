package com.shengjiangbo.databindingadapter.paging

import com.sheng.mvvm.BaseBindActivity
import com.shengjiangbo.databindingadapter.R
import com.shengjiangbo.databindingadapter.databinding.ActivityPagingBinding

/**
 * 创建人：Bobo
 * 创建时间：2021/5/20 16:46
 * 类描述：
 */
class PagingActivity : BaseBindActivity<PagingViewModel, ActivityPagingBinding>() {

    override fun initView() {

    }

    override fun onBackPressed() {
        setResult(100)
        finish()
    }

    override val layoutId: Int
        get() = R.layout.activity_paging

}