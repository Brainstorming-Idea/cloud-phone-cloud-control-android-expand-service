package com.cloud.control.expand.service.utils;

import com.cloud.control.expand.service.log.KLog;

/**
 * Author：wangwenbin
 * Date：2021/11/4
 * Description：环境地址
 */
public class Environment {

    /**
     * 配网工具区分使用http/https
     * @param url
     * @return
     */
    public static boolean isHttps(String url){
        try {
            if (url != null) {
                if (url.regionMatches(true, 0, "ws:", 0, 3)) {
                    return false;
                } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            KLog.e("isHttps Exception : " + e.getMessage());
        }
        return false;
    }

    /**
     * 服务器前缀地址(http/https)
     * @param url
     * @return
     */
    public static String getAppHostPrefix(String url){
        return isHttps(url) ? "https://" : "http://";
    }

    /**
     * 服务器地址
     * @param url
     * @return
     */
    public static String getAppHost(String url){
        try {
            if(url != null) {
                if (url.regionMatches(true, 0, "ws:", 0, 3)) {
                    url = "http:" + url.substring(3, url.length() - 2);
                } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
                    url = "https:" + url.substring(4, url.length() - 2);
                }
                return url;
            }
        } catch (Exception e){
            KLog.e("isHttps getAppIp : " + e.getMessage());
        }
        return "";
    }

}
