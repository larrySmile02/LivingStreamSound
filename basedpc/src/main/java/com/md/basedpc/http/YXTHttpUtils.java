package com.md.basedpc.http;

import android.text.TextUtils;

import com.md.basedpc.PHUtils;
import com.md.basedpc.R;
import com.md.basedpc.application.AppManager;
import com.md.basedpc.http.Interface.YXTHttpResponseHandler;
import com.md.basedpc.http.cookie.DefaultCookiesManager;
import com.md.basedpc.http.model.ACacheModel;
import com.md.basedpc.http.model.HttpInfo;
import com.md.basedpc.http.utils.CacheType;
import com.md.basedpc.http.utils.CountingFileRequestBody;
import com.md.basedpc.http.utils.HTTPLogUtils;
import com.md.basedpc.http.utils.HttpCacheUtil;
import com.md.basedpc.http.utils.HttpJsonCommonParser;
import com.md.basedpc.http.utils.HttpLibUtils;
import com.md.basedpc.http.utils.OkHttpCallBack;
import com.md.basedpc.http.utils.OkHttpFileDowloadCallBack;
import com.md.basedpc.log.Log;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：网络请求基础类
 */
public class YXTHttpUtils {
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");  // 请求体是json
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8"); // Post方式提交流 、Post方式提交文件
    private static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream"); // Post方式提交流 、Post方式提交文件

    private volatile static YXTHttpUtils mInstance;
    public static final int NO_NETWORK = -999;
    private static long DEFAULT_MILLISECONDS = 15000;
    private OkHttpClient mOkHttpClient;
    private Map<String, String> headerMap = new ConcurrentHashMap<>();

    /**
     * 获取http请求头
     *
     * @return Map
     */
    public Map getHeaderMap() {
        return headerMap;
    }

