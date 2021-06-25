package com.shengjiangbo.databindingadapter

import android.content.Intent
import android.util.Log
import com.sheng.mvvm.BaseKTXBindActivity
import com.sheng.mvvm.BaseViewModel
import com.shengjiangbo.databindingadapter.databinding.ActivityTestBinding

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 12:08
 * 类描述：
 */
class TextKTXActivity : BaseKTXBindActivity<BaseViewModel, ActivityTestBinding>() {
    override fun initView() {
        supportFragmentManager.beginTransaction().add(R.id.fl_layout, TestFragment()).commitAllowingStateLoss()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("TextKTXActivity", "onActivityResult: $requestCode resultCode:$resultCode")
    }

    override val layoutId: Int
        get() = R.layout.activity_test
}