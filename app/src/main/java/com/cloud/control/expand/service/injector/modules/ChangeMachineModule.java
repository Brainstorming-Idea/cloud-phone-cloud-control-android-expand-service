package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.module.changemachine.ChangeMachineActivity;
import com.cloud.control.expand.service.module.changemachine.ChangeMachinePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：一键新机 module
 */
@Module
public class ChangeMachineModule {

    private final ChangeMachineActivity mView;

    public ChangeMachineModule(ChangeMachineActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ChangeMachinePresenter provideChangeMachinePresenter() {
        return new ChangeMachinePresenter(mView);
    }

}
