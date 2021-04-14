package com.cloud.control.expand.service.module.virtualscene;

import android.text.TextUtils;
import android.util.Log;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.IBasePresenter;
import com.cloud.control.expand.service.entity.BaseResponse;
import com.cloud.control.expand.service.entity.ServerErrorCode;
import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.entity.baidumap.AddressParse;
import com.cloud.control.expand.service.entity.baidumap.InverseGCInfo;
import com.cloud.control.expand.service.entity.baidumap.MyIp;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.GPSUtil;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;
import com.cloud.control.expand.service.utils.bdmap.BdMapUtils;

import java.math.BigDecimal;
import java.util.Arrays;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/3
 */
public class VirtualScenePresenter implements IBasePresenter, IVirtualScene {
    private static final String TAG = "VirtualScenePresenter";

    private VirtualSceneView mView;
    private int radius = 0;//半径，单位米
    private double[] centerCoord = new double[2];//中心点坐标（WGS84坐标系）0:维度：1：经度
    public static String currCity = "";
    private SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
    private VsConfig vsConfig = null;
    public VirtualScenePresenter() {
    }

    public VirtualScenePresenter(VirtualSceneView mView) {
        this.mView = mView;
        vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
    }

    @Override
    public void getData() {

    }

    @Override
    public void getCenterLoc() {
        String gpsLoc = HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation();
        double lat = Double.parseDouble(gpsLoc.split(";")[0]);
        double lng = Double.parseDouble(gpsLoc.split(";")[1]);
        centerCoord = GPSUtil.gps84_To_bd09(lat, lng);
        BigDecimal bdLat = new BigDecimal(Double.toString(centerCoord[0]));
        BigDecimal bdLng = new BigDecimal(Double.toString(centerCoord[1]));
        /*保留4位小数，与H5返回的坐标精度一致*/
        String latStr = bdLat.setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString();
        String lngStr = bdLng.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
        centerCoord[0] = Double.parseDouble(latStr);
        centerCoord[1] = Double.parseDouble(lngStr);
        mView.loadCenterLocation(centerCoord);
        /*获取中心点坐标的逆地理编码*/
        RetrofitServiceManager.reverseCoding(centerCoord)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<InverseGCInfo>bindToLife())
                .subscribe(new Subscriber<InverseGCInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        Log.e(TAG, "reserveCoding error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(InverseGCInfo inverseGCInfo) {
                        if (inverseGCInfo != null && inverseGCInfo.getStatus() == 0 && inverseGCInfo.getResult() != null){
                            String address = inverseGCInfo.getResult().getFormatted_address();
                            mView.loadCenterLocDes(address);
                            mView.setCenterCity(inverseGCInfo.getResult().getAddressComponent().getCity());
                        }else {
                            if (centerCoord[1] > 0) {//不管南纬了:)
                                mView.loadCenterLocDes(lngStr + "°E，" + latStr + "°N");
                            }else {
                                mView.loadCenterLocDes(lngStr + "°W，" + latStr + "°N");
                            }
                        }
                    }
                });

