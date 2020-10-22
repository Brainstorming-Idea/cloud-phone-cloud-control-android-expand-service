package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 切换IP方式实体类
 */
public class SwitchProxyTypeEntity {


    /**
     * status : 0
     * msg : success
     * data : 0
     */

    private int status;
    private String msg;
    private int data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
