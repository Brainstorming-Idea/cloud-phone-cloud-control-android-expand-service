package com.cloud.control.expand.service.injector.modules;

import com.cloud.control.expand.service.adapter.ExpandServiceListAdapter;
import com.cloud.control.expand.service.home.list.ExpandServiceListActivity;
import com.cloud.control.expand.service.home.list.ExpandServiceListPresenter;
import com.cloud.control.expand.service.injector.PerActivity;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：扩展服务列表 module
 */
@Module
public class ExpandServiceListModule {

    private final ExpandServiceListActivity mView;

    public ExpandServiceListModule(ExpandServiceListActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ExpandServiceListPresenter provideExpandServiceMainListPresenter() {
        return new ExpandServiceListPresenter(mView);
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideExpandServiceMainListAdapter() {
        return new ExpandServiceListAdapter(mView);
    }

}