//        //先获取一下IP所在城市
//        RetrofitServiceManager.getMyIp()
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        mView.showLoading();
//                    }
//                })
//                .compose(mView.<MyIp>bindToLife())
//                .subscribe(new Subscriber<MyIp>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "myIP:"+e.getMessage()+"");
//                        mView.hideLoading();
//                    }
//
//                    @Override
//                    public void onNext(MyIp myIp) {
//                        if (myIp != null){
//                            String province = myIp.getProvince();
//                            String city = myIp.getCity();
//                            currCity = city;
//                            Log.d(TAG, city+"");
//                            if (vsConfig != null && !TextUtils.isEmpty(vsConfig.getAddress()) && !TextUtils.isEmpty(vsConfig.getCity())){
//                                mView.loadCenterLocDes(vsConfig.getAddress());
//                                mView.setCenterCity(vsConfig.getCity());
//                                getTarLocation(vsConfig.getAddress(),vsConfig.getCity());
//                            }else{
//                                mView.loadCenterLocDes(province+city);
//                                mView.setCenterCity(city);
//                                getTarLocation(province+city,city);
//                            }
//
//                        }
//                    }
//                });

    }

    public String getCurrCity(){
        return currCity;
    }

    private void getTarLocation(String address, String city){
        RetrofitServiceManager.geoCoding(address, city)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<AddressParse>bindToLife())
                .subscribe(new Subscriber<AddressParse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "baidu:"+e.getMessage() + "");
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(AddressParse addressParse) {
                        mView.hideLoading();
                        if (addressParse == null) {
                            return;
                        }
                        double[] bdmCoord = new double[]{addressParse.getResult().getLocation().getLat(),addressParse.getResult().getLocation().getLng()};
                        Log.d(TAG, "百度坐标系：纬度值："+bdmCoord[0]
                        + ",经度值："+bdmCoord[1]);

                        //转换坐标系
                        double[] gpsCoord = GPSUtil.bd09_To_gps84(addressParse.getResult().getLocation().getLat(),addressParse.getResult().getLocation().getLng());
                        Log.d(TAG, "GPS坐标系：纬度值："+gpsCoord[0]
                                + ",经度值："+gpsCoord[1]);
//                        /*设置GPS位置到安卓卡*/
                        HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).setGpsLocation(gpsCoord[0] +
                                ";" + gpsCoord[1]);
                        Log.d(TAG, "安卓卡GPS："+HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation());
                        centerCoord = bdmCoord;
                        mView.loadCenterLocation(centerCoord);
                    }
                });
    }



    /**
     * 计算运动区域
     * 一经度：30.9*3600 * cos(维度值)
     * 一秒维度：约30.9米 一维度的距离：30.9*3600
     * 一秒经纬度约33米
     * @param radius 半径 单位：米
     */
    @Override
    public double[] getTerminalPoint(int radius){
        if (centerCoord == null || centerCoord[0] == 0 || centerCoord[1] == 0){
            VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG,VsConfig.class);
            if(vsConfig != null){
                centerCoord = vsConfig.getCenterCoords();
            }else{
                throw new RuntimeException("中心点坐标获取失败！");
            }
        }
        return BdMapUtils.getTerminalPoint(centerCoord, radius);
//        GlobalCoordinates source = new GlobalCoordinates(29.490295, 106.486654);
//        GlobalCoordinates target = new GlobalCoordinates(29.615467, 106.486654);
//
//        GlobalCoordinates source1 = new GlobalCoordinates(29.490295, 106.486654);
//        GlobalCoordinates target1 = new GlobalCoordinates(29.490295, 106.581515);
//
////        double meter1 = getDistanceMeter(source, target, Ellipsoid.Sphere);
//        double meter2 = getDistanceMeter(source, target, Ellipsoid.WGS84);
//        double lngMeter = getDistanceMeter(source1,target1,Ellipsoid.WGS84);
////        System.out.println("Sphere坐标系计算结果："+meter1 + "米");
//        System.out.println("WGS84坐标系计算结果："+meter2 + "米");
//        double l = meter2/(29615467 - 29490295);
//        double m = lngMeter / (106581515 - 106486654);
//        Log.d(TAG,"单位经度长度："+l);
//        Log.d(TAG,"单位维度长度："+m);
    }

    /**
     * 设置虚拟场景的状态
     * @param typeId
     * @param sn
     * @param isOpen
     */
    public void setVsStatus(int typeId, String sn, int isOpen){
        RetrofitServiceManager.setVSStatus(typeId, sn, isOpen)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<BaseResponse<Object>>bindToLife())
                .subscribe(new Subscriber<BaseResponse<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        String startError = ExpandServiceApplication.getInstance().getString(R.string.vs_start_failed);
                        String stopError = ExpandServiceApplication.getInstance().getString(R.string.vs_stop_failed);
                        mView.toast(isOpen == 1 ? startError : stopError);
                        Log.e(TAG, "setStatus:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<Object> base) {
                        if (base != null){
                            if (base.getStatus() == 0) {
                                mView.updateStatus();
                            }else if (base.getRetCode() == ServerErrorCode.E_30011){
                                mView.hideLoading();
                                mView.dialog("提示", ExpandServiceApplication.getInstance().getString(R.string.expand_service_deadline),
                                        "", "确认");
                            }

                        }
                    }
                });
    }
}