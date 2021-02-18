package com.md.basedpc.log;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.md.basedpc.PHTimeUtils;
import com.md.basedpc.common.PHThreadPool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * create by wanggq on 2020年03月05日17:58:42
 * 日志工具类
 */
public class LogUtils {
    public static int LEVEL = 0;
    public final static int V = 1;
    public final static int W = 2;
    public final static int I = 3;
    public final static int D = 4;
    public final static int E = 5;
    private final static int P = Integer.MAX_VALUE;
    private static final String _L = "[";
    private static final String _R = "]";
    public static boolean isNeedLog = true;
    public static boolean isNeedLogCatV = true;
    public static boolean isNeedLogCatI = true;
    public static boolean isNeedLogCatD = true;
    public static boolean isNeedLogCatW = true;
    public static boolean isNeedLogCatE = true;
    private static String LOG_FILE_DIR = "";//打印日志保存路径
    private static String LOG_FILE_FILE = "phoenix.ph";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());//生成一个日期文件名格式的日式格式对象
    private static int LOG_MAX_SIZE = 4 * 1024 * 1024;//单个日志的最大的容量,如果一个日志太大了，打开会影响效率
    public static int maxLogSize = 3500;

    public static void initPath(Context context) {
        LOG_FILE_DIR = context.getExternalCacheDir() + File.separator + "log";
        File p = new File(LOG_FILE_DIR);
        if (!p.exists())
            p.mkdirs();
        PHThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                LogUtils.cleanLogs();
            }
        });
    }

    /**
     * 清楚日志 1
     */
    public static void cleanLogs() {
        File lp = new File(LOG_FILE_DIR);
        if (lp.exists()) {
            try {
                File[] logs = lp.listFiles();
                String nowTime = PHTimeUtils.getCurrentTime();
                if (logs != null && logs.length > 0) {
                    for (int i = 0; i < logs.length; i++) {
                        File log = logs[i];
                        if (!log.isDirectory()) {
                            String logName = log.getName();
                            if (logName.indexOf("_") > 0) {
                                logName = logName.substring(6, logName.length() - 13).replace("_", "-");
                                long diff = Math.abs(PHTimeUtils.timeDiff(nowTime, logName));
//                                w("log", logName + " " + diff);
                                if (diff > 30)
                                    log.delete();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e("log", e.getMessage());
            }
        }
    }

    public static final void i(String Tag, String message) {
        if (LEVEL <= I && isNeedLog && isNeedLogCatI) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.i(Tag, sub.trim());
            }
        }
    }

    public static final void e(String Tag, String message) {
        if (LEVEL <= E && isNeedLog && isNeedLogCatE) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.e(Tag, sub.trim());
            }

        }
    }

    public static final void w(String Tag, String message) {
        if (LEVEL <= W && isNeedLog && isNeedLogCatW) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }
                index += maxLogSize;
                Log.w(Tag, sub.trim());
            }
        }
    }

    public static final void v(String Tag, String message) {
        if (LEVEL <= V && isNeedLog && isNeedLogCatV) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.v(Tag, sub.trim());
            }
        }
    }

    public static final void d(String Tag, String message) {
        if (LEVEL <= D && isNeedLog && isNeedLogCatD) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.d(Tag, sub.trim());
            }
        }
    }

    public static final void httpLog(String Tag, String message) {
        if (isNeedLog) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.i(Tag, sub.trim());
            }
        }
        // YXTLogSystem.getInstance().write(message,3);
    }

    public static final void exceptionLog(String Tag, Throwable throwable) {
        //"ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " +
        throwable.printStackTrace();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTraceString = sw.toString();
        // YXTLogSystem.getInstance().write(stackTraceString,1);
    }

    public static final void local(String Tag, String message) {
        if (isNeedLog) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }
                index += maxLogSize;
                Log.d(Tag, sub.trim());
            }
        }
        // YXTLogSystem.getInstance().write(message,2);
    }

    public static final void localI(String Tag, String message) {
        if (LEVEL <= I && isNeedLog && isNeedLogCatI) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.i(Tag, sub.trim());
            }
        }
        LogFile.writeLog(message);
    }

    public static final void localE(String Tag, String message) {
        if (LEVEL <= E && isNeedLog && isNeedLogCatE) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.e(Tag, sub.trim());
            }

        }
        LogFile.writeLog(message);
    }

    public static final void localW(String Tag, String message) {
        if (LEVEL <= W && isNeedLog && isNeedLogCatW) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }
                index += maxLogSize;
                Log.w(Tag, sub.trim());
            }
        }
        LogFile.writeLog(message);
    }

    public static final void localV(String Tag, String message) {
        if (LEVEL <= V && isNeedLog && isNeedLogCatV) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.v(Tag, sub.trim());
            }
        }
        LogFile.writeLog(message);
    }

    public static final void localD(String Tag, String message) {
        if (LEVEL <= D && isNeedLog && isNeedLogCatD) {
            int index = 0;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLogSize) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, index + maxLogSize);
                }

                index += maxLogSize;
                Log.d(Tag, sub.trim());
            }
        }
        LogFile.writeLog(message);
    }

    /**
     * 获取一个Tag打印标签
     *
     * @param msg
     * @return
     * @since JDK 1.5
     */
    private static String getTag(String msg) {
        if (msg != null) {
            // since jdk 1.5
            if (Thread.currentThread().getStackTrace().length > 0) {
                String name = Thread.currentThread().getStackTrace()[0]
                        .getClassName();
                return _L + name.substring(name.lastIndexOf(".") + 1) + _R;
            }
        }
        return _L + "null" + _R;
    }


    /**
     * 获取时间字符串
     */
    private static String getCurrTimeDir() {
        return sdf.format(new Date());
    }

    /**
     * LOG定制类。 输出LOG到日志。
     */
    private static class LogFile {
        /**
         * 内部强制性打印使用。区分print ， 是为了解决无限循环打印exception
         */
        private static void print(String msg) {
            if (LEVEL <= P) {
                int index = 0;
                String sub;
                while (index < msg.length()) {
                    // java的字符不允许指定超过总的长度end
                    if (msg.length() <= index + maxLogSize) {
                        sub = msg.substring(index);
                    } else {
                        sub = msg.substring(index, maxLogSize);
                    }

                    index += maxLogSize;
                    Log.e(getTag(msg), sub.trim());
                }
            }
        }

        /**
         * 打印信息
         *
         * @param message
         */
        public static synchronized void writeLog(String message) {
            File f = getFile();
            if (f != null) {
                try {
                    FileWriter fw = new FileWriter(f, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.append("\n");
                    bw.append(message);
                    bw.flush();
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    print("writeLog error, " + e.getMessage());
                }
            } else {
                print("writeLog error, due to the file dir is error");
            }
        }

        /**
         * 获取文件
         *
         * @return
         */
        private static File getFile() {
            if ("".equals(LOG_FILE_DIR)) {
                return null;
            }
            synchronized (LogUtils.class) {
                File f = new File(LOG_FILE_DIR + File.separator + LOG_FILE_FILE);
                if (!f.exists()) {
                    try {
                        f.getParentFile().mkdirs();
                        f.createNewFile();
                    } catch (IOException e) {
                        Log.e("log", e.getMessage());
                    }
                } else {
                    if (f.length() > LOG_MAX_SIZE) {
                        f.renameTo(new File(LOG_FILE_DIR + "/phoenix" + getCurrTimeDir() + ".ph"));
                        try {
                            f.getParentFile().mkdirs();
                            f.createNewFile();
                        } catch (IOException e) {
                            Log.e("log", e.getMessage());
                        }
                    }
                }
                return f;
            }
        }
    }

//    /**
//     * 上传日志
//     *
//     * @param userName
//     */
//    public static void sendLocalLogToServer(String userName, int type) {
//        PHThreadPool.getInstance().execute(() -> {
//            try {
//                File[] fs = new File(LOG_FILE_DIR).listFiles();
//                File[] fs1 = new File("/data/data/" + AppManager.getAppManager().getNowContext().getPackageName() + "/databases").listFiles();
//                File[] fs2 = new File("/data/data/" + AppManager.getAppManager().getNowContext().getPackageName() + "/shared_prefs").listFiles();
//                File[] fs3 = new File(AppManager.getAppManager().getNowContext().getExternalCacheDir() + File.separator + AppManager.getAppManager().getNowContext().getPackageName() + "/cache/IM/log").listFiles();
//
//                if (fs == null)
//                    fs = new File[0];
//                if (fs1 == null)
//                    fs1 = new File[0];
//                if (fs2 == null)
//                    fs2 = new File[0];
//                if (fs3 == null)
//                    fs3 = new File[0];
//                File[] all = new File[fs.length + fs1.length + fs2.length + fs3.length];
//                int t = 0;
//                if (fs != null && fs.length > 0)
//                    for (int i = 0; i < fs.length; i++) {
//                        all[t] = fs[i];
//                        t++;
//                    }
//                if (fs1 != null && fs1.length > 0)
//                    for (int i = 0; i < fs1.length; i++) {
//                        all[t] = fs1[i];
//                        t++;
//                    }
//                if (fs2 != null && fs2.length > 0)
//                    for (int i = 0; i < fs2.length; i++) {
//                        all[t] = fs2[i];
//                        t++;
//                    }
//                if (fs3 != null && fs3.length > 0)
//                    for (int i = 0; i < fs3.length; i++) {
//                        all[t] = fs3[i];
//                        t++;
//                    }
//                List<File> f = new ArrayList<>();
//                for (File file : all) {
//                    if (file == null || !file.exists()) {
//                        continue;
//                    }
//                    f.add(file);
//                }
//                String pathZip = BaseConstants.DEFAULT_APP_ROOT_FOLDER + getCurrTimeDir() + "_yxt_log_" + Utils.getAppBaseVersionCode() + ".zip";
//                if (Build.VERSION.SDK_INT <= 22) {
//                    Utils.zipFileList(f, pathZip);
//                } else {
//                    Utils.zipFileList(f, pathZip, "pwdasdwx");
//                }
//                Message m = new Message();
//                m.what = 1;
//                Bundle b = new Bundle();
//                b.putString("url", pathZip);
//                b.putInt("type", type);
//                b.putString("userName", userName);
//                m.setData(b);
//                mHandle.sendMessageDelayed(m, 1000);
//            } catch (Exception e) {
//                Log.e(getTag(e.getMessage()), e.getMessage());
//            }
//        });
//    }

//    private static Handler mHandle = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    String path = msg.getData().getString("url");
//                    int type = msg.getData().getInt("type");
//                    String userName = msg.getData().getString("userName");
//                    String url = BaseConstants.YXT_LOG_INFO_URL + "?fileName=" + "Android" + type + "(" + (Build.BRAND + ":" + Build.MODEL) + ")_" + userName + "_" + (new File(path)).getName();
//                    YXTHttpUtils.getInstance().uploadFileProgress(url, url, new File(path), new YXTHttpResponseHandler() {
//                        @Override
//                        public void onFinish() {
//                            super.onFinish();
//                            Message m = new Message();
//                            m.what = 2;
//                            Bundle b = new Bundle();
//                            b.putString("url", path);
//                            m.setData(b);
//                            mHandle.sendMessageDelayed(m, 3000);
//                        }
//                    });
//                    break;
//                case 2:
//                    String path1 = msg.getData().getString("url");
//                    Utils.deleteFile(path1);
//                    break;
//            }
//            return false;
//        }
//    });
}
