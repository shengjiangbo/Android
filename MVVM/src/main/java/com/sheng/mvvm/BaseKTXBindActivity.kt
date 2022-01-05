package com.sheng.mvvm

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 17:13
 * 类描述：kotlin
 */
abstract class BaseKTXBindActivity<VM : BaseViewModel, BD : ViewDataBinding> : BaseActivity() {
    protected lateinit var binding: BD

    protected val mModel: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseKTXBindActivity, 0)
        val model = ViewModelProvider(this@BaseKTXBindActivity)[clx]
        lifecycle.addObserver(model)//添加声明周期
        model.setLifecycleInstance(lifecycle)//设置声明周期对象
        model
    }

    override fun setLayoutView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mModel)
    }
}

