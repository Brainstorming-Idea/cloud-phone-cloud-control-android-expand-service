package com.cloud.control.expand.service.retrofit.manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cloud.control.expand.service.entity.BaseResponse;
import com.cloud.control.expand.service.entity.ChangeMachineStatusEntity;
import com.cloud.control.expand.service.entity.CityListEntity;
import com.cloud.control.expand.service.entity.CloseProxyEntity;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.entity.ModelInfoEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.SwitchProxyTypeEntity;
import com.cloud.control.expand.service.entity.TimeInfoEntity;
import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;
import com.cloud.control.expand.service.entity.VirtualLocationEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;
import com.cloud.control.expand.service.entity.baidumap.AddressParse;
import com.cloud.control.expand.service.entity.baidumap.MyIp;
import com.cloud.control.expand.service.entity.baidumap.RoutePlan;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.api.IUrls;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.NetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Author：abin
 * Date：2020/9/28
 * Description：整个网络通信服务的启动控制，必须先调用初始化函数才能正常使用网络通信接口
 */
public class RetrofitServiceManager {

    private static IUrls mRetrofitService;
    private static final String LOG_TAG = "okhttp";

    private RetrofitServiceManager() {
        throw new AssertionError();
    }

    /**
     * 初始化网络通信服务
     *
     * @param host
     */
    public static void init(String host) {
        // 指定缓存路径,缓存大小100Mb
//        Cache cache = new Cache(new File(ExpandServiceApplication.getInstance().getApplicationContext().getCacheDir(), "HttpCache"),
//                1024 * 1024 * 100);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()/*.cache(cache)*/
                .retryOnConnectionFailure(false)
//                .addInterceptor(sLoggingInterceptor)
                .addInterceptor(loggingInterceptor())
                .addInterceptor(headerInterceptor())
//                .addInterceptor(sRewriteCacheControlInterceptor)
//                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(host)
                .build();

        mRetrofitService = retrofit.create(IUrls.class);


    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(ExpandServiceApplication.getInstance().getApplicationContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                KLog.e("no network");
            } else {
                KLog.e("network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(ExpandServiceApplication.getInstance().getApplicationContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                KLog.e("有网");
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
                        .build();
            } else {
                KLog.e("无网");
                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
//                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                KLog.e("LogTAG", "request.body() == null");
            }
            //打印url信息
//            KLog.e(request.url() + (request.body() != null ? "?" + _parseParams(request.body(), requestBuffer) : ""));
            final Response response = chain.proceed(request);
            KLog.e("response = " + response.toString());
            return response;
        }
    };

    /**
     * 日志拦截器
     */
    private static final HttpLoggingInterceptor loggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(LOG_TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//日志打印级别
    }

