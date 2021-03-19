package com.cloud.control.expand.service.entity;

/**
 * @author wangyou
 * @desc: api返回的消息基类
 * @date :2021/3/15
 */
public class BaseResponse<T> {
    private Integer status;//0：成功、1：失败
    private String msg;
    private T data;
    private Integer retCode;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }
}