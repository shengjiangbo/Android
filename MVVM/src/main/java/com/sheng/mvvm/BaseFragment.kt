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
abstract class BaseFragment : LazyLoadFragment() {
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




     override fun onFirstLoad() {
        initView()
        val bundle = arguments
        if (bundle != null) {
            initData(bundle) //传递数据了
        } else {
            initData()
        }
        initListener()
    }

    open fun initListener() {

    }

    open fun initData(bundle: Bundle) {}
    open fun initData() {}
    protected abstract fun initView()

    /**
     * 不使用注解
     *     override val layoutId: Int
    get() = R.layout.demo_layout
     */
    protected abstract val layoutId: Int


    // startActivity
    protected fun openActivity(cls: Class<*>) {
        openActivity(cls, null)
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}