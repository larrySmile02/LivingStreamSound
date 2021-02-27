package com.liulishuo.filedownloader.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;


/**
 * Created by Jacksgong on 12/17/15.
 */
public class DemoApplication extends Application {
    public static Context CONTEXT;
    private final static String TAG = "FileDownloadApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // for demo.
        CONTEXT = this;
        // just for open the log in this demo project.
        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        /**
         * just for cache Application's Context, and ':filedownloader' progress will NOT be launched
         * by below code, so please do not worry about performance.
         * @see FileDownloader#init(Context)
         */
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }
}
