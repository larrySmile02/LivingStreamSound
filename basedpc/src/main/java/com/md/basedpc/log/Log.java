package com.md.basedpc.log;

import com.md.basedpc.common.PHThreadPool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with AndroidStudio3.0.
 * Description:
 * User: wanggq
 * Date: 2017-10-31
 * Time: 下午2:29
 */
public class Log {

    public static void i(String message) {
        if (LogUtils.isNeedLog) {
            String[] infos = getAutoJumpLogInfos();
            PHThreadPool.getInstance().execute(() -> LogUtils.i(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
        }
    }

    public static void v(String message) {
        if (LogUtils.isNeedLog) {
            String[] infos = getAutoJumpLogInfos();
            PHThreadPool.getInstance().execute(() -> LogUtils.v(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
        }
    }

    public static void e(String message) {
        if (LogUtils.isNeedLog) {
            String[] infos = getAutoJumpLogInfos();
            PHThreadPool.getInstance().execute(() -> LogUtils.e(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
        }
    }

    public static void d(Object message) {
        if (LogUtils.isNeedLog) {
            String[] infos = getAutoJumpLogInfos();
            PHThreadPool.getInstance().execute(() -> LogUtils.d(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
        }
    }


    public static void w(String message) {
        if (LogUtils.isNeedLog) {
            String[] infos = getAutoJumpLogInfos();
            PHThreadPool.getInstance().execute(() -> LogUtils.w(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
        }
    }

    public static void httpLog(String message) {
        String[] infos = getAutoJumpLogInfos();
        PHThreadPool.getInstance().execute(() -> LogUtils.httpLog(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
    }

    public static void exceptionLog(Throwable throwable) {
        String[] infos = getAutoJumpLogInfos();
        PHThreadPool.getInstance().execute(() -> LogUtils.exceptionLog(infos[3], throwable));
    }

    public static void local(String message) {
        String[] infos = getAutoJumpLogInfos();
        PHThreadPool.getInstance().execute(() -> LogUtils.local(infos[3], "ph_log[" + infos[0] + "]" + infos[2] + infos[1] + " : " + message));
    }

    private static String[] getAutoJumpLogInfos() {
        String[] infos = new String[]{"", "", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            LogUtils.e("Log", "Stack is too shallow!!!");
            return infos;
        } else {
            infos[0] = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            infos[1] = "[" + elements[4].getMethodName() + "()]";
            infos[2] = "(" + elements[4].getFileName() + ":"
                    + elements[4].getLineNumber() + ") ";
            try {
                infos[3] = elements[4].getFileName().substring(0, elements[4].getFileName().lastIndexOf("."));
            } catch (Exception e) {
                infos[3] = "unknown";
            }
            return infos;
        }
    }

    private static SimpleDateFormat getSimpleDateFormat(String strFormat) {
        if (strFormat != null && !"".equals(strFormat.trim())) {
            return new SimpleDateFormat(strFormat);
        } else {
            return new SimpleDateFormat();
        }
    }

    private static String formatDate(Date date, String format) {
        return getSimpleDateFormat(format).format(date);
    }
}
