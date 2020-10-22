package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.ChangeMachineModule;
import com.cloud.control.expand.service.module.changemachine.ChangeMachineActivity;

import dagger.Component;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：一键新机 Component
 */
@PerActivity
@Component(modules = ChangeMachineModule.class)
public interface ChangeMachineComponent {
    void inject(ChangeMachineActivity activity);
}
