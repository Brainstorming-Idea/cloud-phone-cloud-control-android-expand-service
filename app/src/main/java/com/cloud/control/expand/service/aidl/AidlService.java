package com.cloud.control.expand.service.aidl;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private String ip = "";
    private String changeLoc = "";
    private double[] locArray;
    private boolean isIpChange = false;
    private SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
    private int reGetCount = 0;//重新获取当前位置信息的次数
    private String intentAddress = "";//代理传过来的坐标对应的地址
    private String intentCity = "";//代理传过来的坐标对应的城市
    private boolean isBindService = false;//是否绑定到了虚拟场景服务
    private VirtualSceneService.MyBinder vsBinder;
    private VirtualSceneReceiver vsReceiver = new VirtualSceneReceiver();
    private boolean receiverFlag = true;
    //网络异常重试
    private ExecutorService retryGetLocExecutor = Executors.newCachedThreadPool();

    public AidlService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        vsIntent = new Intent(AidlService.this, VirtualSceneService.class);
//        //绑定服务
        bindService(vsIntent, aidlServiceConnection, Service.BIND_AUTO_CREATE);
        //注册虚拟场景广播
        IntentFilter filter = new IntentFilter(ConstantsUtils.BroadCast.SERVICE_CONNECTION_ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(vsReceiver,filter);
    }

    /*虚拟场景广播接收*/
    public class VirtualSceneReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String action = intent.getAction();
                Log.d(TAG, "hashcode:"+this.hashCode());
                Log.d(TAG, "onReceive:"+action+ ",status:"+intent.getBooleanExtra(ConstantsUtils.BroadCast.KEY_SERVICE_STATUS,false));
                switch (action){
                    case ConstantsUtils.BroadCast.SERVICE_CONNECTION_ACTION:
                        boolean status = intent.getBooleanExtra(ConstantsUtils.BroadCast.KEY_SERVICE_STATUS,false);
                        if (!status){//服务停止
//                            receiverFlag = false;
                            Log.d(TAG, "已停止虚拟场景路线规划");
//                            //重新配置参数并重启服务
                            if (!TextUtils.isEmpty(intentAddress) && !TextUtils.isEmpty(intentCity)) {
                                setAndRestart(intentAddress, intentCity, locArray);
                            }
                        }else {
                            Log.d(TAG, "已开启虚拟场景路线规划");
                        }
                        break;
                }
            }else {
                Log.e(TAG, "intent is null");
            }
        }
    }

    private ServiceConnection aidlServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "虚拟场景服务已创建，AIDLService与之建立了连接");
            isBindService = true;
            vsBinder = (VirtualSceneService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBindService = false;
            Log.e(TAG, "AIDL与虚拟场景服务连接断开");
//            //开始重启服务
//            //重新配置参数并重启服务
//            if (!TextUtils.isEmpty(intentAddress) && !TextUtils.isEmpty(intentCity)) {
//                setAndRestart(intentAddress, intentCity);
//            }
        }
    };

    public class NetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            Log.d(TAG, "网络已连接");

            //解析出当前GPS坐标位置的逆地理编码
//            double[] currCoord = BdMapUtils.getCurrLoc();
//            getCurrReverseCoding(currCoord);
            //要解析代理传过来的坐标不能解析当前的，因为当前定位在不停变化
            Log.d(TAG, changeLoc+"");
            locArray = new double[]{Double.parseDouble(changeLoc.split(",")[0]),Double.parseDouble(changeLoc.split(",")[1])};
            getCurrReverseCoding(locArray);
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
        RetrofitServiceManager.reverseCoding(currCoord)
                .subscribe(new Subscriber<InverseGCInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "获取逆地理编码失败"+e.getMessage());
                        retryGetLocExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                //3秒试一次
                                SystemClock.sleep(3000);
                                getCurrReverseCoding(currCoord);
                            }
                        });
