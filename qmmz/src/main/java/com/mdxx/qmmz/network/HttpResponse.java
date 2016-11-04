package com.mdxx.qmmz.network;

import java.io.Serializable;

/**
 * Created by Rays on 16/5/12.
 */
public class HttpResponse implements Serializable {

    public static final int CODE_OK = 200;// 请求返回值成功可用
    public static final int CODE_ERROR = -500;// 错误码
    /**
     * 状态码，0为成功
     */
    public int code = CODE_ERROR;
    /**
     * 状态码不为0时，才有这个值
     */
    public String message;

    public HttpResponse() {

    }

    public HttpResponse(String message) {
        this.message = message;
    }

    public HttpResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 判断是否成功
     * @return
     */
    public boolean isSuccess() {
        return  code == CODE_OK;
    }
}
