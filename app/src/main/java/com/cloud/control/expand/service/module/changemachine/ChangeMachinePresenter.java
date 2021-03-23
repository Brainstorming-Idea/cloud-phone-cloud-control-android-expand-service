package com.cloud.control.expand.service.module.changemachine;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ModelInfoEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：一键新机 Presenter
 */
public class ChangeMachinePresenter implements IBasePresenter, IChangeMachine {

    private final ChangeMachineView mView;
    //所有机型对应的品牌数据集合
    private List<ModelInfoEntity> modelBindBrandList = new ArrayList<>();

    public ChangeMachinePresenter(ChangeMachineView view) {
        mView = view;
    }

    @Override
    public void getData() {
        //品牌型号
        RetrofitServiceManager.getPhoneBrandModel()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<PhoneBrandModelEntity>bindToLife())
                .subscribe(new Subscriber<PhoneBrandModelEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getPhoneBrandModel onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getPhoneBrandModel onError");
                    }

                    @Override
                    public void onNext(final PhoneBrandModelEntity brandModelEntity) {
                        KLog.e("getPhoneBrandModel onNext " + brandModelEntity.getData().toString());
                        modelBindBrandList.clear();
                        //后台数据源放入本地机型集合
                        for (int i = 0; i < brandModelEntity.getData().size(); i++) {
                            ModelInfoEntity infoEntity = new ModelInfoEntity();
                            infoEntity.setBrand(brandModelEntity.getData().get(i).getBrandName());
                            for (int j = 0; j < brandModelEntity.getData().get(i).getModel().size(); j++) {
                                infoEntity.setModel(brandModelEntity.getData().get(i).getModel().get(j).getMobileModel());
                                modelBindBrandList.add(infoEntity);
                            }
                        }
                        mView.loadData(brandModelEntity, null);
                    }
                });

        //机型配置参数
        RetrofitServiceManager.getPhoneModifyInfo("")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<PhoneModelInfoEntity>bindToLife())
                .subscribe(new Subscriber<PhoneModelInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getPhoneModifyInfo onCompleted");
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getPhoneModifyInfo onError");
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(PhoneModelInfoEntity infoEntity) {
                        KLog.e("getPhoneModifyInfo onNext " + infoEntity.toString());
                        mView.loadData(null, infoEntity.getData());
                    }
                });
    }

    @Override
    public void startChangeMachine(UpdatePhoneConfigEntity configEntity) {
        KLog.e("configEntity " + configEntity.toString());
        //启用新机
        RetrofitServiceManager.modifyCard(configEntity)
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
                        KLog.e("modifyCard onCompleted");
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("modifyCard onError");
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(final ResponseEntity responseEntity) {
                        KLog.e("modifyCard onNext " + responseEntity.toString());
                        if (responseEntity.getRetCode() == ConstantsUtils.SERVICE_EXPIRED_CODE) {
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            return;
                        }
                        mView.toast(responseEntity.getMsg());
                    }
                });
    }

    @Override
    public void refreshAllData() {
        //重置刷新数据
        RetrofitServiceManager.getPhoneModifyInfo("")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<PhoneModelInfoEntity>bindToLife())
                .subscribe(new Subscriber<PhoneModelInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                        KLog.e("getPhoneModifyInfo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("getPhoneModifyInfo onError");
                    }

                    @Override
                    public void onNext(PhoneModelInfoEntity infoEntity) {
                        KLog.e("getPhoneModifyInfo onNext " + infoEntity.toString());
                        mView.loadData(null, infoEntity.getData());
                    }
                });
    }

    @Override
    public void refreshSelectModelData(String mobileTypeList) {
        KLog.e("mobileTypeList " + mobileTypeList);
        //指定机型刷新数据
        RetrofitServiceManager.getPhoneModifyInfo(mobileTypeList)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(mView.<PhoneModelInfoEntity>bindToLife())
                .subscribe(new Subscriber<PhoneModelInfoEntity>() {
                    @Override
                    public void onCompleted() {
                        KLog.e("getPhoneModifyInfo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("getPhoneModifyInfo onError");
                    }

                    @Override
                    public void onNext(PhoneModelInfoEntity infoEntity1) {
                        KLog.e("getPhoneModifyInfo onNext " + infoEntity1.getData().toString());
                        mView.loadData(null, infoEntity1.getData());
                    }
                });
    }
}
