package com.cloud.control.expand.service.module.changemachine;

import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.ChangeMachineStatusEntity;
import com.cloud.control.expand.service.entity.ModelInfoEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    //选中品牌机型数据源
    private ModelInfoEntity selectModelInfoEntity = new ModelInfoEntity();

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
                        KLog.e("getPhoneBrandModel onNext " + brandModelEntity.toString());
                        modelBindBrandList.clear();
                        //后台数据源放入本地机型集合
                        for (int i = 0; i < brandModelEntity.getData().size(); i++) {
                            for (int j = 0; j < brandModelEntity.getData().get(i).getModel().size(); j++) {
                                ModelInfoEntity infoEntity = new ModelInfoEntity();
                                infoEntity.setBrand(brandModelEntity.getData().get(i).getBrandName());
                                infoEntity.setModel(brandModelEntity.getData().get(i).getModel().get(j).getMobileModel());
                                modelBindBrandList.add(infoEntity);
                            }
                        }

                        //是否改机
                        RetrofitServiceManager.getPhoneModel()
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                    }
                                })
                                .compose(mView.<ChangeMachineStatusEntity>bindToLife())
                                .subscribe(new Subscriber<ChangeMachineStatusEntity>() {
                                    @Override
                                    public void onCompleted() {
                                        KLog.e("getPhoneModel onCompleted");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        KLog.e("getPhoneModel onError");
                                    }

                                    @Override
                                    public void onNext(ChangeMachineStatusEntity statusEntity) {
                                        KLog.e("getPhoneModel onNext " + statusEntity.toString());
                                        //未改机
                                        if (statusEntity.getData().size() == 0) {
                                            //随机获取一条数据
                                            Random random = new Random();
                                            int n = random.nextInt(modelBindBrandList.size() - 1);
                                            selectModelInfoEntity = modelBindBrandList.get(n);
                                        } else { //已改机
                                            for (int k = 0; k < modelBindBrandList.size(); k++) {
                                                if (modelBindBrandList.get(k).getModel().equals(statusEntity.getData().get(0).getMobileType())) {
                                                    selectModelInfoEntity = modelBindBrandList.get(k);
                                                }
                                            }
                                        }
                                        //机型配置参数
                                        RetrofitServiceManager.getPhoneModifyInfo(selectModelInfoEntity)
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
                                                        mView.loadData(selectModelInfoEntity, null, brandModelEntity, infoEntity.getData().get(0));
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    @Override
    public void startChangeMachine(UpdatePhoneConfigEntity configEntity) {
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
                        if(responseEntity.getMsg().equals("未购买一键新机服务")){
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            return;
                        }
                        mView.toast(responseEntity.getMsg());
                    }
                });
    }

    @Override
    public void refreshAllData() {
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
                        KLog.e("getPhoneBrandModel onNext " + brandModelEntity.toString());
                        modelBindBrandList.clear();
                        //后台数据源放入本地机型集合
                        for (int i = 0; i < brandModelEntity.getData().size(); i++) {
                            for (int j = 0; j < brandModelEntity.getData().get(i).getModel().size(); j++) {
                                ModelInfoEntity infoEntity = new ModelInfoEntity();
                                infoEntity.setBrand(brandModelEntity.getData().get(i).getBrandName());
                                infoEntity.setModel(brandModelEntity.getData().get(i).getModel().get(j).getMobileModel());
                                modelBindBrandList.add(infoEntity);
                            }
                        }

                        //随机获取一条数据
                        Random random = new Random();
                        int n = random.nextInt(modelBindBrandList.size() - 1);
                        selectModelInfoEntity = modelBindBrandList.get(n);
                        //配置参数
                        RetrofitServiceManager.getPhoneModifyInfo(selectModelInfoEntity)
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
                                        mView.loadData(selectModelInfoEntity, null, brandModelEntity, infoEntity.getData().get(0));
                                    }
                                });
                    }
                });
    }

    @Override
    public void refreshSelectModelData(String brand, String model) {
        selectModelInfoEntity.setBrand(brand);
        selectModelInfoEntity.setModel(model);
        //配置参数
        RetrofitServiceManager.getPhoneModifyInfo(selectModelInfoEntity)
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
                        KLog.e("getPhoneModifyInfo onNext " + infoEntity1.getData().get(0).toString());
                        mView.loadData(selectModelInfoEntity, null, null, infoEntity1.getData().get(0));
                    }
                });
    }
}
