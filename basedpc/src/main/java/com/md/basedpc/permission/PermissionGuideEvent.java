package com.md.basedpc.permission;

/**
 * @CreateDate: 2020/1/8 15:35
 * @Author: shiq@yxt.com
 * @Description:
 */
public class PermissionGuideEvent {

    private String tag;

    private String permission;
    private boolean isGranted;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }
}
