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
    protected val binding: BD by lazy {
        DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, null, false)
    }

    protected lateinit var mModel: VM

    override fun setLayoutView(layoutId: Int) {
        setContentView(binding.root)
        val clx: Class<VM> = TUtil.getInstance(this, 0)
        mModel = ViewModelProvider(this)[clx]
        lifecycle.addObserver(mModel)//添加声明周期
        mModel.setLifecycleInstance(lifecycle)//设置声明周期对象
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mModel)
    }
}

