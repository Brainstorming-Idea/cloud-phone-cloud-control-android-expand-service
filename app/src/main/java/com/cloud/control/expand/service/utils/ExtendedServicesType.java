package com.cloud.control.expand.service.utils;

/**
 * Author：wangwenbin
 * Date：2021/3/5
 * Description：扩展服务类型
 */
public enum ExtendedServicesType {

    //1 IP代理
    //2 虚拟定位
    //3 一键新机
    SWITCH_PROXY(1, "IP代理"), VIRTUAL_LOCATION(2, "虚拟定位"), CHANGE_MACHINE(3, "一键新机");

    private Integer key;
    private String value;

    ExtendedServicesType(Integer key, String value){
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
