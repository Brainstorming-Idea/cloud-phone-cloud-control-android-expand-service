package com.cloud.control.expand.service.utils;

public class NoFastClickUtils {
    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 500;//时间间隔
    private static int spaceTimeCustomerService = 1000;//在线客服界面第一次跳转响应较慢,间隔时间给长点

    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = false;

        } else {
            isAllowClick = true;

        }

        lastClickTime = currentTime;
        return isAllowClick;
    }

    /**
     * 在线客服界面专用
     *
     * @return
     */
    public static boolean isCustomerServiceFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTimeCustomerService) {

            isAllowClick = false;

        } else {
            isAllowClick = true;

        }

        lastClickTime = currentTime;
        return isAllowClick;
    }

}
