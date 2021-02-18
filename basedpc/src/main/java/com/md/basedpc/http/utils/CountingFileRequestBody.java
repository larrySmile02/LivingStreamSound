package com.md.basedpc.http.utils;

import com.md.basedpc.http.Interface.YXTHttpResponseHandler;
import com.md.basedpc.http.model.HttpInfo;

import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by ryan
 *
 * @date 2019/10/17.
 * description：上传文件分片内容
 */
public class CountingFileRequestBody extends RequestBody {

    private static final int SEGMENT_SIZE = 4 * 1024;
    private double fileSize = 0;
    private final File file;
    private YXTHttpResponseHandler fileHttpResponseHandler;
    private final MediaType cType;
    private String url;

    public CountingFileRequestBody(File file, MediaType contentType, YXTHttpResponseHandler fileHttpResponseHandler, String url) {
        this.file = file;
        this.cType = contentType;
        this.fileHttpResponseHandler = fileHttpResponseHandler;
        this.url = url;
        if (file != null && file.exists()) {
            fileSize = file.length();
        }
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return cType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            double total = 0d;
            double read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();
                int progress = (int) (fileSize * 1.0f / total * 100);
                this.fileHttpResponseHandler.sendProgressMessage(total, fileSize, progress);
            }
        } catch (IOException e) {
            fileHttpResponseHandler.sendFailureMessage(0, new HttpInfo(null, "", url), e.toString(), e);
            fileHttpResponseHandler.sendFinishMessage();
        } finally {
            Util.closeQuietly(source);
        }
    }


}
