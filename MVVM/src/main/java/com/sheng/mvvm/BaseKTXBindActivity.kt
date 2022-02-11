package com.sheng.mvvm


import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 17:13
 * 类描述：kotlin
 */
abstract class BaseKTXBindActivity<VM : BaseViewModel, BD : ViewDataBinding> : BaseActivity() {
    protected lateinit var binding: BD

    protected val mModel: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseKTXBindActivity, 0)
        ViewModelProvider(this@BaseKTXBindActivity)[clx]
    }

    override fun setLayoutView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

}

