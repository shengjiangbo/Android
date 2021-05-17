package com.sheng.mvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 11:29
 * 类描述：
 */
abstract class BaseKTXBindFragment<VM : BaseViewModel, BD : ViewDataBinding> : BaseFragment() {

    protected val binding: BD by lazy {
        DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, null, false)
    }

    protected lateinit var  mModel: VM

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        val viewInject = this.javaClass.getAnnotation(Bind::class.java)
        if (viewInject != null) {
            if (viewInject.viewModel.isNotEmpty()) {
                val viewModel = viewInject.viewModel[0]
                val javaPrimitiveType = viewModel.javaPrimitiveType
                javaPrimitiveType?.let {
                    mModel = ViewModelProvider(this)[it] as VM
                    if(viewInject.viewModelId>0){
                        binding.setVariable(viewInject.viewModelId,mModel)
                    }
                    lifecycle.addObserver(mModel)//添加声明周期
                    mModel.setLifecycleInstance(lifecycle)//设置声明周期对象
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mModel)
    }
}