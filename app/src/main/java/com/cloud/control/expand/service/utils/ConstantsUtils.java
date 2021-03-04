package com.cloud.control.expand.service.utils;

/**
 * Author：abin
 * Date：2020/11/11
 * Description： 常量工具
 */
public class ConstantsUtils {

    //图片中间拼接地址
    public static final String IMAGE_MIDDLE_URL = "api/upgrade/downlodFile/viewImage/?url=";

    /**
     * IP切换方式
     */
    public interface IpChangeType{
        int RANDOM_SWITCH_SHORT = 0; //短效全国随机
        int ASSIGN_SWITCH_SHORT = 1; //短效指定城市
        int RANDOM_SWITCH_LONG = 2; //长效全国随机
        int ASSIGN_SWITCH_LONG = 3; //长效指定城市
    }

}
