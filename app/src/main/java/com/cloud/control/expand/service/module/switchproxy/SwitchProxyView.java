package com.cloud.control.expand.service.module.switchproxy;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.CityListEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.SwitchProxyTypeEntity;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：切换IP接口
 */
public interface SwitchProxyView extends IBaseView {

    /**
     * 显示数据
     *
     * @param ip
     * @param ipChangeType
     * @param cityListEntity
     */
    void loadData(ResponseEntity ip, SwitchProxyTypeEntity ipChangeType, CityListEntity cityListEntity);

}
