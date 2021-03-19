package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.VirtualCenterModule;
import com.cloud.control.expand.service.module.virtualcenter.VirtualCenterActivity;

import dagger.Component;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/8
 */
@PerActivity
@Component(modules = VirtualCenterModule.class)
public interface ChooseVirtualCenterComponent {
    void inject(VirtualCenterActivity activity);
}
