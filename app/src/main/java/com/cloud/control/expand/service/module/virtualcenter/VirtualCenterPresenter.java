package com.cloud.control.expand.service.module.virtualcenter;

import android.widget.Toast;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/8
 */
public class VirtualCenterPresenter implements IBasePresenter {

    private VirtualCenterView mView;

    public VirtualCenterPresenter(VirtualCenterView mView) {
        this.mView = mView;
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
                        KLog.e("getGps onError:"+e.getMessage());
                    }

                    @Override
                    public void onNext(VirtualLocationInfoEntity virtualLocationInfoEntity) {
                        KLog.e("getGps onNext " + virtualLocationInfoEntity.toString());
                        mView.loadData(virtualLocationInfoEntity);
                    }
                });
    }

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
                        if("未购买GPS定位服务".equals(locationEntity.getMsg())){
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            return;
                        }
                        mView.setResult();
                    }
                });
    }
}