//                        if (reGetCount < 1){
//                            reGetCount++;
//
//                        }else {
//                            reGetCount = 0;
//                            Log.e(TAG, "获取逆地理编码失败,虚拟场景中心位置切换失败:"+e.getMessage());
//                            //关闭
//                        }
                    }

                    @Override
                    public void onNext(InverseGCInfo inverseGCInfo) {
                        reGetCount = 0;
                        //remind 不判断ip了,只要出现主动切换定位的情况（切换ip、虚拟定位到期、ip代理到期（同时购买了虚拟定位））就判断城市是否切换了
//                        //先判断ip有没有变化
//                        if (isIpChange) {
//                            Log.d(TAG, "切换了ip");
//                            isIpChange = false;
//                        }else {
//                            Log.d(TAG, "定位切换了，没有切换ip");
//                            return;
//                        }
                        if (inverseGCInfo != null && inverseGCInfo.getStatus() == 0 && inverseGCInfo.getResult() != null){
                            String address = inverseGCInfo.getResult().getFormatted_address();
                            intentAddress = address;
                            intentCity = inverseGCInfo.getResult().getAddressComponent().getCity();
                            judgeChangeCity(address, inverseGCInfo.getResult().getAddressComponent().getCity(), currCoord);
                        }else {
                            if (currCoord[1] > 0) {
                                judgeChangeCity(currCoord[1] + "°E，" + currCoord[0] + "°N", "",currCoord);
                            }else {
                                judgeChangeCity(currCoord[1] + "°W，" + currCoord[0] + "°N", "", currCoord);
                            }
                        }

                    }
                });
    }

    /**
     * 判断城市是否不同
     * @param address 根据gps坐标解析出来的详细地址
     */
    private void judgeChangeCity(String address, String city, double[] centerCoord) {
        Log.d(TAG, "当前城市："+city);
        //判断是否切换了ip，虚拟场景启动时频繁在频繁更新定位，要结合ip是否切换了城市来确定是否要切换虚拟场景的中心点
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
//            stopService(vsIntent);
            if (isBindService && vsBinder != null && ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())){
                Log.d(TAG, "正在停止服务");
                vsBinder.getService().restartRoute();
            }else {
                Log.e(TAG, "未绑定服务，无法停止虚拟场景");
            }
////            //重新配置参数并重新开启一个路线规划任务
//            setAndRestart(address, city, centerCoord);
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

    /**
     * 配置参数并重启虚拟场景服务
     * @param address 详细地址信息
     * @param city 虚拟定位/IP代理设置的定位城市
     * @param centerCoord 新的中心点GPS坐标
     */
    private void setAndRestart(String address, String city, double[] centerCoord){
        //转换坐标系
//        Log.d(TAG, "新安卓卡GPS："+HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation());
        double[] bdmCoord = GPSUtil.gps84_To_bd09(centerCoord[0], centerCoord[1]);
        //重新启动服务
        Log.d(TAG, "新的中心点坐标(百度)："+ Arrays.toString(bdmCoord));
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
        Log.d(TAG, "虚拟场景服务开始重启");
//        RetrofitServiceManager.geoCoding(address, city)
//                .subscribe(new Subscriber<AddressParse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "baidu:"+e.getMessage() + "");
//                    }
//
//                    @Override
//                    public void onNext(AddressParse addressParse) {
//                        if (addressParse == null) {
//                            return;
//                        }
//                        double[] bdmCoord = new double[]{addressParse.getResult().getLocation().getLat(),addressParse.getResult().getLocation().getLng()};
//                        Log.d(TAG, "百度坐标系：纬度值："+bdmCoord[0]
//                                + ",经度值："+bdmCoord[1]);
//
//                        //转换坐标系
//                        double[] gpsCoord = GPSUtil.bd09_To_gps84(addressParse.getResult().getLocation().getLat(),addressParse.getResult().getLocation().getLng());
//                        Log.d(TAG, "GPS坐标系：纬度值："+gpsCoord[0]
//                                + ",经度值："+gpsCoord[1]);
////                        /*设置GPS位置到安卓卡*/
//                        HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).setGpsLocation(gpsCoord[0] +
//                                ";" + gpsCoord[1]);
//                        Log.d(TAG, "新安卓卡GPS："+HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation());
//                        //重新启动服务
//                        Log.d(TAG, "新的中心点坐标："+ Arrays.toString(bdmCoord));
//                        //重新设置参数并保存
//                        vsConfig.setStart(true);
//                        vsConfig.setCenterCoords(bdmCoord);
//                        vsConfig.setCity(city);
//                        vsConfig.setAddress(address);
//                        helper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
//                        vsIntent.putExtra("scene_type", vsConfig.getSceneType());
//                        vsIntent.putExtra("start_loc", bdmCoord);
//                        vsIntent.putExtra("terminal_loc", BdMapUtils.getTerminalPoint(bdmCoord,vsConfig.getRadius()));
//                        vsIntent.putExtra("radius", vsConfig.getRadius());
//                        startService(vsIntent);
//                        Log.d(TAG, "虚拟场景服务开始重启");
//                    }
//                });
    }

    @Override
    public void onDestroy() {
        unbindService(aidlServiceConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(vsReceiver);
        super.onDestroy();
    }
}
