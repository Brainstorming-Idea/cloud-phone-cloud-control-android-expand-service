package com.cloud.control.expand.service.retrofit.api;

import com.cloud.control.expand.service.entity.ChangeMachineStatusEntity;
import com.cloud.control.expand.service.entity.CityListEntity;
import com.cloud.control.expand.service.entity.CloseProxyEntity;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.SwitchProxyTypeEntity;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author：abin
 * Date：2020/9/28
 * Description：API接口地址
 */
public interface IUrls {

    //获取扩展服务列表 （已弃用）
    @GET("api/public/v2.0/extendService/getAllExtendServiceAndroid")
    Observable<ExpandServiceListEntity> getExtendServiceList();

    //获取扩展服务记录
    @POST("api/user/v2.0/extendServiceRecord/getExtendServiceRecordBySn")
    Observable<ExpandServiceRecordEntity> getExtendServiceRecord(@Body RequestBody body);

    //获取IP切换方式
    @GET("api/wsi/v1/cardChangeIp/getChangeIpTypeAndroid")
    Observable<SwitchProxyTypeEntity> getChangeIpType(@Query("sn") String sn);

    //获取城市列表
    @GET("api/wsi/v1/cardChangeIp/getCityList")
    Observable<CityListEntity> getCityList(@Query("sn") String sn);

    //获取城市IP
    @GET("api/wsi/v1/cardChangeIp/getCityIpAndroid")
    Observable<ResponseEntity> getCityIp(@Query("sn") String sn);

    //一键切换IP
    @POST("api/wsi/v2.0/cardChangeIp/changeCityIpAndroid")
    Observable<ResponseEntity> changeCityIp(@Body RequestBody body);

    //关闭代理
    @POST("api/wsi/v2.0/cardChangeIp/closeProxyAndroid")
    Observable<ResponseEntity> closeProxy(@Body RequestBody body);

    //获取经纬度
    @GET("api/wsi/v1/instruct/getGpsAndroid")
    Observable<VirtualLocationInfoEntity> getGps(@Query("sn") String sn);

    //设置经纬度
    @POST("api/wsi/v2.0/gps/setGpsAndroid")
    Observable<VirtualLocationEntity> setGps(@Body RequestBody body);

    //获取卡IP
    @GET("api/wsi/v1/cardChangeIp/getIPBySnAndroid")
    Observable<CloseProxyEntity> getCardIp(@Query("sn") String sn);

    //获取手机的型号
    @GET("api/user/v1/modelSn/getBySns")
    Observable<ChangeMachineStatusEntity> getPhoneModel(@Query("sns") String sn);

    //获取手机品牌和手机类型
    @GET("api/user/v1/brand/brandList")
    Observable<PhoneBrandModelEntity> getPhoneBrandModel(@Query("sn") String sn);

    //获取手机的配置参数
    @GET("api/wsi/v1/instruct/getModifyInfoAndroid")
    Observable<PhoneModelInfoEntity> getPhoneModifyInfo(@Query("sns") String sn, @Query("mobileTypes") String mobileType);

    //修改手机的配置参数
    @POST("api/wsi/v1/instruct/modifyCardAndroid")
    Observable<ResponseEntity> modifyCard(@Body RequestBody body);

}
