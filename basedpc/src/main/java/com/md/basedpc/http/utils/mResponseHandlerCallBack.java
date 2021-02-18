package com.md.basedpc.http.utils;

import com.md.basedpc.http.Interface.YXTHttpResponseHandler;
import com.md.basedpc.http.model.HttpInfo;

import java.util.Map;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：网络请求回调
 */

public class mResponseHandlerCallBack<T> implements YXTHttpResponseHandler.ResponseHandlerCallBack<T> {

    private String url;
    private String paramsEntity;
    private YXTHttpResponseHandler callBackInterceptor;
    private Map headerMap;

    public mResponseHandlerCallBack(YXTHttpResponseHandler callBackInterceptor, String url, Map headermap, String paramsEntity) {
        this.url = url;
        this.headerMap = headermap;
        this.callBackInterceptor = callBackInterceptor;
        if (paramsEntity != null) {
            this.paramsEntity = paramsEntity;
        } else {
            this.paramsEntity = "";
        }
    }

    @Override
    public void onStart() {
        //TODO NOTHING
    }

    @Override
    public void onFinish() {
        //TODO NOTHING
    }

    @Override
    public void onSuccess(int statusCode, HttpInfo httpInfo, String responseString, String errorMsg) {
        if (callBackInterceptor != null)
            callBackInterceptor.onSuccess(statusCode,httpInfo, responseString, errorMsg);
    }

    @Override
    public void onSuccess(int statusCode, HttpInfo httpInfo, T t) {
        if (callBackInterceptor != null)
            callBackInterceptor.onSuccess(statusCode,httpInfo, t);
    }

    @Override
    public void onFailure(int statusCode,HttpInfo httpInfo, String responseString, Throwable throwable) {
        if (callBackInterceptor != null)
            callBackInterceptor.onFailure(statusCode, httpInfo,responseString, throwable);
    }

    @Override
    public void onProgress(double bytesWritten, double totalSize) {
        //TODO NOTHING
    }

}
