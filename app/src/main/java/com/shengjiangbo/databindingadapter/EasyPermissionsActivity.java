package com.shengjiangbo.databindingadapter;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sheng.mvvm.BaseBindActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivityEasyPermissionsBinding;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * 创建人：Bobo
 * 创建时间：2022/1/27 15:18
 * 类描述：
 */
public class EasyPermissionsActivity extends BaseBindActivity<ActivityEasyPermissionsBinding> {
    @Override
    protected void initView() {
        methodRequiresTwoPermission();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_easy_permissions;
    }

    private final int REQUEST_CODE = 145;

    @AfterPermissionGranted(REQUEST_CODE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions("App需要相机和存储权限", REQUEST_CODE, perms);
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == REQUEST_CODE) {
            // TODO: 2022/1/27  获取到权限后的逻辑 二选一
        }
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        super.onPermissionsGranted(requestCode, list);
        if (requestCode == REQUEST_CODE && list.contains(Manifest.permission.CAMERA) && list.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // TODO: 2022/1/27  获取到权限后的逻辑 二选一
        }
    }

    /**
     * 权限被拒绝了
     *
     * @param requestCode
     * @param list
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        super.onPermissionsDenied(requestCode, list);
        if (requestCode == REQUEST_CODE) {

        }
    }
}
