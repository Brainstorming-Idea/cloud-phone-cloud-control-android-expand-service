package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 百度接口返回的消息基类
 * @date :2021/3/8
 */
public class BMapBaseResponse {
    /**
     * status : 0：成功
     * 1：服务内部错误
     * 2：参数无效
     * 7：无返回结果
     * message : 状态码对应的信息
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}