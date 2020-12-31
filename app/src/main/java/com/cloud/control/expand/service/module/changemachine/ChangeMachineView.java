package com.cloud.control.expand.service.module.changemachine;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.ChangeMachineStatusEntity;
import com.cloud.control.expand.service.entity.ModelInfoEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：一键新机接口
 */
public interface ChangeMachineView extends IBaseView {

    /**
     * 显示数据
     *
     * @param modelInfoEntity
     * @param statusEntity
     * @param brandModelEntity
     * @param dataBean
     */
    void loadData(ModelInfoEntity modelInfoEntity, ChangeMachineStatusEntity statusEntity, PhoneBrandModelEntity brandModelEntity, PhoneModelInfoEntity.DataBean dataBean);

}
