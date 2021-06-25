package com.sheng.mvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 11:29
 * 类描述：
 */
abstract class BaseKTXBindFragment<VM : BaseViewModel, BD : ViewDataBinding> : BaseFragment() {

    protected val binding: BD by lazy {
        DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, null, false)
    }

    protected lateinit var mModel: VM

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        val clx: Class<VM> = TUtil.getInstance(this, 0)
        mModel = ViewModelProvider(this)[clx]
        lifecycle.addObserver(mModel)//添加声明周期
        mModel.setLifecycleInstance(lifecycle)//设置声明周期对象
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mModel)
    }
}