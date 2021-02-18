package com.md.basedpc.http.Interface;


import com.md.basedpc.http.model.HttpInfo;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：http回调接口
 */
public interface ResponseHandlerInterface {

    /**
     * Notifies callback, that request started execution
     */
    void sendStartMessage();

    /**
     * Notifies callback, that request was completed and is being removed from thread pool
     */
    void sendFinishMessage();

    /**
     * Notifies callback, that request was handled successfully
     *
     * @param statusCode   HTTP status code
     * @param responseBody returned data
     */
    void sendSuccessMessage(int statusCode, HttpInfo httpInfo, String responseBody, String errorMsg);

    /**
     * Returns if request was completed with error code or failure of implementation
     *
     * @param statusCode   returned HTTP status code
     * @param responseBody returned data
     * @param error        cause of request failure
     */
    void sendFailureMessage(int statusCode, HttpInfo httpInfo, String responseBody, Throwable error);


    /**
     * Notifies callback, that request (mainly uploading) has progressed
     *
     * @param bytesWritten number of written bytes
     * @param bytesTotal   number of total bytes to be written
     */
    void sendProgressMessage(double bytesWritten, double bytesTotal, int progress);

    /**
     * Notifies callback, that request (mainly uploading) has progressed
     *
     * @param msg        of written bytes
     * @param bytesTotal number of total bytes to be written
     */
    void sendDownLoadedFinishMessage(int statusCode, String msg, double bytesTotal);

}

