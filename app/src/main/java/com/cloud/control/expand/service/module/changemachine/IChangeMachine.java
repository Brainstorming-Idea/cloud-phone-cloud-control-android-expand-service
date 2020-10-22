package com.cloud.control.expand.service.module.changemachine;

import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description：
 */
public interface IChangeMachine {
    /**
     * 启用新机
     *
     * @param configEntity
     */
    void startChangeMachine(UpdatePhoneConfigEntity configEntity);

    /**
     * 刷新所有数据
     */
    void refreshAllData();

    /**
     * 刷新选中机型数据
     */
    void refreshSelectModelData(String brand, String model);
}
