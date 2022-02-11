package com.sheng.mvvm

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 17:13
 * 类描述：java
 */
abstract class BaseBindVMActivity<VM : BaseViewModel, BD : ViewDataBinding> : BaseActivity(),
    Observer<Any> {

    protected lateinit var binding: BD

    protected val model: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseBindVMActivity, 0)
        ViewModelProvider(this@BaseBindVMActivity)[clx]
    }

    override fun setLayoutView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
        model.data.observeForever(this)
    }

    override fun onChanged(t: Any?) {
        TODO("Not yet implemented")
    }
}

