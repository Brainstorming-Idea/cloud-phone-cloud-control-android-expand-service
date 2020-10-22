package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
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
public class ExpandServiceListPresenter implements IBasePresenter, IExpandServiceExpired {

    private final ExpandServiceListView mView;

    public ExpandServiceListPresenter(ExpandServiceListView view) {
        mView = view;
    }

    @Override
    public void getData() {
        RetrofitServiceManager.getExtendServiceList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<ExpandServiceListEntity>bindToLife())
                .subscribe(new Subscriber<ExpandServiceListEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getExtendServiceList onCompleted");
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                        mView.hideLoading();
                        KLog.e("getExtendServiceList onError");
                    }

                    @Override
                    public void onNext(ExpandServiceListEntity listEntity) {
                        KLog.e("getExtendServiceList onNext " + listEntity.toString());
                        if (listEntity.getData() != null && listEntity.getData().size() > 0) {
                            mView.loadData(listEntity.getData());
                        } else {
                            mView.showNoData("还没有扩展服务哦～敬请期待");
                        }
                    }
                });
    }

    @Override
    public void lookExpiredStatus(final ExpandServiceListEntity.DataBean dataBean) {
        RetrofitServiceManager.getExtendServiceRecord()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<ExpandServiceRecordEntity>bindToLife())
                .subscribe(new Subscriber<ExpandServiceRecordEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getExtendServiceRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getExtendServiceRecord onError");
                    }

                    @Override
                    public void onNext(ExpandServiceRecordEntity recordEntity) {
                        KLog.e("getExtendServiceRecord recordEntity " + recordEntity.toString());
                        mView.jumpPage(dataBean, recordEntity);
                    }
                });
    }
}
