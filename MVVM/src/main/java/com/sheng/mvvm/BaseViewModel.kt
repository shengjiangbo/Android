package com.sheng.mvvm

import androidx.lifecycle.*

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 18:22
 * 类描述：
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    protected open lateinit var lifecycle: Lifecycle

    val data: MutableLiveData<Any> = MutableLiveData()

    /**
     * 拿到声明周期实例
     */
    fun setLifecycleInstance(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onStop() {

    }

    /**
     * viewModel销毁
     */
    override fun onCleared() {
        super.onCleared()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun onDestroy() {

    }
}