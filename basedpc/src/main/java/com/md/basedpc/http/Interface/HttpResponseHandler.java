package com.md.basedpc.http.Interface;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.md.basedpc.http.model.HttpInfo;
import com.md.basedpc.json.JsonUtils;
import com.md.basedpc.log.Log;

/**
 * Created by ryan
 * @date 2019/10/17.
 * description：http 回调消息
 */
public abstract class HttpResponseHandler<T> implements ResponseHandlerInterface {

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int SUCCESS_CACHE_MESSAGE = 8;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int DOWNLOADED_FINISH_MESSAGE = 7;
    protected Looper looper;
    private Handler handOS;//异步 handler 回调
    private Class<T> ty;
    public void setTy(Class<T> ty) {
        this.ty = ty;
    }

    public HttpResponseHandler() {
        this(Looper.getMainLooper());
    }

    public HttpResponseHandler(Looper looper) {
        this(looper, false);
    }

    public HttpResponseHandler(Looper looper, boolean usePoolThread) {
        if (!usePoolThread) {
            this.looper = looper;
            this.handOS = new EventHandler(looper);
        } else {
            this.looper = null;
            this.handOS = null;
        }
    }


    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart() {
    }

    /**
     * Fired in all cases when the request is finished, after both success and failure, override to
     * handle in your own code
     */
    public abstract void onFinish(); // default log warning is not necessary, because this method is just optional notification

    @Deprecated
    public abstract void onSuccess(int statusCode, HttpInfo httpInfo, String responseString, String errorMsg);

    public abstract void onSuccess(int statusCode, HttpInfo httpInfo, T t);

    public abstract void onFailure(int statusCode, HttpInfo httpInfo, String responseString, Throwable throwable);

    /**
     * Fired when the request progress, override to handle in your own code
     *
     * @param bytesWritten offset from start of file
     * @param totalSize    total size of file
     */
    public void onProgress(double bytesWritten, double totalSize) {
    }

    /**
     * Fired when the request progress, override to handle in your own code
     *
     * @param
     * @param
     */
    public void onDownLoadedFinsh(int statusCode, String msg, double bytesTotal) {
    }

    @Override
    final public void sendStartMessage() {
        handOS.sendMessage(obtainMessage(START_MESSAGE, null));
    }

