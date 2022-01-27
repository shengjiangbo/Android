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

    protected lateinit var binding: BD

    protected val mModel: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseKTXBindFragment, 0)
        ViewModelProvider(this@BaseKTXBindFragment)[clx]
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, container, false)
        return binding.root
    }
}