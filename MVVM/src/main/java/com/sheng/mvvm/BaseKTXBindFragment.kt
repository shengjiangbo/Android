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
abstract class BaseKTXBindFragment<VM : BaseViewModel, BD : ViewDataBinding> : BaseFragment(),
    Observer<Any> {

    protected lateinit var binding: BD

    protected val model: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseKTXBindFragment, 0)
        ViewModelProvider(this@BaseKTXBindFragment)[clx]
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, container, false)
        model.data.observeForever(this)
        binding.lifecycleOwner = this
        binding.setVariable(BR.model, model)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            binding.unbind()
        } catch (e: Exception) {
        }
    }

    override fun onChanged(t: Any?) {
        TODO("Not yet implemented")
    }
}