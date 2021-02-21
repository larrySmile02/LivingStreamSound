package com.md.basedpc.permission;

import android.Manifest;
import android.os.Build;

import com.md.basedpc.PHStringUtils;
import com.md.basedpc.permission.listener.OnPermissionListener;
import com.md.basedpc.persistence.PHSPUtil;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

import static com.md.basedpc.permission.PermissionUtils.createPermission;

/**
 * @CreateDate: 2020/1/6 13:20
 * @Author: shiq@yxt.com
 * @Description: 1. 对SoulPermission简单的封装，避免当框架替换时，需
 * 要改动过多的地方
 * 2. 当权限被拒绝时，跳转到权限向导页，引导用户去设置里面授权权限
 */
public class PermissionHelper {
    private PermissionHelper() {
    }

    public static void checkAndRequestPermission(String[] permissions, OnPermissionListener listener) {
        checkAndRequestPermission(permissions, listener, false);
    }

    public static void checkAndRequestPermission(String[] permissions, OnPermissionListener listener, boolean openPermissionGuideAfterDeny) {
        if (Build.VERSION.SDK_INT <= 22) {
            if (listener != null) {
                listener.onPermissionGranted(null);
            }
        } else {
            SoulPermission.getInstance().checkAndRequestPermissions(Permissions.build(permissions), new CheckRequestPermissionsListener() {
                @Override
                public void onAllPermissionOk(Permission[] allPermissions) {
                    if (listener != null) {
                        listener.onPermissionGranted(createPermission(allPermissions));
                    }
                }

                @Override
                public void onPermissionDenied(Permission[] refusedPermissions) {
                    PermissionStatus[] permissionStatuses = createPermission(refusedPermissions);
                    if (listener != null) {
//                        listener.onPermissionDeniedImmediate(permissionStatuses);
                    }
                    if (openPermissionGuideAfterDeny) {
                        new PermissionGuideProcessor(listener, permissions, permissionStatuses).startProcess();
                    } else {
                        if (listener != null) {
                            listener.onPermissionDenied(permissionStatuses);
                        }
                    }
                }
            });
        }
    }

    public static boolean isNecessaryPermission(String permission) {
        if (PHStringUtils.isEmpty(permission)) {
            return false;
        }

        switch (permission) {
            case Manifest.permission_group.STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return true;
        }

        return false;
    }

//    /**
//     * 请求通知权限，App默认是有通知权限的，如果用户关闭后，一个月最多请求一次
//     */
//    public static void requestNotificationPermissionIfNeed() {
//        boolean hasPermission = SoulPermission.getInstance().checkSpecialPermission(Special.NOTIFICATION);
//        if (!hasPermission) {
//            long lastRequestTime = PHSPUtil.getInstance().getLong("last_notification_request_time");
//            long monthMillis = 30 * 24 * 60 * 60 * 1000L;
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - lastRequestTime > monthMillis) {
//                PHSPUtil.getInstance().putLong("last_notification_request_time", currentTime);
//                PermissionUtils.showNotificationGuideDialog();
//            }
//        }
//    }

    /**
     * 获取手机存储权限
     *
     * @param listener
     */
    public static void requestNecessaryPermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, listener);
    }

    /**
     * 获取手机存储权限
     *
     * @param listener
     */
    public static void requestNecessaryPermission(OnPermissionListener listener, boolean openPermissionGuideAfterDeny) {
        checkAndRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, listener, openPermissionGuideAfterDeny);
    }

    /**
     * 获取录音权限
     *
     * @param listener
     */
    public static void requestAudioPermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.RECORD_AUDIO}, listener);
    }

    /**
     * 获取录音权限
     *
     * @param listener
     */
    public static void requestAudioPermission(OnPermissionListener listener, boolean openPermissionGuideAfterDeny) {
        checkAndRequestPermission(new String[]{Manifest.permission.RECORD_AUDIO}, listener, openPermissionGuideAfterDeny);
    }

    /**
     * 获取相机权限
     *
     * @param listener
     */
    public static void requestCameraPermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.CAMERA}, listener);
    }

    /**
     * 获取定位权限
     *
     * @param listener
     */
    public static void requestLocationPermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, listener);
    }

    /**
     * 获取打定话权限
     *
     * @param listener
     */
    public static void requestCallPermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.CALL_PHONE}, listener);
    }

    /**
     * 获取手机信息
     *
     * @param listener
     */
    public static void requestPhonePermission(OnPermissionListener listener) {
        checkAndRequestPermission(new String[]{Manifest.permission.READ_PHONE_STATE}, listener);
    }

}