    /**
     * 获取网络请求基础类单例
     *
     * @return HttpUtils
     */
    public static YXTHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (YXTHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new YXTHttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 网络基础类构造,获得okHttpClient
     */
    public YXTHttpUtils() {
        if (mOkHttpClient == null) {
            try {
                OkHttpClient.Builder builderClient = new OkHttpClient.Builder();
                builderClient.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                        .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                        .addInterceptor(new RetryIntercepter(3))
                        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                        .hostnameVerifier((hostname, session) -> true);
                builderClient.readTimeout(600, TimeUnit.SECONDS);
                builderClient.cookieJar(new DefaultCookiesManager());
                mOkHttpClient = builderClient.build();
                mOkHttpClient.dispatcher().setMaxRequests(80);
                mOkHttpClient.dispatcher().setMaxRequestsPerHost(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get方法 带缓存返回
     *
     * @param url
     * @param yxtHttpResponseHandler
     */
    public void get(String url, YXTHttpResponseHandler yxtHttpResponseHandler) {
        get(CacheType.CACHED_ELSE_NETWORK, DEFAULT_MILLISECONDS, url, null, yxtHttpResponseHandler);
    }

    /**
     * get方法 不带缓存返回
     *
     * @param url
     * @param yxtHttpResponseHandler
     */
    public void getNoCache(String url, YXTHttpResponseHandler yxtHttpResponseHandler) {
        HttpCacheUtil.getInstance().cleanCacheByUrl(url);
        get(CacheType.ONLY_NETWORK, DEFAULT_MILLISECONDS, url, null, yxtHttpResponseHandler);
    }

    public void getNoCache(String url, Map<String, Object> params, YXTHttpResponseHandler yxtHttpResponseHandler) {
        HttpCacheUtil.getInstance().cleanCacheByUrl(url);
        get(CacheType.ONLY_NETWORK, DEFAULT_MILLISECONDS, url, params, yxtHttpResponseHandler);
    }

    /**
     * get方法 带缓存返回
     *
     * @param url
     * @param params
     * @param yxtHttpResponseHandler
     */
    public void get(final String url, Map<String, Object> params, YXTHttpResponseHandler yxtHttpResponseHandler) {
        get(CacheType.CACHED_ELSE_NETWORK, DEFAULT_MILLISECONDS, url, params, yxtHttpResponseHandler);
    }

    /**
     * get方法
     *
     * @param cacheType
     * @param connectTimeout
     * @param url
     * @param params
     * @param yxtHttpResponseHandler
     */
    public void get(CacheType cacheType, long connectTimeout, String url, Map<String, Object> params, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        String urlStr = HttpLibUtils.appendParams(url, params);
        Log.httpLog("http type:GET url:" + urlStr);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }

        if (yxtHttpResponseHandler != null) {
            if (cacheType != null && cacheType == CacheType.CACHED_ELSE_NETWORK) {
                Object clientKey = getHeader("clientKey");
                String k = "";
                if (clientKey != null)
                    k = clientKey.toString();
                ACacheModel cacheModel = HttpCacheUtil.getInstance().getACache(k + urlStr);
                if (cacheModel != null) {
                    if (yxtHttpResponseHandler != null) {
                        if (cacheModel.getCode() > 199 && cacheModel.getCode() < 299)
                            yxtHttpResponseHandler.sendSuccessMessage(cacheModel.getCode(), new HttpInfo(null, "", url, true), cacheModel.getResponseBody(), "");
                    }
                }
            }
        }
        if (HttpLibUtils.isUrlWrong(urlStr, yxtHttpResponseHandler)) return;
        Request.Builder builder = new Request.Builder();
        builder.url(urlStr)  // Headers加入
                .tag(urlStr)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap))  // Headers加入
                .get();
        final Request request = builder.build();
        OkHttpClient mOkHttpClient_new = mOkHttpClient.newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .build();
        Call call = mOkHttpClient_new.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(cacheType, null, request, yxtHttpResponseHandler));
    }


    /**
     * post 请求
     *
     * @param url
     * @param paramsEntity           请求体
     * @param yxtHttpResponseHandler
     */
    public void post(String url, Map<String, Object> paramsEntity, YXTHttpResponseHandler yxtHttpResponseHandler) {
        try {
            post(url, HttpJsonCommonParser.getString(paramsEntity), yxtHttpResponseHandler);
        } catch (Exception e) {
            Log.exceptionLog(e);
        }
    }

    /**
     * post 请求
     *
     * @param url
     * @param jsonEntity             请求体
     * @param yxtHttpResponseHandler
     */
    public void post(String url, String jsonEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:POST url:" + url + " send:" + jsonEntity);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .tag(url);
        if (TextUtils.isEmpty(jsonEntity)) {
            builder.post(RequestBody.create(null, new byte[0]));
        } else {
            builder.post(RequestBody.create(JSON, jsonEntity));
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, jsonEntity, request, yxtHttpResponseHandler));
    }

    /**
     * post 请求
     *
     * @param url
     * @param paramsEntity           请求体
     * @param yxtHttpResponseHandler
     */
    public void postWithCache(String url, Map<String, Object> paramsEntity, YXTHttpResponseHandler yxtHttpResponseHandler) {
        try {
            postWithCache(url, HttpJsonCommonParser.getString(paramsEntity), yxtHttpResponseHandler);
        } catch (Exception e) {
            Log.exceptionLog(e);
        }
    }


    /**
     * post 请求
     *
     * @param url
     * @param jsonEntity             请求体
     * @param yxtHttpResponseHandler
     */
    public void postWithCache(String url, String jsonEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:POST url:" + url + " send:" + jsonEntity);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }

        if (yxtHttpResponseHandler != null) {
                Object clientKey = getHeader("clientKey");
                String k = "";
                if (clientKey != null)
                    k = clientKey.toString();
                ACacheModel cacheModel = HttpCacheUtil.getInstance().getACache(k + url);
                if (cacheModel != null) {
                    if (cacheModel.getCode() > 199 && cacheModel.getCode() < 299)
                        yxtHttpResponseHandler.sendSuccessMessage(cacheModel.getCode(), new HttpInfo(null, "", url, true), cacheModel.getResponseBody(), "");
                }
        }

        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .tag(url);
        if (TextUtils.isEmpty(jsonEntity)) {
            builder.post(RequestBody.create(null, new byte[0]));
        } else {
            builder.post(RequestBody.create(JSON, jsonEntity));
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(CacheType.CACHED_ELSE_NETWORK, jsonEntity, request, yxtHttpResponseHandler));
    }
    /**
     * put请求
     *
     * @param url
     * @param paramsEntity           请求体
     * @param yxtHttpResponseHandler
     */
    public void put(String url, Map<String, Object> paramsEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        try {
            put(url, HttpJsonCommonParser.getString(paramsEntity), yxtHttpResponseHandler);
        } catch (Exception e) {
            Log.exceptionLog(e);
        }
    }

