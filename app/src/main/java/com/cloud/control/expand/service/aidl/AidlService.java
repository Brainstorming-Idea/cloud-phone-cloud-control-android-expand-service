package com.cloud.control.expand.service.aidl;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.entity.baidumap.AddressParse;
import com.cloud.control.expand.service.entity.baidumap.InverseGCInfo;
import com.cloud.control.expand.service.entity.baidumap.MyIp;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.module.virtualscene.HardwareUtil;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneService;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.GPSUtil;
import com.cloud.control.expand.service.utils.NetUtil;
import com.cloud.control.expand.service.utils.ServerUtils;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;
import com.cloud.control.expand.service.utils.bdmap.BdMapUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/16
 */
public class AidlService extends Service {
    private static final String TAG = "AidlService";
    

    private Intent vsIntent;
    private VsConfig vsConfig;
    private String ip;
    private String changeLoc;
    private boolean isIpChange = false;
    private SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
    private int reGetCount = 0;//重新获取当前位置信息的次数
    public AidlService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public class NetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            Log.d(TAG, "网络已连接");
            //解析出当前GPS坐标位置的逆地理编码
            double[] currCoord = BdMapUtils.getCurrLoc();
            getCurrReverseCoding(currCoord);

//            double[] locArray = new double[]{Double.parseDouble(changeLoc.split(",")[0]),Double.parseDouble(changeLoc.split(",")[1])};
//            getCurrReverseCoding(locArray);
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            Log.d(TAG, "网络已断开");
        }
    }

    /**
     * 获取当前位置的编码
     * @param currCoord
     */
    private void getCurrReverseCoding(double[] currCoord){
        //先判断ip有没有变化
        if (isIpChange) {
            isIpChange = false;
        }else {
            Log.d(TAG, "定位切换了，没有切换ip");
            return;
        }
        RetrofitServiceManager.reverseCoding(currCoord)
                .subscribe(new Subscriber<InverseGCInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        reGetCount++;
                        if (reGetCount <= 10){
                            getCurrReverseCoding(currCoord);
                        }else {
                            Log.e(TAG, "获取逆地理编码失败,虚拟场景中心位置切换失败:"+e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(InverseGCInfo inverseGCInfo) {
                        reGetCount = 0;
                        if (inverseGCInfo != null && inverseGCInfo.getStatus() == 0 && inverseGCInfo.getResult() != null){
                            String address = inverseGCInfo.getResult().getFormatted_address();
                            judgeChangeCity(address, inverseGCInfo.getResult().getAddressComponent().getCity());
                        }else {
                            if (currCoord[1] > 0) {
                                judgeChangeCity(currCoord[1] + "°E，" + currCoord[0] + "°N", "");
                            }else {
                                judgeChangeCity(currCoord[1] + "°W，" + currCoord[0] + "°N", "");
                            }
                        }

                    }
                });
    }

    /**
     * 判断城市是否不同
     * @param address 根据gps坐标解析出来的详细地址
     */
    private void judgeChangeCity(String address, String city) {
        Log.d(TAG, "当前城市："+city);
        //如果城市变化说明切换了IP，需要把虚拟行为场景的中心点切换一下
        vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
        if (vsConfig == null){
            Log.e(TAG, "未获取到用户的虚拟场景配置信息");
            return;
        }
        if (!TextUtils.isEmpty(city) && !city.equals(vsConfig.getCity())) {
            Log.d(TAG, "切换到了："+city);
            //1.判断虚拟场景是否在运行 2.停止虚拟场景服务  3.设置新城市的中心点 4.重新启动虚拟场景服务(拿到停止之前的一些状态信息)
            if (!vsConfig.isStart()){
                Log.d(TAG, "服务未启动！");
                return;
            }
            Log.d(TAG, "正在停止服务");
            vsIntent = new Intent(AidlService.this, VirtualSceneService.class);
            ExpandServiceApplication.getInstance().stopService(vsIntent);
            //重新配置参数并重启服务
            setAndRestart(address, city);
        }else {
            Log.e(TAG, "城市未发生改变:"+city+","+vsConfig.getCity());
        }
//        //获取IP所在城市，判断是否更换了城市
//        RetrofitServiceManager.getMyIp()
//                .subscribe(new Subscriber<MyIp>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, e.getMessage());
//                        if (reGetCount <= 10){
//                            judgeChangeCity(address, city);
//                        }else {
//                            Log.e(TAG, "无法获取IP所在城市，虚拟场景中心位置切换失败");
//                        }
//                    }
//
//                    @Override
//                    public void onNext(MyIp myIp) {
//                        if (myIp != null){
//                            String city = myIp.getCity();
//                            Log.d(TAG, "当前城市："+city);
//                            //如果城市变化说明切换了IP，需要把虚拟行为场景的中心点切换一下
//                            vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
//                            if (vsConfig == null){
//                                Log.e(TAG, "未获取到用户的虚拟场景配置信息");
//                                return;
//                            }
//                            if (!TextUtils.isEmpty(city) && !city.equals(vsConfig.getCity())) {
//                                Log.d(TAG, "切换到了："+city);
//                                //1.判断虚拟场景是否在运行 2.停止虚拟场景服务  3.设置新城市的中心点 4.重新启动虚拟场景服务(拿到停止之前的一些状态信息)
//                                if (!vsConfig.isStart()){
//                                    Log.d(TAG, "服务未启动！");
//                                    return;
//                                }
//                                Log.d(TAG, "正在停止服务");
//                                vsIntent = new Intent(AidlService.this, VirtualSceneService.class);
//                                ExpandServiceApplication.getInstance().stopService(vsIntent);
//                                //重新配置参数并重启服务
//                                setAndRestart(address, city);
//                            }else {
//                                Log.e(TAG, "城市未发生改变:"+city+","+vsConfig.getCity());
//                            }
//                        }else {
//                            Log.e(TAG, "myIP is NUll");
//                        }
//                    }
//                });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private AEIPCService.Stub binder = new AEIPCService.Stub() {
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            /*检查客户端是否申请了此权限*/
            int check = checkCallingPermission("com.cloud.control.expand.service.aidl.permission.AGENT_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED){
                Log.e(TAG, "权限错误");
                return false;
            }
            String packageName = getAppPkg(Binder.getCallingPid());
//            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
//            if(packages != null && packages.length > 0){
//                packageName = packages[0];
//            }else {
//                Log.e(TAG, "包名不合法");
//                return false;
//            }
            if(packageName != null && !packageName.startsWith("com.cloud.phone.control.agent")){
                Log.e(TAG, "包名错误:"+packageName);
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
        @Override
        public void ipChange(String proxyIp) throws RemoteException {
            //收到这个指令表示安卓卡的IP被切换了
            Log.d(TAG, "接收到的IP："+proxyIp);
            ip = proxyIp;
            isIpChange = true;
//            //监听下网络状态
//            NetUtil.monitorNet(new NetworkCallback());
        }

        @Override
        public void locChange(String loc) throws RemoteException {
            // 1.监听下网络状态
            // 2.网络可以访问后开始获取当前定位的城市，
            // 3.然后判断是否切换了ip,若为true ->
            // 4.再判断城市是否发生变化，true:->同时把isIpChange置为false
            // 5.若变化则重启虚拟场景
            Log.d(TAG, "接收到的定位："+loc);
            changeLoc = loc;
            NetUtil.monitorNet(new NetworkCallback());
        }

        @Override
        public int getVsStatus() throws RemoteException {
            VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
            if (vsConfig != null){
                if (ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())
                        && vsConfig.isStart()){
                    return 1;
                }else {
                    vsConfig.setStart(false);
                    helper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
                }
            }
            return 0;
        }
    };

    /**
     * 获取调用者的包名
     * @param pid
     * @return
     */
    private String getAppPkg(int pid) {
        String processName = "";
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : list) {
                if (info.pid == pid) {
                    processName = info.processName;
                    break;
                }
            }
        }
        return processName;
    }

    private void setAndRestart(String address, String city){
        RetrofitServiceManager.geoCoding(address, city)
                .subscribe(new Subscriber<AddressParse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "baidu:"+e.getMessage() + "");
                    }

                    @Override
                    public void onNext(AddressParse addressParse) {
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
                        Log.d(TAG, "新安卓卡GPS："+HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation());
                        //重新启动服务
                        Log.d(TAG, "新的中心点坐标："+ Arrays.toString(bdmCoord));
                        //重新设置参数并保存
                        vsConfig.setStart(true);
                        vsConfig.setCenterCoords(bdmCoord);
                        vsConfig.setCity(city);
                        vsConfig.setAddress(address);
                        helper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
                        vsIntent.putExtra("scene_type", vsConfig.getSceneType());
                        vsIntent.putExtra("start_loc", bdmCoord);
                        vsIntent.putExtra("terminal_loc", BdMapUtils.getTerminalPoint(bdmCoord,vsConfig.getRadius()));
                        vsIntent.putExtra("radius", vsConfig.getRadius());
                        startService(vsIntent);
                        Log.d(TAG, "虚拟场景服务已重启");
                    }
                });
    }
}
