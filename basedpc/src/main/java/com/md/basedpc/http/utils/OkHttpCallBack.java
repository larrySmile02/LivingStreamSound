package com.md.basedpc.http.utils;

import com.md.basedpc.http.Interface.YXTHttpResponseHandler;
import com.md.basedpc.http.model.ACacheModel;
import com.md.basedpc.http.model.HttpInfo;
import com.md.basedpc.log.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：
 * 异步回调 内部类
 * 抛给 UI 线程处理
 * 缓存操作：  http://www.jiangwenrou.com/okhttp%E7%BC%93%E5%AD%98%E6%B5%85%E6%9E%90.html
 */

public class OkHttpCallBack implements Callback {

    private YXTHttpResponseHandler yxtHttpResponseHandler;
    private Request request;
    private CacheType cacheType = CacheType.ONLY_NETWORK;
    private String paramsEntity;

    public OkHttpCallBack(CacheType cacheType, String paramsEntity, Request request, YXTHttpResponseHandler handlerInterface) {
        if (paramsEntity == null) {
            paramsEntity = "";
        }
        this.paramsEntity = paramsEntity;
        if (cacheType != null) this.cacheType = cacheType;
        this.yxtHttpResponseHandler = handlerInterface;
        this.request = request;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if (yxtHttpResponseHandler != null) {
            yxtHttpResponseHandler.sendFailureMessage(call.hashCode(), new HttpInfo(null, "", call.request().url().toString()), e.toString(), e);
            yxtHttpResponseHandler.sendFinishMessage();
        }
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        try {
            int code = response.code();
            String responseBody = response.body().string();
            Headers headers = response.headers();
            Map<String, String> h = new HashMap<>();
            String clientKey = "";
            for (int i = 0; i < headers.names().size(); i++) {
                if (headers.name(i).equals("clientKey"))
                    clientKey = headers.value(i);
                h.put(headers.name(i), headers.value(i));
            }
            for (int i = 0; i < call.request().headers().names().size(); i++) {
                if (call.request().headers().name(i).equals("clientKey"))
                    clientKey = call.request().headers().value(i);
                h.put(call.request().headers().name(i), call.request().headers().value(i));
            }
            //打印请求数据
            HTTPLogUtils.writeLog(response.code(), h, paramsEntity, call.request().url().toString(), responseBody, call.request().method());

            boolean needReturn = true;
            ACacheModel cacheModel = HttpCacheUtil.getInstance().getACache(clientKey + call.request().url().toString());
            if (cacheModel != null && cacheType != null && cacheType == CacheType.CACHED_ELSE_NETWORK) {
                needReturn = !(cacheModel.getResponseBody() == null ? "" : cacheModel.getResponseBody()).equals(responseBody);
            }
            if (cacheType != null && cacheType == CacheType.CACHED_ELSE_NETWORK) {
                if (code > 199 && code < 299)
                    HttpCacheUtil.getInstance().putACache(clientKey, response, code, responseBody);
            }
            if (yxtHttpResponseHandler != null) {
                if (needReturn) {
                    if (code > 199 && code < 299)
                        yxtHttpResponseHandler.sendSuccessMessage(code, new HttpInfo(headers, call.request().body() == null ? "" : call.request().body().toString(), call.request().url().toString(), cacheModel == null && call.request().method().toLowerCase().equals("get")), responseBody, response.message());
                    else
                        yxtHttpResponseHandler.sendFailureMessage(code, new HttpInfo(headers, call.request().body() == null ? "" : call.request().body().toString(), call.request().url().toString()), responseBody, new Throwable(response.message()));
                }
                yxtHttpResponseHandler.sendFinishMessage();
            }
        } catch (IOException e) {
            Log.exceptionLog(e);
        }

    }
}
