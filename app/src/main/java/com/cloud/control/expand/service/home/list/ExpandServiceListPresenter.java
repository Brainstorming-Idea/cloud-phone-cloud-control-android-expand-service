package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.DateUtils;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：扩展服务列表Presenter
 */
public class ExpandServiceListPresenter implements IBasePresenter, IExpandServiceList {

    private final ExpandServiceListView mView;
    private boolean isExpire; //是否到期

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
                            mView.hideListView();
                            mView.showNoData("还没有可用扩展服务哦~");
                        }
                    }
                });
    }

    @Override
    public void examineServiceStatus(final ExpandServiceRecordEntity.DataBean dataBean) {
        //同步后台数据状态，避免上次界面停留后数据未实时刷新，再次购买的服务不可使用的现象
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
                        KLog.e("getExtendServiceRecord onNext " + recordEntity.toString());
                        if (recordEntity != null && recordEntity.getData() != null && recordEntity.getData().size() > 0) {
                            isExpire = true;
                            for (int i = 0; i < recordEntity.getData().size(); i++) {
                                if (dataBean.getTypeId() == recordEntity.getData().get(i).getTypeId()) {
                                    isExpire = false;
                                    if (DateUtils.isExpire(recordEntity.getData().get(i).getCurrentTime(), recordEntity.getData().get(i).getDueTimeStr())) {
                                        mView.dialog("提示", "该扩展服务已过期", "", "确认");
                                    } else {
                                        mView.jumpPage(recordEntity.getData().get(i));
                                    }
                                }
                            }
                            //没有找到对应的扩展服务，显示过期弹框
                            if (isExpire) {
                                mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            }
                        } else {
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                        }
                    }
                });
    }
}
