package com.cloud.control.expand.service.module.switchproxy;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.CityListEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.SwitchProxyTypeEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：切换IP Presenter
 */
public class SwitchProxyPresenter implements IBasePresenter, ISwitchProxy {

    private final SwitchProxyView mView;
    private CityListEntity mCityListEntity;

    public SwitchProxyPresenter(SwitchProxyView view) {
        mView = view;
    }

    @Override
    public void getData() {
        RetrofitServiceManager.getCityIp()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<ResponseEntity>bindToLife())
                .subscribe(new Subscriber<ResponseEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getCityIp onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getCityIp onError");
                    }

                    @Override
                    public void onNext(ResponseEntity responseEntity) {
                        KLog.e("getCityIp onNext " + responseEntity.toString());
                        mView.loadData(responseEntity, null, null);
                    }
                });
        RetrofitServiceManager.getChangeIpType()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<SwitchProxyTypeEntity>bindToLife())
                .subscribe(new Subscriber<SwitchProxyTypeEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getChangeIpType onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getChangeIpType onError");
                    }

                    @Override
                    public void onNext(SwitchProxyTypeEntity typeEntity) {
                        KLog.e("getChangeIpType onNext " + typeEntity.getData());
                        mView.loadData(null, typeEntity, null);
                    }
                });
        RetrofitServiceManager.getCityList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<CityListEntity>bindToLife())
                .subscribe(new Subscriber<CityListEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("getCityList onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("getCityList onError");
                    }

                    @Override
                    public void onNext(CityListEntity cityListEntity) {
                        KLog.e("getCityList onNext " + cityListEntity.toString());
                        mCityListEntity = cityListEntity;
                        mView.loadData(null, null, cityListEntity);
                    }
                });
    }

    @Override
    public void startProxy(String[] cityList, int ipChangeType) {
        RetrofitServiceManager.changeCityIp(cityList, ipChangeType)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<ResponseEntity>bindToLife())
                .subscribe(new Subscriber<ResponseEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("changeCityIp onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("changeCityIp onError");
                    }

                    @Override
                    public void onNext(ResponseEntity responseEntity) {
                        KLog.e("changeCityIp onNext " + responseEntity.toString());
                        if(responseEntity.getMsg().equals("未购买IP代理扩展服务")){
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            return;
                        }
                        mView.toast(responseEntity.getMsg());
                    }
                });
    }

    @Override
    public void refreshCityList() {
        if(mCityListEntity != null){
            mView.loadData(null, null, mCityListEntity);
            return;
        }
        RetrofitServiceManager.getCityList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<CityListEntity>bindToLife())
                .subscribe(new Subscriber<CityListEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("getCityList onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("getCityList onError");
                    }

                    @Override
                    public void onNext(CityListEntity cityListEntity) {
                        KLog.e("getCityList onNext " + cityListEntity.toString());
                        mView.loadData(null, null, cityListEntity);
                    }
                });
    }

    @Override
    public void closeProxy() {
        RetrofitServiceManager.closeProxy()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<ResponseEntity>bindToLife())
                .subscribe(new Subscriber<ResponseEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("closeProxy onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("closeProxy onError");
                    }

                    @Override
                    public void onNext(ResponseEntity responseEntity) {
                        KLog.e("closeProxy onNext " + responseEntity.toString());
                        mView.toast(responseEntity.getMsg());
                    }
                });
    }
}
