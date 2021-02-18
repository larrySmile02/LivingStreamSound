package com.md.basedpc.http.model;

import java.io.Serializable;

/**
 * Created by ryan
 * @date 2019/10/17.
 * description：网络缓存
 */

public class ACacheModel implements Serializable {
    int code;
    String responseBody;

    public ACacheModel(int code, String responseBody) {
        this.code = code;
        this.responseBody = responseBody;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
