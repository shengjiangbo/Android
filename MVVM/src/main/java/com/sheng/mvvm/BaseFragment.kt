package com.sheng.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions

/**
 * 创建人：Bobo
 * 创建时间：2021/5/14 16:53
 * 类描述：
 */
abstract class BaseFragment : LazyLoadFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    protected open lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        return EasyPermissions.hasPermissions(mContext, *perms)
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