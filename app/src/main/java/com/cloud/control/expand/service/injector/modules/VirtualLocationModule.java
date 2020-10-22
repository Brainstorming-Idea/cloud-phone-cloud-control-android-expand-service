package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationActivity;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：虚拟定位 module
 */
@Module
public class VirtualLocationModule {

    private final VirtualLocationActivity mView;

    public VirtualLocationModule(VirtualLocationActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public VirtualLocationPresenter provideVirtualLocationPresenter() {
        return new VirtualLocationPresenter(mView);
    }

}
