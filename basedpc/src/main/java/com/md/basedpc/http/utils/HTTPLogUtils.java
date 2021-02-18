package com.md.basedpc.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.md.basedpc.application.AppManager;
import com.md.basedpc.common.PHThreadPool;
import com.md.basedpc.log.Log;
import com.md.basedpc.log.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.wasabeef.glide.transformations.internal.Utils;

/**
 * Created by ryan
 *
 * @date 2019/11/21.
 * description：网络请求日志工具
 */
public class HTTPLogUtils {

    /**
     * 写入并打印网络请求日志
     *
     * @param status
     * @param header
     * @param prm
     * @param url
     * @param body
     * @param type
     */
    static void writeLog(int status, Map header, String prm, String url, String body, String type) {
        PHThreadPool.getInstance().execute(() -> {
            if ((url.contains("log-qida") || url.contains("log-common") || url.contains("aip.baidubce.com/rest/2.0/face")) && LogUtils.isNeedLog)
                return;
            try {
                StringBuilder sb = new StringBuilder();
                StringBuilder p = new StringBuilder();
                if (header != null) {
                    Set<String> stringSet = header.keySet();
                    sb.append("\r\n{\r\n");
                    for (int i = 0; i < stringSet.size(); i++) {
                        String s = (String) stringSet.toArray()[i];
                        String v = "";
                        if (header.get(s) instanceof List && ((List) header.get(s)).size() > 0) {
                            v = (String) ((List) header.get(s)).get(0);
                        } else {
                            v = header.get(s) != null ? ((String) header.get(s)) : "";
                        }
//                        if (i == stringSet.size() - 1)
//                            sb.append("\t\"" + s + "\":" + (!Utils.isInteger(v) ? "\"" + v + "\"" : v) + "\r\n");
//                        else
//                            sb.append("\t\"" + s + "\":" + (!com.yxt.basic_frame.utils.Utils.isInteger(v) ? "\"" + v + "\"" : v) + ",\r\n");
                    }
                    sb.append("}");
                }
                if (prm != null) {
                    final Object ex = parseResponse(prm);
                    p.append(ex.toString());
                }
                String bodyType = "JSONObject";
                try {
                    final Object ex = parseResponse(body);
                    if (ex == null) {
                    } else if (ex instanceof JSONObject) {
                        bodyType = "JSONObject";
                    } else if (ex instanceof JSONArray) {
                        bodyType = "JSONArray";
                    } else {
                        bodyType = "String";
                    }
                } catch (final JSONException var2) {
                    Log.e(var2.getMessage());
                    bodyType = "未知";
                }
                String logString = "\r\nhttp type:" + type + " url:" + url + "\r\nhttp send:" + (p.toString().replace(",", ",\r\n\t").replace("{", "\r\n{\r\n\t").replace("}", "\r\n}")) + "\r\nhttp head:" + sb.toString() + "\r\nhttp Result[statusCode:" + status + "](" + bodyType + "):" + body.replace(",", ",\r\n\t").replace("{", "\r\n{\r\n").replace("}", "\r\n}");
                Log.httpLog(logString);
            } catch (Exception e) {
                Log.exceptionLog(e);
            }
        });
    }

    /**
     * 解析请求参数
     *
     * @param responseString
     * @return
     * @throws JSONException
     */
    private static Object parseResponse(String responseString) throws JSONException {
        if (null == responseString) {
            return null;
        } else {
            Object result = null;
            String jsonString = responseString;
            jsonString = jsonString.trim();
            if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                result = (new JSONTokener(jsonString)).nextValue();
            }

            if (result == null) {
                result = jsonString;
            }
            return result;
        }
    }

    /**
     * 获取手机信息
     *
     * @return
     */
    public static String getPhoneAndUserInfo() {
        Context mContext = AppManager.getAppManager().getNowContext();
        SIMCardInfo simInfo = new SIMCardInfo(mContext);
        String mobile_desc = android.os.Build.MODEL;
        String mobile_sdk = android.os.Build.VERSION.SDK;
        String mobile_f = android.os.Build.BRAND;
        String mobile_sys = android.os.Build.VERSION.RELEASE;
        // String version_number = com.yxt.basic_frame.utils.Utils.getAppBaseVersion();

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        String userPhoneNum = "没有权限获取";
        String providersName = "没有权限获取";
        try {
            userPhoneNum = simInfo.getNativePhoneNumber();
            providersName = simInfo.getProvidersName();
        } catch (Exception e) {
            Log.e(e.getMessage());
        }

        String phoneInformation = "手机型号:(" + mobile_f + ")" + mobile_desc +
                "\n SDK版本:" + mobile_sdk +
                "\n 系统版本:" + mobile_sys +
                "\n 软件版本:" + 1.0 +
                "\n CPU:" + getCpuName() +
                "\n 电话号码:" + userPhoneNum +
                "\n 运营商:" + providersName +
                "\n 联网方式:" + (info == null ? "未知" : info.getTypeName()) + "(" + (info == null ? "未知" : info.getExtraInfo()) + ")\n";

        return phoneInformation;
    }

    /**
     * 获取CPU名字
     *
     * @return
     */
    private static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (IOException e) {
            Log.e(e.getMessage());
        }
        return "未知";
    }

}
