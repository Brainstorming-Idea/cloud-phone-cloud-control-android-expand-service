package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.adapter.SwitchProxyCityListAdapter;
import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.SelectCityStatusEntity;
import com.cloud.control.expand.service.injector.PerActivity;
import com.cloud.control.expand.service.module.switchproxy.SwitchProxyActivity;
import com.cloud.control.expand.service.module.switchproxy.SwitchProxyPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：切换IP module
 */
@Module
public class SwitchProxyModule {

    private final SwitchProxyActivity mView;

    public SwitchProxyModule(SwitchProxyActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public SwitchProxyPresenter provideSwitchProxyPresenter() {
        return new SwitchProxyPresenter(mView);
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideSwitchProxyCityListAdapter() {
        return new SwitchProxyCityListAdapter(mView).setICityDataListener(mView);
    }

}
