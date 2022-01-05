package com.sheng.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 16:53
 * 类描述：
 */
abstract class BaseActivity : AppCompatActivity() {
    protected open lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setImmersiveStatusBar()
        setLayoutView(layoutId)
        ActivityStack.create().add(this)
        clearFragmentsBeforeCreate()
        initView()
        val bundle = intent.extras
        if (bundle != null) {
            initData(bundle) //传递数据了
        } else {
            initData()
        }
        initListener()
    }

    /**
     * 沉浸式状态栏
     */
    open fun setImmersiveStatusBar() {
        ImmersiveStatusBar.setImmersiveStatusBar(this, false)
    }

    open fun setLayoutView(layoutId: Int) {
        setContentView(layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.create().remove(this)
    }

    open fun initData(bundle: Bundle) {}
    open fun initData() {}
    open fun initListener() {}
    protected abstract fun initView()
    protected abstract val layoutId: Int

    /**
     * 处理因为Activity重建导致的fragment叠加问题
     */
    protected open fun clearFragmentsBeforeCreate() {
        try {
            val fragments = supportFragmentManager.fragments
            if (fragments.size == 0) {
                return
            }
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in fragments) {
                fragmentTransaction.remove(fragment!!)
            }
            fragmentTransaction.commitNow()
        } catch (e: Exception) {
        }
    }

    // startActivity
    protected fun openActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    // startActivity
    protected fun openActivity(cls: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, cls)
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