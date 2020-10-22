package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 关闭代理实体类
 */
public class CloseProxyEntity {

    /**
     * status : 0
     * msg : 切换IP指令已发送
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

    @Override
    public String toString() {
        return "CloseProxyEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
