package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：扩展服务列表Presenter
 */
public class ExpandServiceListPresenter implements IBasePresenter {

    private final ExpandServiceListView mView;

    public ExpandServiceListPresenter(ExpandServiceListView view) {
        mView = view;
    }

    @Override
    public void getData() {
        RetrofitServiceManager.getExtendServiceRecord()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<ExpandServiceRecordEntity>bindToLife())
                .subscribe(new Subscriber<ExpandServiceRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getExtendServiceRecord onCompleted");
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getExtendServiceRecord onError");
                        mView.showNetError();
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(ExpandServiceRecordEntity recordEntity) {
                        KLog.e("getExtendServiceRecord onNext " + recordEntity.toString());
                        if (recordEntity.getData() != null && recordEntity.getData().size() > 0) {
                            mView.loadData(recordEntity.getData());
                        } else {
                            mView.showNoData("还没有扩展服务哦～敬请期待");
                        }
                    }
                });
    }
}
