package com.kunlun.common;

import java.io.Serializable;

/**
 * 数据统一返回封装类
 * Created by kunlun on 2017/3/24.
 */
public class DataRet<T> implements Serializable {

    private static final long serialVersionUID = -4474045135841749331L;

    //错误编码
    private String errorCode;

    //提示信息
    private String message;

    //返回体
    private T body;

    public DataRet(String errorCode, String message, T body) {
        this.errorCode = errorCode;
        this.message = message;
        this.body = body;
    }

    public DataRet() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Boolean isSuccess() {
        return this.errorCode == null || "".equals(this.errorCode.trim());
    }
}
