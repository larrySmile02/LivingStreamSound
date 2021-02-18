package com.md.basedpc.application;

import android.content.Context;
import com.md.basedpc.log.LogUtils;
import com.md.basedpc.persistence.PHDbUtils;
import com.md.basedpc.persistence.PHSPUtil;
import com.qw.soul.permission.SoulPermission;

import androidx.multidex.MultiDexApplication;

/**
 * create by 徐黎平 on 2020年03月05日17:28:46
 * BaseApplication
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (isMainThread()) {
        SoulPermission.init(this);
        PHSPUtil.getInstance().init(this);
        PHSPUtil.getInstance().putLong("optimization", System.currentTimeMillis());
        android.util.Log.e("性能优化----->", "主进程app启动了");
        initData(this);
//        } else {
//            android.util.Log.e("性能优化----->", "子进程app启动了");
//        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppManager.getAppManager().setAppContext(this);
//        MultiDex.install(this);
    }

    private void initData(Context context) {
        PHDbUtils.getInstance().initDB(this, null);
        PHSPUtil.getInstance().init(this);
        LogUtils.initPath(context);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        if (isMainThread()) {
//            ARouter.getInstance().destroy();
//        }
    }

//    public boolean isMainThread() {
//        String processName1 = Utils.getProcessName(getApplicationContext());
//        return (processName1 != null && processName1.equals(getApplicationContext().getPackageName()));
//    }
}
