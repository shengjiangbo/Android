package com.sheng.mvvm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * 创建人：Bobo
 * 创建时间：2021/4/13 15:26
 * 类描述：懒加载
 */
open class LazyLoadFragment : Fragment() {
    private var isLoaded = false //是否已经加载过
    private var isLoadCreatedView = false
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoadCreatedView = true
        if (!isLazyLoad) {
            onFirstLoad()
        } else if (isMenuVisible) {
            //增加了Fragment是否可见的判断
            if (!isLoaded && !isHidden) {
                isLoaded = true
                onFirstLoad()
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (isLazyLoad) {
            if (isLoadCreatedView && menuVisible) {
                //增加了Fragment是否可见的判断
                if (!isLoaded && !isHidden) {
                    isLoaded = true
                    onFirstLoad()
                }
            }
        }
    }

    protected open fun onFirstLoad() {}

    /**
     * 是否开启懒加载 默认开启
     *
     * @return
     */
    protected val isLazyLoad: Boolean
        get() = true

    override fun onDestroy() {
        super.onDestroy()
        isLoaded = false
        isLoadCreatedView = false
    }
}