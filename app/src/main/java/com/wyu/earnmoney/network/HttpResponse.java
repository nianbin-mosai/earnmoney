package com.wyu.earnmoney.network;

import java.io.Serializable;

/**
 * Created by Rays on 16/5/12.
 */
public class HttpResponse implements Serializable {

    public static final int CODE_OK = 200;// 请求返回值成功可用
    public static final int CODE_ERROR = 300;// 错误码

    /**
     * 状态码，0为成功
     */
    public int returnCode = CODE_ERROR;
    /**
     * 状态码不为0时，才有这个值
     */
    public int code = CODE_OK;
    public String message;

    public HttpResponse() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HttpResponse(String message) {
        this.message = message;
    }

    public HttpResponse(int returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    /**
     * 判断是否成功
     * @return
     */
    public boolean isSuccess() {
        return  code == CODE_OK;
    }
    public boolean isError(){
        return code == CODE_ERROR;
    }
}
