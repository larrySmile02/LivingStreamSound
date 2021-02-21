package com.md.basedpc.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.md.basedpc.R;
import com.md.basedpc.application.AppManager;
import com.qw.soul.permission.PermissionTools;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.GoAppDetailCallBack;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * @CreateDate: 2020/1/6 14:33
 * @Author: shiq@yxt.com
 * @Description: Permission工具类
 */
public class PermissionUtils {

    private PermissionUtils() {
    }

    public static @NonNull
    PermissionStatus[] getPermissionStatus(@NonNull String[] permissions) {
        Permission[] permissionsData = SoulPermission.getInstance().checkPermissions(permissions);

        PermissionStatus[] permissionStatuses = new PermissionStatus[permissions.length];
        for (int i = 0; i < permissionsData.length; i++) {
            permissionStatuses[i] = createPermission(permissionsData[i]);
        }

        return permissionStatuses;
    }

    public static boolean isPermissionGrant(@NonNull String permission) {
        PermissionStatus[] permissionStatuses = getPermissionStatus(new String[]{permission});
        return permissionStatuses[0].isGranted();
    }

    public static PermissionStatus createPermission(String permission) {
        if (permission == null) {
            return null;
        }

        boolean isGranted = isPermissionGrant(permission);
        Activity activity = AppManager.getAppManager().currentActivity();
        boolean shouldRationale;
        if (activity != null) {
            shouldRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        } else {
            shouldRationale = false;
        }

        return new PermissionStatus(permission, getPermissionFlag(isGranted), shouldRationale);
    }

    public static PermissionStatus createPermission(Permission permission) {
        if (permission == null) {
            return null;
        }
        return new PermissionStatus(permission.permissionName, getPermissionFlag(permission.isGranted()), permission.shouldRationale());
    }

    public static PermissionStatus[] createPermission(Permission[] permissions) {
        if (permissions == null) {
            return null;
        }
        PermissionStatus[] permissionStatuses = new PermissionStatus[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            permissionStatuses[i] = createPermission(permissions[i]);
        }
        return permissionStatuses;
    }

    public static int getPermissionFlag(boolean isGranted) {
        if (isGranted) {
            return PackageManager.PERMISSION_GRANTED;
        } else {
            return PackageManager.PERMISSION_DENIED;
        }
    }

//    public static void showNotificationGuideDialog() {
//        NotificationPermissionService service = (NotificationPermissionService) ARouter.getInstance().build("/permission/notification_guide").navigation();
//        service.showNotificationPermissionDialog();
//    }

    public static boolean isAllPermissionGrant(@NonNull String[] permissions) {
        PermissionStatus[] permissionStatuses = getPermissionStatus(permissions);
        for (PermissionStatus status : permissionStatuses) {
            if (!status.isGranted()) {
                return false;
            }
        }

        return true;
    }

    public static void goAppSettings(GoAppDetailCallBack callBack) {
        SoulPermission.getInstance().goApplicationSettings(callBack);
    }

    public static void goAppNotificationSettings() {
        Context context = AppManager.getAppManager().getNowContext();
        if (context instanceof Activity) {
            Intent intent = PermissionTools.getAppManageIntent(context);
            context.startActivity(intent);
        } else {
            SoulPermission.getInstance().goApplicationSettings();
        }
    }

//    public static String getPermissionDesc(Context context, String permission) {
//        String desc;
//        switch (permission) {
//            case Manifest.permission_group.CAMERA:
//            case Manifest.permission.CAMERA:
//                desc = context.getString(R.string.common_yxt_lbl_cameraname);
//                break;
//            case Manifest.permission_group.CONTACTS:
//            case Manifest.permission.READ_CONTACTS:
//            case Manifest.permission.WRITE_CONTACTS:
//            case Manifest.permission.GET_ACCOUNTS:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_contactname);
//                break;
//            case Manifest.permission_group.PHONE:
//            case Manifest.permission.CALL_PHONE:
//            case Manifest.permission.READ_CALL_LOG:
//            case Manifest.permission.WRITE_CALL_LOG:
//            case Manifest.permission.ADD_VOICEMAIL:
//            case Manifest.permission.USE_SIP:
//            case Manifest.permission.PROCESS_OUTGOING_CALLS:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_callname);
//                break;
//            case Manifest.permission_group.STORAGE:
//            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
//            case Manifest.permission.READ_EXTERNAL_STORAGE:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_storagename);
//                break;
//            case Manifest.permission_group.LOCATION:
//            case Manifest.permission.ACCESS_FINE_LOCATION:
//            case Manifest.permission.ACCESS_COARSE_LOCATION:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_locationname);
//                break;
//            case Manifest.permission.READ_PHONE_STATE:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_phonestatusname);
//                break;
//            case Manifest.permission_group.CALENDAR:
//            case Manifest.permission.READ_CALENDAR:
//            case Manifest.permission.WRITE_CALENDAR:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_calendername);
//                break;
//            case Manifest.permission_group.MICROPHONE:
//            case Manifest.permission.RECORD_AUDIO:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_audioname);
//                break;
//            case Manifest.permission_group.SENSORS:
//            case Manifest.permission.BODY_SENSORS:
//                desc = context.getResources().getString(R.string.common_yxt_lbl_sensorname);
//                break;
//            case Manifest.permission_group.SMS:
//            case Manifest.permission.SEND_SMS:
//            case Manifest.permission.RECEIVE_SMS:
//            case Manifest.permission.READ_SMS:
//            case Manifest.permission.RECEIVE_WAP_PUSH:
//            case Manifest.permission.RECEIVE_MMS:
//                desc = context.getString(R.string.common_yxt_lbl_smsname);
//                break;
//            default:
//                desc = context.getString(R.string.common_yxt_lbl_undefinename);
//                break;
//        }
//        return desc;
//    }

}
