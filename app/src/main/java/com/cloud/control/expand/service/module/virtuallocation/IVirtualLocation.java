package com.cloud.control.expand.service.module.virtuallocation;

/**
 * Author：abin
 * Date：2020/10/17
 * Description：
 */
public interface IVirtualLocation {

    /**
     * 启动定位
     * @param longitude
     * @param latitude
     * @param city
     */
    void startLocation(String longitude, String latitude, String city);

}
