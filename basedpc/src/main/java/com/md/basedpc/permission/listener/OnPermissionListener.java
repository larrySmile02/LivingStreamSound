package com.md.basedpc.permission.listener;

import com.md.basedpc.permission.PermissionStatus;

/**
 * @CreateDate: 2020/1/6 13:29
 * @Author: shiq@yxt.com
 * @Description:
 */
public interface OnPermissionListener {

    /**
     * 所有权限都授权成功时回调
     * 回调时机:
     * 1. 全部权限都已获取
     * 2. 系统弹框后都同意
     * 3. 系统弹框后拒绝权限，然后在权限向导页跳至设置-应用-App页面获取了权限
     */
    void onPermissionGranted(PermissionStatus[] permissions);

    /**
     * 有权限未授权通过
     * 回调时机:
     * 1. 系统弹框后拒绝权限, 不允许打开权限向导页
     * 2. 系统弹框后拒绝权限, 允许打开权限向导页, 然后在权限向导页返回，或者跳至设置-应用-APP详情页面也没给权限
     */
    void onPermissionDenied(PermissionStatus[] permissions);

    /**
     * 有权限未授权通过
     * 回调时机:
     * 1. 系统弹框后拒绝权限时直接回调, 方便用户使用
     */
    void onPermissionDeniedImmediate(PermissionStatus[] permissions);

}
