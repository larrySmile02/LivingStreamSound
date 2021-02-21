package com.md.basedpc.permission.listener;


import com.md.basedpc.permission.PermissionStatus;

/**
 * @CreateDate: 2020/1/8 14:26
 * @Author: shiq@yxt.com
 * @Description:
 */
public abstract class OnPermissionListenerAdapter implements OnPermissionListener {

    @Override
    public abstract void onPermissionGranted(PermissionStatus[] permissions);

    @Override
    public void onPermissionDenied(PermissionStatus[] permissions) {
    }

    @Override
    public void onPermissionDeniedImmediate(PermissionStatus[] permissions) {
    }
}
