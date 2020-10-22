package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 虚拟定位实体类
 */
public class VirtualLocationEntity {

    /**
     * status : 0
     * msg :  定位保存成功
     * data : null
     */

    private int status;
    private String msg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
