// AEIPCService.aidl
package com.cloud.control.expand.service.aidl;

// Declare any non-default types here with import statements

interface AEIPCService {
    /**
    * IP变化
    */
    void ipChange(String proxyIp);

    /**
     * 获取虚拟场景状态
     */
    int getVsStatus();
}
