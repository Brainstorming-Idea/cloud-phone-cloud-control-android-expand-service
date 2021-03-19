package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：扩展服务列表Presenter
 */
public class ExpandServiceListPresenter implements IBasePresenter, IExpandServiceList {

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
                            /*REMIND --------test---------*/
                            mView.hideListView();
                            mView.showNoData("还没有可用扩展服务哦~");

//                                test();
                            /*---------test---------*/
                        }
                    }
                });
    }

    private void test() {
        List<ExpandServiceRecordEntity.DataBean> expandServiceRecordEntities = new ArrayList<>();
        ExpandServiceRecordEntity.DataBean dataBean = new ExpandServiceRecordEntity.DataBean();
        dataBean.setCurrentTime("2020-11-10 09:00:44");
        dataBean.setDueTimeStr("2021-11-30 07:49:35");
        dataBean.setTypeId(4);
        dataBean.setTypeName("虚拟场景");
        dataBean.setStatus(1);
        expandServiceRecordEntities.add(dataBean);
        mView.loadData(expandServiceRecordEntities);
    }

    @Override
    public void examineServiceStatus(final int position) {
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
                        KLog.e("getExtendServiceRecord onError:"+e.getMessage());
                    }

                    @Override
                    public void onNext(ExpandServiceRecordEntity recordEntity) {
                        KLog.e("getExtendServiceRecord onNext " + recordEntity.toString());
//                        /*----------REMIND test-----------*/
//                        List<ExpandServiceRecordEntity.DataBean> expandServiceRecordEntities = new ArrayList<>();
//                        ExpandServiceRecordEntity.DataBean dataBean = new ExpandServiceRecordEntity.DataBean();
//                        dataBean.setCurrentTime("2020-11-10 09:00:44");
//                        dataBean.setDueTimeStr("2021-11-30 07:49:35");
//                        dataBean.setTypeId(4);
//                        dataBean.setTypeName("虚拟场景");
//                        dataBean.setStatus(1);
//                        expandServiceRecordEntities.add(dataBean);
//                        recordEntity.setData(expandServiceRecordEntities);
//                        /*------------test----------*/
                        if (recordEntity != null && recordEntity.getData() != null && recordEntity.getData().size() > 0) {
                            if(DateUtils.isExpire(recordEntity.getData().get(position).getCurrentTime(), recordEntity.getData().get(position).getDueTimeStr())){
                                mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            }else {
                                mView.jumpPage(recordEntity.getData().get(position));
                            }
                        }else{
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                        }
                    }
                });
    }
}
