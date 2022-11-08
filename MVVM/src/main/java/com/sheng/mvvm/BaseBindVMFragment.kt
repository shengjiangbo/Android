package com.sheng.mvvm

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
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

    protected val model: VM by lazy {
        val clx: Class<VM> = TUtil.getInstance(this@BaseBindVMFragment, 0)
        ViewModelProvider(this@BaseBindVMFragment)[clx]
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding =
            DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, container, false)
        model.data.observeForever(this)
        binding.lifecycleOwner = this
        binding.setVariable(BR.model, model)
        return binding.root
    }

    override fun onChanged(t: Any?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            binding.unbind()
        } catch (e: Exception) {
        }
    }

}

@MainThread
public inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}