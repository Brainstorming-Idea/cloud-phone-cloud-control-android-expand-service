package com.cloud.control.expand.service.utils.system;

/**
 * Author：wangwenbin
 * Date：2021/8/3
 * Description：板卡及Android版本类型
 */
public enum DeviceBoard {

    RK_ANDROID7("RK_3399_Android7"), RK_ANDROID10("RK_3399_Android10"), MTK_ANDROID10("MTK_G90_Android10"), VIRTUAL("VIRTUAL_Android7");
    String type;

    DeviceBoard(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
