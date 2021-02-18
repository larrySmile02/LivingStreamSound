package com.md.basedpc.http.utils;

import com.md.basedpc.http.Interface.YXTHttpResponseHandler;
import com.md.basedpc.http.model.HttpInfo;
import com.md.basedpc.log.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：
 * 异步回调 内部类
 * 下载UI异步回调
 * 抛给 UI 线程处理
 * Server通过请求头中的Range: bytes=0-xxx来判断是否是做Range请求，如果这个值存在而且有效，则只发回请求的那部分文件内容，响应的状态码变成206，表示Partial Content，并设置Content-Range。
 * 如果无效，则返回416状态码，表明Request Range Not Satisfiable（http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.17 ）
 * 。如果不包含Range的请求头，则继续通过常规的方式响应
 */
public class OkHttpFileDowloadCallBack implements Callback {

    private File file = null;
    private YXTHttpResponseHandler yxtHttpResponseHandler;
    Map headermap;
    long rangeStart = 0;
    private String paramsEntity;

    public OkHttpFileDowloadCallBack(String url, Map headermap, String paramsEntity, File file, long rangeStart, YXTHttpResponseHandler yxtHttpResponseHandler) {
        this.file = file;
        this.rangeStart = rangeStart;
        if (paramsEntity == null) {
            paramsEntity = "";
        }
        this.paramsEntity = paramsEntity;
        this.yxtHttpResponseHandler = yxtHttpResponseHandler;
        this.headermap = headermap;
        if (yxtHttpResponseHandler != null) {
            yxtHttpResponseHandler.sendStartMessage();
        }
        // hand 回调 ， UI线程
        yxtHttpResponseHandler.setHandlerCallBack(new mResponseHandlerCallBack(yxtHttpResponseHandler, url, headermap, paramsEntity));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (yxtHttpResponseHandler != null) {
            yxtHttpResponseHandler.sendFailureMessage(call.hashCode(), new HttpInfo(null, "", call.request().url().toString()), e.toString(), e);
            yxtHttpResponseHandler.sendFinishMessage();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        save(call, response, rangeStart, file, yxtHttpResponseHandler);
    }


    /**
     * RandomAccessFile -- 保存文件
     *
     * @param response
     * @param mDownloadedSize
     * @param destination
     * @param yxtHttpResponseHandler
     */
    private void save(Call call, Response response, long mDownloadedSize, File destination, YXTHttpResponseHandler yxtHttpResponseHandler) {
        Headers headers = response.headers();
        Map<String, String> h = new HashMap<>();
        for (int i = 0; i < headers.names().size(); i++) {
            h.put(headers.name(i), headers.value(i));
        }
        for (int i = 0; i < call.request().headers().names().size(); i++) {
            h.put(call.request().headers().name(i), call.request().headers().value(i));
        }

        if (response.code() == 404) {
            Log.local("下载文件不存在:" + call.request().url().toString());
            return;
        }

        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        RandomAccessFile raf = null;
        // 储存下载文件的目录
        try {
            raf = new RandomAccessFile(destination.getAbsolutePath(), "rw");
            raf.seek(raf.length() - 2 < 0 ? 0 : raf.length() - 2);
            is = response.body().byteStream();
            long total = response.body().contentLength() + mDownloadedSize;
//                                fos = new FileOutputStream(file);
            long sum = mDownloadedSize;
            while ((len = is.read(buf)) != -1) {
                raf.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                // 下载中
                long finalSum = sum;
                if (yxtHttpResponseHandler != null) {
                    yxtHttpResponseHandler.sendProgressMessage((double) finalSum, (double) total, progress);
                }
            }
            if (yxtHttpResponseHandler != null)
                yxtHttpResponseHandler.sendDownLoadedFinishMessage(response.code(), "", total);
            //打印请求数据
            HTTPLogUtils.writeLog(response.code(), h, paramsEntity, call.request().url().toString(), "下载文件完成:" + call.request().url().toString() + " -> " + destination.getAbsolutePath(), call.request().method());
        } catch (Exception e) {
            Log.e(e.getMessage());
            if (yxtHttpResponseHandler != null)
                yxtHttpResponseHandler.sendFailureMessage(0, new HttpInfo(null, "", call.request().url().toString()), "", null);
            //打印请求数据
            HTTPLogUtils.writeLog(response.code(), h, paramsEntity, call.request().url().toString(), "下载文件失败:" + call.request().url().toString() + " -> " + destination.getAbsolutePath(), call.request().method());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
            }
        }
        if (yxtHttpResponseHandler != null) {
            yxtHttpResponseHandler.sendFinishMessage();
        }
    }
}
