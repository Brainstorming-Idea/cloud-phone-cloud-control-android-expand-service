package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.VirtualSceneModule;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneActivity;

import dagger.Component;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/3
 */
@PerActivity
@Component(modules = VirtualSceneModule.class)
public interface VirtualSceneComponent {
    /**
     *
     * @param activity
     */
    void inject(VirtualSceneActivity activity);
}