    private static Interceptor headerInterceptor(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Connection","close")
                        .build();
                return chain.proceed(request);
            }
        };
    }

    @NonNull
    private static String _parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }

    /************************************ API *******************************************/

    /**
     * 获取扩展服务列表
     *
     * @return
     */
    public static Observable<ExpandServiceListEntity> getExtendServiceList() {
        return mRetrofitService.getExtendServiceList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取扩展服务记录
     *
     * @return
     */
    public static Observable<ExpandServiceRecordEntity> getExtendServiceRecord() {
        Map<String, Object> map = new HashMap<>();
        map.put("sn", ExpandServiceApplication.getInstance().getCardSn());
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return mRetrofitService.getExtendServiceRecord(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取IP切换方式
     *
     * @return
     */
    public static Observable<SwitchProxyTypeEntity> getChangeIpType() {
        return mRetrofitService.getChangeIpType(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取城市列表
     *
     * @return
     */
    public static Observable<CityListEntity> getCityList() {
        return mRetrofitService.getCityList(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取城市IP
     *
     * @return
     */
    public static Observable<ResponseEntity> getCityIp() {
        return mRetrofitService.getCityIp(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 一键切换IP
     *
     * @param cityList
     * @param ipChangeType
     * @return
     */
    public static Observable<ResponseEntity> changeCityIp(String[] cityList, int ipChangeType) {
        Map<String, Object> map = new HashMap<>();
        map.put("sn", ExpandServiceApplication.getInstance().getCardSn());
        map.put("cityList", cityList);
        map.put("ipChangeType", ipChangeType);
        KLog.e("changeCityIp json " + new Gson().toJson(map));
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return mRetrofitService.changeCityIp(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 关闭代理
     *
     * @return
     */
    public static Observable<ResponseEntity> closeProxy() {
        Map<String, Object> map = new HashMap<>();
        String[] snArray = new String[]{ExpandServiceApplication.getInstance().getCardSn()};
        map.put("snList", snArray);
        KLog.e("closeProxy json " + new Gson().toJson(map));
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return mRetrofitService.closeProxy(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置经纬度
     *
     * @param longitude
     * @param latitude
     * @param city
     * @return
     */
    public static Observable<VirtualLocationEntity> setGps(String longitude, String latitude, String city) {
        Map<String, Object> map = new HashMap<>();
        map.put("sn", ExpandServiceApplication.getInstance().getCardSn());
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("city", city);
        KLog.e("setGps json " + new Gson().toJson(map));
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return mRetrofitService.setGps(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public static Observable<VirtualLocationInfoEntity> getGps() {
        return mRetrofitService.getGps(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取卡IP
     *
     * @return
     */
    public static Observable<CloseProxyEntity> getCardIp() {
        return mRetrofitService.getCardIp(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取对应的手机型号
     *
     * @return
     */
    public static Observable<ChangeMachineStatusEntity> getPhoneModel() {
        return mRetrofitService.getPhoneModel(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取手机品牌和手机类型
     *
     * @return
     */
    public static Observable<PhoneBrandModelEntity> getPhoneBrandModel() {
        return mRetrofitService.getPhoneBrandModel(ExpandServiceApplication.getInstance().getCardSn())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取手机的配置参数
     *
     * @param selectModelInfoEntity
     * @return
     */
    public static Observable<PhoneModelInfoEntity> getPhoneModifyInfo(ModelInfoEntity selectModelInfoEntity) {
        String mobileType = selectModelInfoEntity.getModel();
        return mRetrofitService.getPhoneModifyInfo(ExpandServiceApplication.getInstance().getCardSn(), mobileType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改手机的配置参数
     *
     * @param configEntity
     * @return
     */
    public static Observable<ResponseEntity> modifyCard(UpdatePhoneConfigEntity configEntity) {
        KLog.e("modifyCard json " + new Gson().toJson(configEntity));
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(configEntity));
        return mRetrofitService.modifyCard(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static Observable<TimeInfoEntity> getCurrentTime() {
        return mRetrofitService.getCurrentTime()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**/
    public static Observable<AddressParse> getTarLocation(String address, String city) {
        Map<String, Object> options = new HashMap<>(10);
        options.put("address", address);
        options.put("city", city);
        options.put("ak", ConstantsUtils.BaiDuMap.AK);
//        options.put("ret_coordtype", ConstantsUtils.BaiDuMap.BD_COORD_TYPE);
        options.put("output", "json");
        return mRetrofitService
                .parseAddress(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 芝麻代理获取手机IP的接口
     * {"ip":"119.139.198.238","country":"中国","area":"0","province":"广东省","city":"深圳市","isp":"电信","timestamp":1615189282}
     */
    public static Observable<MyIp> getMyIp() {
        return mRetrofitService.getMyIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RoutePlan> getDrivePlan(String origin, String destination){
        Map<String,Object> map = new HashMap<>(10);
        map.put("ak",ConstantsUtils.BaiDuMap.AK);
        map.put("origin", origin);
        map.put("destination", destination);
        map.put("tactics", 0);//0：常规路线,1：不走高速2：躲避拥堵3：距离较短
        return mRetrofitService.getDriveRoute(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RoutePlan> getWalkPlan(String origin, String destination){
        Map<String,Object> map = new HashMap<>(10);
        map.put("ak",ConstantsUtils.BaiDuMap.AK);
        map.put("origin", origin);
        map.put("destination", destination);
        return mRetrofitService.getWalkRoute(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BaseResponse<Object>> setVSStatus(int typeId, String sn, int isOpen){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serviceTypeId", typeId);
        jsonObject.addProperty("sn", sn);
        jsonObject.addProperty("isOpen",isOpen);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(jsonObject));
        return mRetrofitService.setVSStatus(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BaseResponse<Integer>> getVsStatus(String sn){
        return mRetrofitService.getVsStatus(sn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
