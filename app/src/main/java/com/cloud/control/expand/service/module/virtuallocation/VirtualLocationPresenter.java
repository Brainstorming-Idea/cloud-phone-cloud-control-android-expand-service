package com.cloud.control.expand.service.module.virtuallocation;

import android.text.TextUtils;
import android.util.Log;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.BaseResponse;
import com.cloud.control.expand.service.entity.ExpandService;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;
import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.module.virtualscene.HardwareUtil;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneService;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.GPSUtil;
import com.cloud.control.expand.service.utils.ServerUtils;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;

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
//        RetrofitServiceManager.getGps()
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        mView.showLoading();
//                    }
//                })
//                .compose(mView.<VirtualLocationInfoEntity>bindToLife())
//                .subscribe(new Subscriber<VirtualLocationInfoEntity>() {
//                    @Override
//                    public void onCompleted() {
//                        mView.hideLoading();
//                        KLog.e("getGps onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.hideLoading();
//                        KLog.e("getGps onError");
//                    }
//
//                    @Override
//                    public void onNext(VirtualLocationInfoEntity virtualLocationInfoEntity) {
//                        KLog.e("getGps onNext " + virtualLocationInfoEntity.toString());
//                        mView.loadData(virtualLocationInfoEntity);
//                    }
//                });

        //从本地获取坐标
        String location = HardwareUtil.getInstance(ExpandServiceApplication.getInstance())
                .getGpsLocation();
        if (!TextUtils.isEmpty(location)) {
            double[] bd09 = GPSUtil.gps84_To_bd09(Double.parseDouble(location.split(";")[0]), Double.parseDouble(location.split(";")[1]));
            VirtualLocationInfoEntity entity = new VirtualLocationInfoEntity();
            VirtualLocationInfoEntity.DataBean dataBean = new VirtualLocationInfoEntity.DataBean();
            dataBean.setLatitude(bd09[0] + "");
            dataBean.setLongitude(bd09[1] + "");
            entity.setData(dataBean);
            mView.loadData(entity);
        }
    }

    @Override
    public void startLocation(String longitude, String latitude, String city) {
        if (checkStatus()){
            return;
        }
        RetrofitServiceManager.setGps(longitude, latitude, city)
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
                        KLog.e("setGps onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        KLog.e("setGps onError");
                    }

                    @Override
                    public void onNext(ResponseEntity locationEntity) {
                        KLog.e("setGps onNext " + locationEntity.toString());
                        if (locationEntity.getRetCode() == ConstantsUtils.SERVICE_EXPIRED_CODE) {
                            mView.dialog("提示", "该扩展服务已过期", "", "确认");
                            return;
                        }
                        mView.toast(locationEntity.getMsg());
                    }
                });
    }

//    /**
//     * 检测是否开启了虚拟场景
//     * @param msg
//     */
//    public void checkVs(String msg){
//        RetrofitServiceManager.getVsStatus(ExpandServiceApplication.getInstance().getCardSn())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        mView.showLoading();
//                    }
//                })
//                .subscribe(new Subscriber<BaseResponse<Integer>>() {
//                    @Override
//                    public void onCompleted() {
//                        mView.hideLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.hideLoading();
//                        KLog.e("getVsStatus:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(BaseResponse<Integer> response) {
//                        if (response != null && response.getStatus() == 0){
//                            if (response.getData() == 1){//开启了虚拟场景
//                                /*确认下当前服务是否在运行，防止安卓卡出现异常时，本地与服务器状态不一致问题*/
//                                SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
//                                VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
//                                boolean isStart = false;
//                                if (vsConfig != null){
//                                    isStart = vsConfig.isStart();
//                                }
//                                if(ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())
//                                || isStart){
//                                    mView.showDialog("提示", ExpandServiceApplication.getInstance().getString(R.string.vl_vs_conflict),
//                                            "","确认",false);
//                                }
//
//                                return;
//                            }
//                        }else if (response != null){
//                            KLog.e("getVsStatus:" + response.getMsg());
//                            return;
//                        }
//                        mView.toast(msg);
//                    }
//                });
//    }

    /**
     * 检查虚拟场景是否在运行
     * @return
     */
    public boolean checkStatus() {
        SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
        VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
        boolean isStart = false;
        if (vsConfig != null) {
            isStart = vsConfig.isStart();
        }
        //服务在运行并且状态保持开启
        if (ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())
                && isStart) {
            mView.showDialog("提示", ExpandServiceApplication.getInstance().getString(R.string.vl_vs_conflict),
                    "", "确认", false);
            return true;
        }
        return false;
    }
}
