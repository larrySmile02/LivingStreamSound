package com.md.basedpc.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yxt.basic_frame.log.Log;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;


/**
 * create by 徐黎平 on 2020年03月05日17:28:46
 * 自定义Activity堆栈管理
 */
public class AppManager {

    private static Stack<Activity> mActivityStack;
    private static Stack<Intent> mActivityNeedShow;
    private static AppManager mInstance;
    private Context appContext;
    private Context mainContext;

    private AppManager() {

    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public Context getNowContext() {
        return currentActivity() == null ? appContext : currentActivity();
    }

    public Context getMainContext() {
        return mainContext;
    }

    public void setMainContext(Context mainContext) {
        this.mainContext = mainContext;
    }

    public static AppManager getAppManager() {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        Log.w("添加启动Activity:"+activity.getClass().getName());
        mActivityStack.add(activity);
    }

    /**
     * 获取当前Context（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (mActivityStack == null || mActivityStack.isEmpty()) {
            return null;
        }
        try {
            Activity activity = mActivityStack.lastElement();
//            if (activity.isDestroyed()||activity.isFinishing())
//                activity = mActivityStack.get(mActivityStack.indexOf(currentActivity())-1);
            return activity;
        } catch (Exception e) {
            Log.exceptionLog(e);
            return null;
        }
    }

    /**
     * 获取所有Activity
     */
    public Stack<Activity> allActivity() {
        return mActivityStack;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (mActivityStack != null) {
            Activity activity = mActivityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            mActivityStack.remove(activity);
            Log.w("移除Activity:"+activity.getClass().getName());
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack != null) {
            try {
                Iterator<Activity> iterator = mActivityStack.iterator();
                while (iterator.hasNext()) {
                    Activity activity = iterator.next();
                    if (activity != null && activity.getClass().equals(cls)) {
                        finishActivity(activity);
                    }
                }

            } catch (ConcurrentModificationException e) {
                Log.e(e.getMessage());
            } catch (Exception e) {
                Log.e(e.getMessage());
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityNoSelf(Class<?> cls) {
        if (mActivityStack != null) {
            for (int i = 0, size = mActivityStack.size(); i < size; i++) {
                if (null != mActivityStack.get(i) && !mActivityStack.get(i).getClass().equals(cls)) {
                    mActivityStack.get(i).finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (int i = 0, size = mActivityStack.size(); i < size; i++) {
                if (null != mActivityStack.get(i)) {
                    mActivityStack.get(i).finish();
                }
            }
            mActivityStack.clear();
        }
    }

    /**
     * 检查是否启动摸个Activity
     */
    public boolean isStartActivity(Class<?> cls) {
        boolean b = false;
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    /**
     * 检查是否启动某个Activity
     */
    public boolean isStartActivity(String cls) {
        boolean b = false;
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().getName().equals(cls)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}