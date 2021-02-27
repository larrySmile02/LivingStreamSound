package com.liulishuo.filedownloader.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;

/**
 * Created by Jacksgong on 12/17/15.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for demo.
        DemoApplication.CONTEXT = getApplicationContext();
        // just for open the log in this demo project.
        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        /**
         * just for cache Application's Context, and ':filedownloader' progress will NOT be launched
         * by below code, so please do not worry about performance.
         * @see FileDownloader#init(Context)
         */
        FileDownloader.setupOnApplicationOnCreate((Application) DemoApplication.CONTEXT)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();

        // 这是只是为了全局监控。如果你有需求需要全局监控（比如用于打点/统计）可以使用这个方式，如果没有类似需求就不需要
        // 如果你有这个需求，实现FileDownloadMonitor.IMonitor接口，也使用FileDownloadMonitor.setGlobalMonitor
        // 注册进去即可
        // You do not have to add below code to your project only if you need monitor the global
        // FileDownloader Engine for statistic or others
        // If you have such requirement, just implement FileDownloadMonitor.IMonitor, and register it
        // use FileDownloadDownloader.setGlobalMonitor the same as below code.
        FileDownloadMonitor.setGlobalMonitor(GlobalMonitor.getImpl());
    }

    public void onClickTasksManager(final View view) {
        startActivity(new Intent(this, TasksAudioManagerActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind and stop service manually if idle
        FileDownloader.getImpl().unBindServiceIfIdle();
        FileDownloadMonitor.releaseGlobalMonitor();
    }


}
