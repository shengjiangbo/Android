package com.sheng.mvvm

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 17:13
 * 类描述：java
 */
abstract class BaseBindActivity<VM : BaseViewModel, BD : ViewDataBinding> : BaseActivity() {
    protected lateinit var  binding: BD

    protected lateinit var mModel: VM

    override fun setLayoutView(layoutId: Int) {
        binding = DataBindingUtil.inflate<BD>(LayoutInflater.from(mContext), layoutId, null, false)
        setContentView(binding.root)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mModel)
    }
}

