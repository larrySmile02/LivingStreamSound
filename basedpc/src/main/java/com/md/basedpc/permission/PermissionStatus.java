package com.md.basedpc.permission;

import com.qw.soul.permission.bean.Permission;

/**
 * @CreateDate: 2020/1/6 13:52
 * @Author: shiq@yxt.com
 * @Description:
 */
public class PermissionStatus extends Permission {
    public PermissionStatus(String permissionName, int isGranted, boolean shouldRationale) {
        super(permissionName, isGranted, shouldRationale);
    }
}
