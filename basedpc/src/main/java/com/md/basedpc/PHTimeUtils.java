package com.md.basedpc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ryan
 *
 * @date 2019/10/25.
 * description：时间格式化类
 */
public class PHTimeUtils {
    /**
     * 获得当前时间
     */
    public static String getCurrentTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 转义时间
     *
     * @param value
     * @return
     */
    public static Date getTimeFromStr(String value) {
        SimpleDateFormat inFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.CHINA);
        try {
            return inFormatter.parse(value);
        } catch (Exception e) {
            SimpleDateFormat inFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                return inFormatter2.parse(value);
            } catch (Exception ee) {
                SimpleDateFormat inFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                try {
                    return inFormatter3.parse(value);
                } catch (Exception eee) {
                    try {
                        return inFormatter2.parse(value);
                    } catch (Exception ee33) {
                        SimpleDateFormat inFormatter4 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                        try {
                            return inFormatter4.parse(value);
                        } catch (Exception eee33) {
                            return new Date();
                        }
                    }
                }
            }
        }
    }

    /***
     * 获取两个日期的差值
     *
     * @param date 前一个日期 yyyy-mm-dd
     * @return long 相差天数
     */
    public static long timeDiff(String date) {
        return timeDiffCustom(date, "yyyy-MM-dd HH:mm:ss");
    }

    /***
     * 获取两个日期的差值
     *
     * @param date 前一个日期 yyyy-mm-dd
     * @return long 相差天数
     */
    public static long timeDiffCustom(String date, String f) {
        DateFormat df = new SimpleDateFormat(f);
        long days = 0;
        try {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH) + 1;
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            Date d1 = df.parse(date + " 00:00:00");
            Date d2 = df.parse(mYear + "-" + mMonth + "-" + mDay + " 00:00:00");
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是毫秒级别
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
        }
        return days;
    }

    /***
     * 获取两个日期的差值
     *
     * @param d11 前一个日期 yyyy-mm-dd
     * @param d22 后一个日期 yyyy-mm-dd
     * @return long 相差天数
     */
    public static long timeDiff(String d11, String d22) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long days = 0;
        try {
            Date d1 = df.parse(d11 + " 00:00:00");
            Date d2 = df.parse(d22 + " 00:00:00");
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

        }
        return days;
    }

    /**
     * 更新倒计时的天时分秒view显示的数据
     * 例（23:10:15）
     * @param time
     */
    public static String updateTimeBeforeExamStart(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = time / dd;
        long hour = (time - day * dd) / hh;
        long minute = (time - day * dd - hour * hh) / mi;
        long second = (time - day * dd - hour * hh - minute * mi) / ss;

        String timeStr = "";
        timeStr += (hour + day * 24) < 10 ? "0" + hour :hour + day * 24;
        timeStr += ":";
        timeStr += minute < 10 ? "0" + minute : "" + minute;
        timeStr += ":";
        timeStr += second < 10 ? "0" + second : "" + second;
        return timeStr;
    }

    /**
     * 更新倒计时的分秒view显示的数据
     * 例（10分15秒）
     * @param time
     */
    public static String updateTimeBeforeCountDown(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = time / dd;
        long hour = (time - day * dd) / hh;
        long minute = (time - day * dd - hour * hh) / mi;
        long second = (time - day * dd - hour * hh - minute * mi) / ss;

        String timeStr = "";
        timeStr += minute < 10 ? "0" + minute : "" + minute;
        timeStr += "%s";
        timeStr += second < 10 ? "0" + second : "" + second;
        return timeStr + "%s";
    }
}
