package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneActivity;
import com.cloud.control.expand.service.module.virtualscene.VirtualScenePresenter;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneService;

import dagger.Module;
import dagger.Provides;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/3
 */
@Module
public class VirtualSceneModule {
    private VirtualSceneActivity mView;

    public VirtualSceneModule(VirtualSceneActivity mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    public VirtualScenePresenter provideVirtualScenePresenter(){
        return new VirtualScenePresenter(mView);
    }

}