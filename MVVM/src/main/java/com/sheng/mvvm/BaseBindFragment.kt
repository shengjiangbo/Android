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
abstract class BaseBindFragment<BD : ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: BD

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        try {
            binding.unbind()
        } catch (e: Exception) {
        }
    }
}