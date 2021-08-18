package com.cloud.control.expand.service.utils.system;

import android.text.TextUtils;

import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.utils.SysProp;

/**
 * Author：wangwenbin
 * Date：2021/8/3
 * Description：板卡信息
 */
public class DeviceUtils {

    public static String getDeviceBoard() {
        //板卡信息
        String deviceBoard = SysProp.get("ro.build.description", "");
        KLog.e("deviceBoard : " + deviceBoard);
        if (TextUtils.isEmpty(deviceBoard)) {
            KLog.e("无法获取板卡信息");
        }
        if (deviceBoard.startsWith("rk3399_Android10")) { //RK Android10
            return DeviceBoard.RK_ANDROID10.getType();
        } else if (deviceBoard.startsWith("anbox_arm64")) { //虚拟化
            return DeviceBoard.VIRTUAL.getType();
        } else if (deviceBoard.startsWith("GM10")) { //MTK Android10
            return DeviceBoard.MTK_ANDROID10.getType();
        } else { //默认RK Android7
            return DeviceBoard.RK_ANDROID7.getType();
        }
    }

}
