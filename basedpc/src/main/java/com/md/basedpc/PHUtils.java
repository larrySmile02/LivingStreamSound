package com.md.basedpc;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by ryan
 *
 * @date 2019/11/8.
 * description：
 */
public class PHUtils {
    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        boolean b = false;
        if (str == null || "".equals(str) || "null".equals(str) || "\"null\"".equals(str)) {
            b = true;
        }
        return b;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * 判断对象是否是数字
     *
     * @param value
     * @return
     */
    public static boolean isInteger(Object value) {
        if (value == null)
            return false;
        if (value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Number)
            return true;

        String v = value.toString();
        if (v != null && v.length() > 0) {
            for (int i = v.length(); --i >= 0; ) {
                int chr = v.charAt(i);
                if (chr < 48 || chr > 57)
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 根据传入的日期格式,格式化date时间
     *
     * @param time
     * @param f
     * @return
     */
    public static String formatDataCustom(Date time, String f) {
        if (isEmpty(time))
            return "-";
        SimpleDateFormat format = new SimpleDateFormat(f);
        try {
            return format.format(time);
        } catch (Exception e) {
            return "-";
        }
    }

    /***
     * 将时间戳转换为时间
     * @param time
     * @param format
     * @return
     */
    public static String stampToDate(long time, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String time_Date = format1.format(new Date(time));
        return time_Date;

    }

    /**
     * 获取h5协议参数json对象
     *
     * @param p
     * @return
     */
    public static JSONObject getProtoPrm(String p) {
        JSONObject res = new JSONObject();
        try {
            res = new JSONObject(p);
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * 根据h5参数名获取协议参数值
     *
     * @param p
     * @param key
     * @return
     */
    public static String getProtoPrm(String p, String key) {
        String res = "";
        try {
            JSONObject j = new JSONObject(p);
            res = j.optString(key, "");
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * 获取H5参数（int）
     *
     * @param p
     * @param key
     * @return
     */
    public static int getProtoPrmInt(String p, String key) {
        int res = 0;
        try {
            JSONObject j = new JSONObject(p);
            res = j.optInt(key, 0);
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * 手机短震动
     *
     * @param context
     */
    public static void vibratorShot(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        v.vibrate(80);
    }

    /**
     * 手机长震动
     *
     * @param context
     */
    public static void vibratorLong(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    /**
     * 手机震动自定义时间
     *
     * @param context
     * @param time    毫秒
     */
    public static void vibratorCustom(Context context, long time) {
        Vibrator v = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    /**
     * 隐藏键盘
     *
     * @param mcontext
     * @param v
     */
    public static void hideSystemKeyBoard(Context mcontext, View v) {
        InputMethodManager imm = (InputMethodManager) (mcontext)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     *
     * @param mContent
     * @param v
     */
    public static void showSystemKeyBoard(final Context mContent, final View v) {
        if (v != null) {
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) mContent.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        v.requestFocus();
                        if (v instanceof EditText) {
                            ((EditText) v).setSelection(((EditText) v).getText().toString().length());
                        }
                        imm.showSoftInput(v, 0);
                    }
                }
            }, 200);

        }
    }

    private static SimpleDateFormat getSimpleDateFormat(String strFormat) {
        if (strFormat != null && !"".equals(strFormat.trim())) {
            return new SimpleDateFormat(strFormat);
        } else {
            return new SimpleDateFormat();
        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        return getSimpleDateFormat(format).format(date);
    }

    /**
     * 格式化时间 超过9999显示1.0w
     *
     * @param count
     * @return
     */
    public static String formatNumberWan(Object count) {
        String wan = count.toString();
        if (isInteger(count)) {
            String c = new BigDecimal(count.toString()).toString();
            if (c.contains(".")) {
                if (c.substring(0, c.indexOf(".")).length() > 4) {
                    c = c.substring(0, c.indexOf("."));
                    double d = Integer.parseInt(c) / 10000.0;
                    final DecimalFormat formater = new DecimalFormat();
                    formater.setMaximumFractionDigits(1);
                    formater.setGroupingSize(0);
                    formater.setRoundingMode(RoundingMode.FLOOR);
                    wan = formater.format(d) + "w";
                } else {
                    wan = c;
                }
            } else {
                if (Integer.parseInt(c) > 9999) {
                    double d = Integer.parseInt(c) / 10000.0;
                    wan = String.format("%.1f", d) + "w";
                } else {
                    wan = c;
                }
            }
        }
        return wan;
    }

    /**
     * 格式化时间 超过999显示1.0k
     *
     * @param count
     * @return
     */
    public static String formatNumberThousand(Object count) {
        String wan = count.toString();
        if (isInteger(count)) {
            String c = new BigDecimal(count.toString()).toString();
            if (c.contains(".")) {
                c = c.substring(0, c.indexOf("."));
                int length = c.length();
                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(1);
                format.setGroupingSize(0);
                format.setRoundingMode(RoundingMode.FLOOR);
                if (length == 4) {
                    double d = Integer.parseInt(c) / 1000.0;
                    wan = format.format(d) + "k";
                } else if (length > 4) {
                    double d = Integer.parseInt(c) / 10000.0;
                    wan = format.format(d) + "w";
                } else {
                    wan = c;
                }
            } else {
                int num = Integer.parseInt(c);
                if (num > 999 && num <= 9999) {
                    double d = num / 1000.0;
                    wan = String.format("%.1f", d) + "k";
                } else if (num > 9999 && num < 9999999) {
                    double d = num / 10000.0;
                    wan = String.format("%.1f", d) + "w";
                } else if (num >= 9999999) {
                    wan = "999w+";
                } else {
                    wan = c;
                }
            }
        }
        return wan;
    }

//    /**
//     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
//     *
//     * @param context Context
//     * @param time    需要格式化的时间 如"2014-07-14 19:01:45"
//     * @return time为null，或者时间格式不匹配，输出空字符""
//     */
//    public static String formatDisplayTime(Context context, String time) {
//        return formatDisplayTime(context, time, "yyyy-MM-dd HH:mm:ss");
//    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param context Context
     * @param time    需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *                <p/>如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
//    public static String formatDisplayTime(Context context, String time, String pattern) {
//        String display = "";
//        int tMin = 60 * 1000;
//        int tHour = 60 * tMin;
//        int tDay = 24 * tHour;
//
//        if (time != null) {
//            try {
//                Date tDate = new SimpleDateFormat(pattern).parse(time);
//                Date today = new Date();
//                //年
//                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
//                //今天的日期
//                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
//                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
//                //昨天
//                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime() - tDay);
//                //前天
//                Date beforeYes = new Date(yesterday.getTime() - tDay);
//                //大前天
//                Date beforeYes1 = new Date(beforeYes.getTime() - tDay);
//                if (tDate != null) {
//                    SimpleDateFormat halfDf = new SimpleDateFormat("MM-dd HH:mm");
//                    long dTime = today.getTime() - tDate.getTime();
//                    if (tDate.before(thisYear)) {
//                        display = new SimpleDateFormat("yyyy-MM-dd").format(tDate);
//                    } else {
//                        if (dTime < tMin) {
//                            // display = context.getString(R.string.common_yxt_lbl_datajust);
//                            display = 1 + context.getString(R.string.common_yxt_lbl_minutesago);
//                        } else if (dTime < tHour) {
//                            display = (int) Math.ceil(dTime / tMin) + context.getString(R.string.common_yxt_lbl_minutesago);
//                        } else if (dTime < tDay && tDate.after(yesterday)) {
//                            display = (int) Math.ceil(dTime / tHour) + context.getString(R.string.common_yxt_lbl_hoursago);
//                        } else if (tDate.after(beforeYes1) && tDate.before(yesterday)) {
//                            display = (int) Math.ceil(dTime / tDay) + context.getString(R.string.common_yxt_lbl_daysago);
//                        } else {
//                            display = halfDf.format(tDate);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("PHUtils", e.getMessage());
//            }
//        }
//        return display;
//    }

    // 判断程序是否在后台
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        return PHStringUtils.equals(context.getPackageName(), getProcessName(context));
    }

    public static String injectIsParams(String url) {
        if (url != null && !url.contains("xxx=")) {
            if (url.contains("?")) {
                return url + "&xxx=" + System.currentTimeMillis();
            } else {
                return url + "?xxx=" + System.currentTimeMillis();
            }
        } else {
            return url;
        }
    }

    public static Drawable tintDrawable(Drawable drawable, int colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(colors));
        return wrappedDrawable;
    }

    public static String getParamByKey(String url, String key) {
        String ret = "";
        if (url != null && key != null && url.contains("?")) {
            String[] s1 = url.split("\\?");
            if (s1.length > 1) {
                String s2 = s1[1];
                if (s2.contains(key)) {
                    if (s2.contains("&")) {
                        String[] s3 = s2.split("&");
                        for (int i = 0; i < s3.length; i++) {
                            String[] s4 = s3[i].split("=");
                            if (s4.length > 1 && s4[0].equals(key)) {
                                ret = s4[1];
                                break;
                            }
                        }
                    } else {
                        if (s2.contains("=")) {
                            String[] s5 = s2.split("=");
                            if (s5.length > 1) {
                                ret = s5[1];
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static HashMap<String, String> getUrlParams(String url) {
        HashMap<String, String> result = new HashMap<>();

        if (url != null) {
            int index = url.indexOf("?");
            if (index > 0 && url.length() > index) {
                String argStr = url.substring(index + 1);
                String[] args = argStr.split("&");
                for (String arg : args) {
                    String[] params = arg.split("=");

                    if (params.length > 1)
                        result.put(params[0], params[1]);
                }
            }
        }
        return result;
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return "";
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return "";
            }
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return appInfo.loadLabel(packageManager).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
