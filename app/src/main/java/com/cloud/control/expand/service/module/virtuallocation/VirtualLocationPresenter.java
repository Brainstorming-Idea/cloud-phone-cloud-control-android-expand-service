package com.cloud.control.expand.service.module.virtuallocation;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：虚拟定位 Presenter
 */
public class VirtualLocationPresenter implements IBasePresenter, IVirtualLocation {

    private final VirtualLocationView mView;

    public VirtualLocationPresenter(VirtualLocationView view) {
        mView = view;
    }

    @Override
    public void getData() {
        RetrofitServiceManager.getGps()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<VirtualLocationInfoEntity>bindToLife())
                .subscribe(new Subscriber<VirtualLocationInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("getGps onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("getGps onError");
                    }

                    @Override
                    public void onNext(VirtualLocationInfoEntity virtualLocationInfoEntity) {
                        KLog.e("getGps onNext " + virtualLocationInfoEntity.toString());
                        mView.loadData(virtualLocationInfoEntity);
                    }
                });
    }

    @Override
    public void startLocation(String longitude, String latitude, String city) {
        RetrofitServiceManager.setGps(longitude, latitude, city)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<VirtualLocationEntity>bindToLife())
                .subscribe(new Subscriber<VirtualLocationEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("setGps onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("setGps onError");
                    }

                    @Override
                    public void onNext(VirtualLocationEntity locationEntity) {
                        KLog.e("setGps onNext " + locationEntity.toString());
                        mView.toast(locationEntity.getMsg());
                    }
                });
    }
}
