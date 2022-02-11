package com.sheng.mvvm

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 17:13
 * 类描述：java
 */
abstract class BaseBindActivity<BD : ViewDataBinding> : BaseActivity() {
    protected lateinit var binding: BD


    override fun setLayoutView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

}

