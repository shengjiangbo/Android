package com.sheng.mvvm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 16:53
 * 类描述：
 */
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    protected open lateinit var mContext: Context

    @SuppressLint("MissingSuperCall")
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
     * 状态栏文字颜色
     * true  黑色
     * false  白色
     */
    open fun statusBarColor(): Boolean {
        return true
    }

    /**
     * 沉浸式状态栏
     */
    open fun setImmersiveStatusBar() {
//        ImmersiveStatusBar.setImmersiveStatusBar(this, statusBarColor())
//        immerse(navigationIsBlack = false)
        showStatusBar(Color.TRANSPARENT, light = true)
    }

    open fun setLayoutView(layoutId: Int) {
        setContentView(layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.create().remove(this)
    }

    /**
     * 传递了数据才会走这里
     */
    open fun initData(bundle: Bundle) {}

    protected abstract val layoutId: Int

    /**
     * 没有传递数据
     */
    open fun initData() {}
    open fun initListener() {}
    protected abstract fun initView()

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

    /**********权限申请*************/
    /**
     * msg:再次申请权限的理由内容
     * perms:需要申请的权限
     * val perms = arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE)
     * String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
     */
    protected fun requestPermissions(msg: String, requestCode: Int, vararg perms: String) {
        if (hasCameraPermission(*perms)) {
            permissionSuccess(requestCode)
        } else {
            EasyPermissions.requestPermissions(
                this, msg,
                requestCode,
                *perms
            )
        }
    }

    private fun hasCameraPermission(vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(this, *perms)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    protected val TAG: String = this.javaClass.simpleName

    override fun onPermissionsGranted(requestCode: Int, list: List<String?>) {
        // Some permissions have been granted
        val str = list.toTypedArray().contentToString()
        Log.e(TAG, "onPermissionsGranted: requestCode:$requestCode 获得的权限:$str")
        // TODO: 2022/1/27 获取权限成功也可以使用该方法判断
    }

    /**
     * 获取权限成功
     */
    open fun permissionSuccess(requestCode: Int) {
        Log.e(TAG, "permissionSuccess: requestCode:$requestCode")
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String?>) {
        // Some permissions have been denied
        val str = list.toTypedArray().contentToString()
        Log.e(TAG, "onPermissionsDenied: requestCode:$requestCode 拒绝的权限:$str")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d(TAG, "onRationaleAccepted:$requestCode")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d(TAG, "onRationaleDenied:$requestCode")
    }
}