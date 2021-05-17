package com.sheng.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 16:53
 * 类描述：
 */
abstract class BaseFragment : Fragment() {
    protected open lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getRootView(inflater, container)
    }

    protected open fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {//关闭了懒加载
            onFirstLoad()
        }
    }

    protected open var isLoaded = false //设置开关页面是否需要懒加载 默认开启


    override fun onResume() {
        super.onResume()
        //增加了Fragment是否可见的判断
        if (!isLoaded && !isHidden) {
            isLoaded = true
            onFirstLoad()
        }
    }

    protected open fun onFirstLoad() {
        initView()
        val bundle = arguments
        if (bundle != null) {
            initData(bundle) //传递数据了
        } else {
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoaded = false
    }

    private fun initData(bundle: Bundle) {}
    private fun initData() {}
    protected abstract fun initView()

    /**
     * 不使用注解
     *     override val layoutId: Int
                get() = R.layout.demo_layout
     */
    protected open val layoutId: Int
        get() {
            val viewInject = this.javaClass.getAnnotation(Bind::class.java)
            return if (viewInject != null) {
                val layoutId: Int = viewInject.layoutId
                if (layoutId > 0) {
                    layoutId
                } else {
                    throw NullPointerException("请重写layoutId()或者使用Bind注解")
                }
            } else {
                throw NullPointerException("请重写layoutId()或者使用Bind注解")
            }
        }


    // startActivity
    // startActivity
    protected fun openActivity(cls: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(mContext, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    // startActivityForResult
    protected fun openActivity(cls: Class<*>, bundle: Bundle? = null, requestCode: Int) {
        val intent = Intent(mContext, cls)
        bundle?.let {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    // startActivityForResult
    protected fun openActivity(cls: Class<*>, requestCode: Int) {
        openActivity(cls, null, requestCode)
    }
}