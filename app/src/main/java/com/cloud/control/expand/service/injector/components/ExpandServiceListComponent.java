package com.cloud.control.expand.service.injector.components;

import com.cloud.control.expand.service.home.list.ExpandServiceListActivity;
import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.injector.modules.ExpandServiceListModule;

import dagger.Component;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：扩展服务列表 Component
 */
@PerActivity
@Component(modules = ExpandServiceListModule.class)
public interface ExpandServiceListComponent {
    void inject(ExpandServiceListActivity activity);
}
