package com.md.basedpc.common;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create by 朱大可 on 2020年03月30日16:22:13
 * 线程池管理中心
 */
public class PHThreadPool {
    private static ExecutorService THREAD_POOL_EXECUTOR;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(8);
    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "MangoTask #" + mCount.getAndIncrement());
        }
    };

    private void initThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory, new RejectedHandler()) {
            @Override
            public void execute(Runnable command) {
                super.execute(command);

            }
        };
        //允许核心线程空闲超时时被回收
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    private class RejectedHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //可在这里做一些提示用户的操作
        }
    }

    private static PHThreadPool instance;

    private PHThreadPool() {
        initThreadPool();
    }

    public static PHThreadPool getInstance() {
        if (instance == null) {
            synchronized (PHThreadPool.class) {
                if (instance == null) {
                    instance = new PHThreadPool();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable command) {
        THREAD_POOL_EXECUTOR.execute(command);
    }

    public void executeUIThread(Runnable runnable) {
        if (uiHandler == null) {
            return;
        }
        if ("main".equals(Thread.currentThread().getName())) {
            runnable.run();
        } else {
            uiHandler.post(new ReleaseRunnable(runnable));
        }
    }

    public void executeUIThread(Runnable runnable, long delayMillis) {
        if (uiHandler == null) {
            return;
        }

        uiHandler.postDelayed(new ReleaseRunnable(runnable), delayMillis);
    }

    private class ReleaseRunnable implements Runnable {
        private Runnable mRunnable;

        private ReleaseRunnable(Runnable runnable) {
            this.mRunnable = runnable;
        }

        @Override
        public void run() {
            if (this.mRunnable != null) {
                try {
                    this.mRunnable.run();
                } catch (Exception var2) {
                    Log.e("threadUtils", var2.getMessage());
                }
            }
        }
    }
}
