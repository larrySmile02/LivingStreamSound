package com.md.basedpc.permission;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yxt.basic_frame.application.AppManager;
import com.yxt.basic_frame.permission.listener.OnPermissionListener;
import com.yxt.phutils.PHStringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @CreateDate: 2020/1/8 16:06
 * @Author: shiq@yxt.com
 * @Description: 处理请求权限失败情况，每一个未授权的权限，都会去进入权限向导页面
 */
public class PermissionGuideProcessor {

    private WeakReference<OnPermissionListener> listenerWeakReference;
    private String[] allPermission;
    private PermissionStatus[] refusedPermission;

    private String tag;
    private int index;

    public PermissionGuideProcessor(OnPermissionListener listener, String[] allPermission, PermissionStatus[] refusedPermission) {
        listenerWeakReference = new WeakReference<>(listener);
        this.allPermission = allPermission;
        this.refusedPermission = refusedPermission;
        this.tag = "permission_" + System.currentTimeMillis();
        index = 0;

        EventBus.getDefault().register(this);
    }

    public void startProcess() {
        index = 0;
        proceed();
    }

    private void proceed() {
        if (index >= refusedPermission.length) {
            sendCallback();
            return;
        }

        String permission = refusedPermission[index].permissionName;
        if (!PermissionUtils.isPermissionGrant(permission)) {
            ARouter.getInstance().build("/permission/permission_guide")
                    .withString("permission", permission)
                    .withString("tag", tag)
                    .navigation(AppManager.getAppManager().currentActivity(), new NavCallback() {
                        @Override
                        public void onArrival(Postcard postcard) {
                        }

                        @Override
                        public void onFound(Postcard postcard) {
                        }

                        @Override
                        public void onLost(Postcard postcard) {
                            sendCallback();
                        }

                        @Override
                        public void onInterrupt(Postcard postcard) {
                            sendCallback();
                        }
                    });
        } else {
            index++;
            proceed();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionGuideEvent bean) {
        if (PHStringUtils.equals(tag, bean.getTag())) {
            index++;
            proceed();
        } else {
            // 一旦收到非当前tag的事件，意味着可能正在处理其他的权限，我们应该直接结束权限请求
            sendCallback();
        }
    }

    private void sendCallback() {
        List<PermissionStatus> lastedRefusedPermissions = new ArrayList<>();
        for (PermissionStatus permission : refusedPermission) {
            if (!PermissionUtils.isPermissionGrant(permission.permissionName)) {
                lastedRefusedPermissions.add(PermissionUtils.createPermission(permission.permissionName));
            }
        }

        if (lastedRefusedPermissions.isEmpty()) {
            for (String permission : allPermission) {
                lastedRefusedPermissions.add(PermissionUtils.createPermission(permission));
            }
            if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                listenerWeakReference.get().onPermissionGranted(lastedRefusedPermissions.toArray(new PermissionStatus[0]));
            }
        } else {
            if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                listenerWeakReference.get().onPermissionDenied(lastedRefusedPermissions.toArray(new PermissionStatus[0]));
            }
        }

        EventBus.getDefault().unregister(this);
    }
}
