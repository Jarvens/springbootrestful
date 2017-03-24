package com.kunlun.common;

import java.io.Serializable;

/**
 * Created by kunlun on 2017/3/24.
 */
public class ErrorInfo<T> implements Serializable {

    private static final long serialVersionUID = 307822638840225779L;

    //系统异常编码
    public static final Integer ERROR =500;

    //错误编码
    private Integer errorCode;

    //异常信息
    private String message;

    //异常地址
    private String url;

    //异常内容
    private T data;

    public ErrorInfo(Integer errorCode, String message, String url, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.url = url;
        this.data = data;
    }

    public ErrorInfo() {
    }

    public static Integer getERROR() {
        return ERROR;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