    @Override
    final public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    @Override
    final public void sendSuccessMessage(int statusCode, HttpInfo httpInfo, String responseBody, String message) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{statusCode, httpInfo, responseBody, message}));
    }

    @Override
    final public void sendFailureMessage(int statusCode, HttpInfo httpInfo, String responseBody, Throwable error) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode, httpInfo, responseBody, error}));
    }

    @Override
    public void sendProgressMessage(double bytesWritten, double bytesTotal, int progress) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{bytesWritten, bytesTotal}));
    }

    @Override
    public void sendDownLoadedFinishMessage(int statusCode, String msg, double bytesTotal) {
        sendMessage(obtainMessage(DOWNLOADED_FINISH_MESSAGE, new Object[]{statusCode, msg, bytesTotal}));
    }

    /**
     * 发handler
     *
     * @param msg
     */
    protected void sendMessage(Message msg) {
        handOS.sendMessage(msg);
    }

    /**
     * Helper method to create Message instance from handler
     *
     * @param responseMessageId   constant to identify Handler message
     * @param responseMessageData object to be passed to message receiver
     * @return Message instance, should not be null
     */
    protected Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handOS, responseMessageId, responseMessageData);
    }

    private class EventHandler extends Handler {
        EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message message) {
            Object[] response;
            try {
                if (message.what == START_MESSAGE) {
                    onStart();
                    handlerCallBack.onStart();
                } else if (message.what == FINISH_MESSAGE) {
                    onFinish();
                    handlerCallBack.onFinish();
                } else if (message.what == SUCCESS_MESSAGE) {
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 4) {
                        onSuccess((Integer) response[0], (HttpInfo) response[1], (String) response[2], (String) response[3]);
                        handlerCallBack.onSuccess((Integer) response[0], (HttpInfo) response[1], (String) response[2], (String) response[3]);
                        if (ty!=null) {
                            String re = (String) response[2];
                            T t = (T) JsonUtils.fromJson(re, ty);
                            handlerCallBack.onSuccess((Integer) response[0], (HttpInfo) response[1], t);
                            onSuccess((Integer) response[0], (HttpInfo) response[1], t);
                        }
                    } else {
                        Log.e("SUCCESS_MESSAGE didn't got enough params");
                    }
                } else if (message.what == SUCCESS_CACHE_MESSAGE) {
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 4) {
                        onSuccess((Integer) response[0], (HttpInfo) response[1], (String) response[2], (String) response[3]);
                        handlerCallBack.onSuccess((Integer) response[0], (HttpInfo) response[1], (String) response[2], (String) response[3]);
                        if (ty!=null) {
                            String re = (String) response[2];
                            T t = (T) JsonUtils.fromJson(re, ty);
                            handlerCallBack.onSuccess((Integer) response[0], (HttpInfo) response[1], t);
                            onSuccess((Integer) response[0], (HttpInfo) response[1], t);
                        }
                    } else {
                        Log.e("SUCCESS_MESSAGE didn't got enough params");
                    }
                } else if (message.what == FAILURE_MESSAGE) {
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 4) {
                        onFailure((Integer) response[0], (HttpInfo) response[1], (String) response[2], (Throwable) response[3]);
                        handlerCallBack.onFailure((Integer) response[0], (HttpInfo) response[1], (String) response[2], (Throwable) response[3]);
                    } else {
                        Log.e("FAILURE_MESSAGE didn't got enough params");
                    }
                } else if (message.what == PROGRESS_MESSAGE) {
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 2) {
                        onProgress((Double) response[0], (Double) response[1]);
                        handlerCallBack.onProgress((Double) response[0], (Double) response[1]);
                    } else {
                        Log.e("PROGRESS_MESSAGE didn't got enough params");
                    }
                } else if (message.what == DOWNLOADED_FINISH_MESSAGE) {
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 2) {
                        onDownLoadedFinsh((Integer) response[0], (String) response[1], (Double) response[2]);
                    } else {
                        Log.e("PROGRESS_MESSAGE didn't got enough params");
                    }
                }
            } catch (Exception e) {
                Log.e("---Handler---Exception----- : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private ResponseHandlerCallBack handlerCallBack = new ResponseHandlerCallBack<T>() {
        @Override
        public void onStart() {
            //TODO NOTING
        }

        @Override
        public void onFinish() {
            //TODO NOTING
        }

        @Override
        public void onSuccess(int statusCode, HttpInfo httpInfo, String responseString, String errorMsg) {
            //TODO NOTING
        }

        @Override
        public void onSuccess(int statusCode, HttpInfo httpInfo, T t) {
            //TODO NOTING
        }

        @Override
        public void onFailure(int statusCode, HttpInfo httpInfo, String responseString, Throwable throwable) {
            //TODO NOTING
        }

        @Override
        public void onProgress(double bytesWritten, double totalSize) {
            //TODO NOTING
        }
    };

    public void setHandlerCallBack(ResponseHandlerCallBack handlerCallBack) {
        if (handlerCallBack != null)
            this.handlerCallBack = handlerCallBack;
    }

    public interface ResponseHandlerCallBack<T> {
        void onStart();

        void onFinish();

        void onSuccess(int statusCode, HttpInfo httpInfo, String responseString, String errorMsg);

        void onSuccess(int statusCode, HttpInfo httpInfo, T t);

        void onFailure(int statusCode, HttpInfo httpInfo, String responseString, Throwable throwable);

        void onProgress(double bytesWritten, double totalSize);
    }
}
