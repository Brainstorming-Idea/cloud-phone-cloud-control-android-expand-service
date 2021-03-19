package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.module.virtualcenter.VirtualCenterActivity;
import com.cloud.control.expand.service.module.virtualcenter.VirtualCenterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/8
 */
@Module
public class VirtualCenterModule {
    private VirtualCenterActivity mView;

    public VirtualCenterModule(VirtualCenterActivity mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    public VirtualCenterPresenter provideVirtualCenterPresenter(){
        return new VirtualCenterPresenter(mView);
    }
}