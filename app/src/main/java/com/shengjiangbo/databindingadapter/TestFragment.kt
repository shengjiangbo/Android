package com.shengjiangbo.databindingadapter

import android.content.Intent
import android.util.Log
import com.sheng.mvvm.BaseBindFragment
import com.sheng.mvvm.BaseViewModel
import com.shengjiangbo.databindingadapter.databinding.FragmentTestBinding
import com.shengjiangbo.databindingadapter.paging.PagingActivity

/**
 * 创建人：Bobo
 * 创建时间：2021/5/21 10:37
 * 类描述：
 */
class TestFragment : BaseBindFragment<FragmentTestBinding>() {
    override fun initView() {

    }

    override fun initListener() {
        super.initListener()
        binding.tvStart.setOnClickListener {
            openActivity(PagingActivity::class.java,1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("TestFragment", "onActivityResult: $requestCode resultCode:$resultCode")
    }

    override val layoutId: Int
        get() = R.layout.fragment_test
}