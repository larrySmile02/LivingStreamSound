package com.md.basedpc.http.Interface;

import com.md.basedpc.http.model.HttpInfo;

/**
 * Created by ryan
 * @date 2019/10/17.
 * description：http回调接口的空实现
 */
public class YXTHttpResponseHandler<T> extends HttpResponseHandler<T> {


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFailure(int statusCode, HttpInfo httpInfo, String responseString, Throwable throwable) {
        //TODO NOTHING
    }

    @Override
    public void onSuccess(int statusCode, HttpInfo httpInfo, String responseString, String errorMsg) {
        //TODO NOTHING
    }

    @Override
    public void onSuccess(int statusCode, HttpInfo httpInfo, T t) {
        //TODO NOTHING
    }

    @Override
    public void onFinish() {
        //TODO NOTHING
    }

    @Override
    public void onProgress(double bytesWritten, double totalSize) {
        //TODO NOTHING
    }


}
