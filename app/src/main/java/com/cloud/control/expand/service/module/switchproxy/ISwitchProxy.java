package com.cloud.control.expand.service.module.switchproxy;

/**
 * Author：abin
 * Date：2020/10/16
 * Description：
 */
public interface ISwitchProxy {
    /**
     * 启动代理
     *
     * @param cityList
     * @param ipChangeType
     */
    void startProxy(String[] cityList, int ipChangeType);

    /**
     * 关闭代理
     */
    void closeProxy();

    /**
     * 刷新城市列表
     */
    void refreshCityList();
}
