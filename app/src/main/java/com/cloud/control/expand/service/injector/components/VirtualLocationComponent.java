package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.VirtualLocationModule;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationActivity;

import dagger.Component;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：虚拟定位 Component
 */
@PerActivity
@Component(modules = VirtualLocationModule.class)
public interface VirtualLocationComponent {
    void inject(VirtualLocationActivity activity);
}
