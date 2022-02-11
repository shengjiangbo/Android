package com.sheng.mvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 11:29
 * 类描述：
 */
abstract class BaseBindVMFragment<VM : BaseViewModel, BD : ViewDataBinding> : BaseFragment(),
    Observer<Any> {

    protected lateinit var binding: BD

    protected val mModel: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseBindVMFragment, 0)
        ViewModelProvider(this@BaseBindVMFragment)[clx]
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, container, false)
        mModel.data.observeForever(this)
        return binding.root
    }

    override fun onChanged(t: Any?) {
        TODO("Not yet implemented")
    }

}