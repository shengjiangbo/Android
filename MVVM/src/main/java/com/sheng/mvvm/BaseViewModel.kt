package com.sheng.mvvm

import androidx.lifecycle.*

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 18:22
 * 类描述：
 */
open class BaseViewModel : ViewModel(){

    val data: MutableLiveData<Any> = MutableLiveData()

    /**
     * viewModel销毁
     */
    override fun onCleared() {
        super.onCleared()

    }

}