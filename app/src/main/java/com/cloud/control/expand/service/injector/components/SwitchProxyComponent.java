package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.SwitchProxyModule;
import com.cloud.control.expand.service.module.switchproxy.SwitchProxyActivity;

import dagger.Component;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：切换IP Component
 */
@PerActivity
@Component(modules = SwitchProxyModule.class)
public interface SwitchProxyComponent {
    void inject(SwitchProxyActivity activity);
}
