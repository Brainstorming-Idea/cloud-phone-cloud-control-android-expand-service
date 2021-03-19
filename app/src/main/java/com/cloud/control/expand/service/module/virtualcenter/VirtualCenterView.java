package com.cloud.control.expand.service.module.virtualcenter;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/8
 */
public interface VirtualCenterView extends IBaseView {
    void loadData(VirtualLocationInfoEntity virtualLocationInfoEntity);

    void setResult();
}