    /**
     * put请求
     *
     * @param url
     * @param jsonEntity             请求体
     * @param yxtHttpResponseHandler
     */
    public void put(String url, String jsonEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:PUT url:" + url);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .tag(url);
        if (TextUtils.isEmpty(jsonEntity)) {
            builder.put(RequestBody.create(null, new byte[0]));
        } else {
            builder.put(RequestBody.create(JSON, jsonEntity));
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, jsonEntity, request, yxtHttpResponseHandler));
    }

    /**
     * delete请求
     *
     * @param url
     * @param paramsEntity           请求体
     * @param yxtHttpResponseHandler
     */
    public void delete(String url, Map<String, Object> paramsEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        try {
            delete(url, HttpJsonCommonParser.getString(paramsEntity), yxtHttpResponseHandler);
        } catch (Exception e) {
            Log.exceptionLog(e);
        }
    }

    /***
     * delete请求
     * @param url
     * @param jsonEntity                请求体
     * @param yxtHttpResponseHandler
     */
    public void delete(String url, String jsonEntity, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:DELETE url:" + url);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap))  // Headers加入
                .tag(url);
        if (TextUtils.isEmpty(jsonEntity)) {
            builder.delete();
        } else {
            builder.delete(RequestBody.create(JSON, jsonEntity));
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, jsonEntity, request, yxtHttpResponseHandler));
    }


    /**
     * postForm 请求
     *
     * @param url
     * @param formBody
     * @param yxtHttpResponseHandler
     */
    public void postForm(String url, Map<String, String> formBody, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:POST FORM url:" + url);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        //创建一个FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder();
        if (formBody != null) {
            for (Map.Entry<String, String> m : formBody.entrySet()) {
                builder.add(m.getKey(), m.getValue());
            }
        }
        //生成表单实体对象
        RequestBody form = builder.build();
        //创建一个请求
        Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap))  // Headers加入
                .post(form)
                .build();
        //创建一个Call
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }

    /**
     * putForm 请求
     *
     * @param url
     * @param formBody
     * @param yxtHttpResponseHandler
     */
    public void putForm(String url, Map<String, String> formBody, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:PUT FORM url:" + url);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        //创建一个FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder();
        if (formBody != null) {
            for (Map.Entry<String, String> m : formBody.entrySet()) {
                builder.add(m.getKey(), m.getValue());
            }
        }
        //生成表单实体对象
        RequestBody form = builder.build();
        //创建一个请求
        Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap))  // Headers加入
                .put(form)
                .build();
        //创建一个Call
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }

    /**
     * deleteForm 请求
     *
     * @param url
     * @param formBody
     * @param yxtHttpResponseHandler
     */
    public void deleteForm(String url, Map<String, String> formBody, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        Log.httpLog("http type:DELETE FORM url:" + url);
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        //创建一个FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder();
        if (formBody != null) {
            for (Map.Entry<String, String> m : formBody.entrySet()) {
                builder.add(m.getKey(), m.getValue());
            }
        }
        //生成表单实体对象
        RequestBody form = builder.build();
        //创建一个请求
        Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .delete(form)
                .build();
        //创建一个Call
        Call call = mOkHttpClient.newCall(request);
        // 异步
        call.enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }


    /***
     * Post方式提交文件
     * 适合 把流放入 请求体 上传
     * @param tag
     * @param url
     * @param file
     * @param yxtHttpResponseHandler
     */
    public void uploadFileProgress(String tag, String url, File file, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        CountingFileRequestBody body = new CountingFileRequestBody(file, JSON, yxtHttpResponseHandler, url);
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .post(body)
                .build();
        // 异步
        mOkHttpClient.newCall(request).enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }

    /***
     * 表单上传--直传文件
     * Post方式提交文件
     *
     * @param tag
     * @param url
     * @param formDataPart
     * @param file
     * @param yxtHttpResponseHandler
     */
    public void uploadFileByMultipartBodyAndProgress(String tag, String url, Map<String, String> formDataPart, File file, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        CountingFileRequestBody fileBody = new CountingFileRequestBody(file, MEDIA_TYPE_OCTET_STREAM, yxtHttpResponseHandler, url);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM).addFormDataPart("file", file.getName().substring(file.getName().indexOf(".") - 1), fileBody);
        if (formDataPart != null && !formDataPart.isEmpty()) {
            for (Iterator ite = formDataPart.entrySet().iterator(); ite.hasNext(); ) {
                Map.Entry entry = (Map.Entry) ite.next();
                String keySpf = entry.getKey().toString();
                String headerKey = entry.getValue().toString();
                builder.addFormDataPart(keySpf, headerKey);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }

    /***
     * 表单上传--直传文件
     * Post方式提交文件
     *
     * @param tag
     * @param url
     * @param formDataPart
     * @param yxtHttpResponseHandler
     */
    public void uploadFileByMultipartBodyAndProgress(String tag, String url, Map<String, String> formDataPart, Map<String, String> fileData, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (fileData != null && !fileData.isEmpty()) {
            for (Iterator ite = fileData.entrySet().iterator(); ite.hasNext(); ) {
                Map.Entry entry = (Map.Entry) ite.next();
                String keyW = entry.getKey().toString();
                String keyPath = entry.getValue().toString();
                File file = new File(keyPath);
                if (file.exists()) {
                    CountingFileRequestBody fileBody = new CountingFileRequestBody(file, MEDIA_TYPE_OCTET_STREAM, yxtHttpResponseHandler, url);
                    builder.setType(MultipartBody.FORM).addFormDataPart(keyW, file.getName().substring(file.getName().indexOf(".") - 1), fileBody);
                } else {
                    Log.httpLog("File is not find");
                }
            }
        }
        if (formDataPart != null && !formDataPart.isEmpty()) {
            for (Iterator ite = formDataPart.entrySet().iterator(); ite.hasNext(); ) {
                Map.Entry entry = (Map.Entry) ite.next();
                String keySpf = entry.getKey().toString();
                String headerKey = entry.getValue().toString();
                builder.addFormDataPart(keySpf, headerKey);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap)) // Headers加入
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new OkHttpCallBack(null, "", request, yxtHttpResponseHandler));
    }


    /***
     * 文件 断点下载
     *
     * @param url
     * @param
     * @param filename                文件名字
     * @param yxtHttpResponseHandler
     */
    public void downloadFile(String tag, final String url, final String folderPath, final String filename, final YXTHttpResponseHandler yxtHttpResponseHandler) {
        if (!NetUtils.isNetworkConnected()) {
            if (yxtHttpResponseHandler != null) {
                yxtHttpResponseHandler.onFailure(NO_NETWORK, new HttpInfo(null, "", url), AppManager.getAppManager().getNowContext().getString(R.string.common_yxt_msg_net_error), null);
                yxtHttpResponseHandler.onFinish();
            }
            return;
        }
        if (HttpLibUtils.isUrlWrong(url, yxtHttpResponseHandler)) return;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File file = new File(folderPath, filename);

        long rangeStart = file.length() - 2;
        if (rangeStart < 0) rangeStart = 0;
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .headers(HttpLibUtils.TranslateMapToHeaders(headerMap))         // Headers加入
                .header("RANGE", "bytes=" + rangeStart + "-")      // 断点续传要用到的，指示下载的区间
                .build();
        Log.httpLog("开始下载:" + url);
        mOkHttpClient.newCall(request).enqueue(new OkHttpFileDowloadCallBack(url, headerMap, null, file, rangeStart, yxtHttpResponseHandler));
    }

    /***
     * 根据 String 标记取消请求
     * @param tag
     */
    public void cancelTag(String tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            Object tagFor = call.request().tag();
            if (tagFor != null && tag.equals(tagFor.toString())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            Object tagFor = call.request().tag();
            if (tagFor != null && tag.equals(tagFor.toString())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            Object tagFor = call.request().tag();
            if (tagFor != null) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            Object tagFor = call.request().tag();
            if (tagFor != null) {
                call.cancel();
            }
        }
    }

//    /**
//     * 收集自定义log接口,主要用于调试信息收集
//     *
//     * @param summary  简介
//     * @param detail   收集详情
//     * @param userInfo 用户信息
//     */
//    public void sendCustomLog(String summary, String detail, String userInfo, String userName) {
//        String mobileInfo = HTTPLogUtils.getPhoneAndUserInfo();
//        HashMap<String, Object> paramsEntity = new HashMap<>();
//        paramsEntity.put("detail", detail.replace("\n", "<br>"));
//        paramsEntity.put("summary", summary);
//        paramsEntity.put("userInfo", userInfo);
//        paramsEntity.put("logType", "debug日志");
//        paramsEntity.put("appVersion", 10 + "");
//        paramsEntity.put("mobileInfo", mobileInfo);
//        paramsEntity.put("logTime", PHUtils.formatDataCustom(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        paramsEntity.put("userName", userName);
//        post(BaseConstants.YXT_CUSTOM_LOG_INFO_URL, paramsEntity, new YXTHttpResponseHandler());
//    }

//    /**
//     * 收集崩溃日志,此方法原则上不允许用户调用,只做崩溃后系统自动调用
//     *
//     * @param summary            简介
//     * @param crashDetail        崩溃详情 包括堆栈
//     * @param userInfo           用户信息
//     * @param extra_history_list 用户使用轨迹
//     */
//    public void sendErrorLog(String summary, String crashDetail, String userInfo, String userName, String extra_history_list) {
//        String mobileInfo = HTTPLogUtils.getPhoneAndUserInfo();
//        crashDetail = "最近轨迹:" + extra_history_list + "\n\n崩溃原因:\n" + crashDetail;
//        if (!crashDetail.contains("logout")) {
//            HashMap<String, Object> paramsEntity = new HashMap<>();
//            paramsEntity.put("detail", crashDetail.replace("\n", "<br>"));
//            paramsEntity.put("summary", summary);
//            paramsEntity.put("userInfo", userInfo);
//            paramsEntity.put("userName", userName);
//            paramsEntity.put("appVersion", Utils.getAppBaseVersionCode() + "");
//            paramsEntity.put("mobileInfo", mobileInfo.replace("\n", "<br>"));
//            paramsEntity.put("logTime", PHUtils.formatDataCustom(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            paramsEntity.put("logType", "崩溃日志");
//            post(BaseConstants.YXT_CRASH_LOG_INFO_URL, paramsEntity, new YXTHttpResponseHandler() {
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    PHSPUtil.getInstance().getBoolean("sendCrashLog", true);
//                }
//            });
//        }
//    }

//    /**
//     * 初始化http头部信息
//     */
//    public void initHttpHeaders() {
//        headerMap.put("Source", "503");
//        headerMap.put("SourceType", "50");
//        headerMap.put("App-Version", Utils.getAppBaseVersionCode() + "");
//        headerMap.put("Content-Type", "application/json;charset=utf-8");
//        headerMap.put("Accept-Language", "zh-cn,zh;q=0.5,en-us,en;q=0.5");
//        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
//        headerMap.put("deviceId", PHSPUtil.getInstance().getDeviceId());// Utils.getUUId(getContext()));
//        initLanguage();
//    }

//    /**
//     * 获取当前语言
//     */
//    public void initLanguage() {
//        headerMap.put("Language", LanguageUtils.getAppCurrentLanguage(false));
//    }

    public void setHeader(String k, String v) {
        headerMap.put(k, v);
    }

    public Object getHeader(String key) {
        return headerMap.get(key);
    }

    /**
     * 初始化身份信息
     *
     * @param token
     * @param clientKey
     */
    public void initToken(String token, String clientKey) {
        headerMap.put("token", token);
        headerMap.put("clientKey", clientKey);
    }
}
