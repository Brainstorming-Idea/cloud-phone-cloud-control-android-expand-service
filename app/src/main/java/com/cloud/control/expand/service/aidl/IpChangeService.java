package com.cloud.control.expand.service.aidl;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.entity.baidumap.AddressParse;
import com.cloud.control.expand.service.entity.baidumap.MyIp;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.module.virtualscene.HardwareUtil;
import com.cloud.control.expand.service.module.virtualscene.VirtualScenePresenter;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneService;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.GPSUtil;
import com.cloud.control.expand.service.utils.NetUtil;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;
import com.cloud.control.expand.service.utils.bdmap.BdMapUtils;

import java.util.Arrays;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/16
 */
public class IpChangeService extends Service {
    private static final String TAG = "IpChangeService";

    private Intent vsIntent;
    private VsConfig vsConfig;
    private String ip;

    public IpChangeService() {

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
            if (!TextUtils.isEmpty(ip)){
                //获取IP所在城市，判断是否更换了城市
                RetrofitServiceManager.getMyIp()
                        .subscribe(new Subscriber<MyIp>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage());
                            }

                            @Override
                            public void onNext(MyIp myIp) {
                                if (myIp != null){
                                    String city = myIp.getCity();
                                    //如果城市变化说明切换了IP，需要把虚拟行为场景的中心点切换一下
                                    vsConfig = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance()).getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
                                    if (vsConfig == null){
                                        Log.e(TAG, "未获取到用户的虚拟场景配置信息");
                                        return;
                                    }
                                    if (!TextUtils.isEmpty(city) && !city.equals(vsConfig.getCity())) {
                                        Log.d(TAG, "切换到了："+city);
                                        //1.判断虚拟场景是否在运行 2.停止虚拟场景服务  3.设置新城市的中心点 4.重新启动虚拟场景服务(拿到停止之前的一些状态信息)
                                        if (!vsConfig.isStart()){
                                            return;
                                        }
                                        Log.d(TAG, "正在停止服务");
                                        vsIntent = new Intent(IpChangeService.this, VirtualSceneService.class);
                                        ExpandServiceApplication.getInstance().stopService(vsIntent);
                                        //重新配置参数
                                        getTarLocation(city, city);

                                    }
                                }
                            }
                        });
            }
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            Log.d(TAG, "网络已断开");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IIPChangeService.Stub binder = new IIPChangeService.Stub() {
//        @Override
//        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//            /*检查客户端是否申请了此权限*/
//            int check = checkCallingPermission("com.cloud.control.expand.service.aidl.permission.AGENT_SERVICE");
//            if (check == PackageManager.PERMISSION_DENIED){
//                return false;
//            }
//            String packageName = null;
//            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
//            if(packages != null && packages.length > 0){
//                packageName = packages[0];
//            }else {
//                return false;
//            }
//            if(packageName != null && !packageName.startsWith("com.cloud.phone.control.agent")){
//                return false;
//            }
//            return super.onTransact(code, data, reply, flags);
//        }
        @Override
        public void ipChange(String proxyIp) throws RemoteException {
            //收到这个指令表示安卓卡的IP被切换了
            Log.d(TAG, "接收到的IP："+proxyIp);
            ip = proxyIp;
            //监听下网络状态
            NetUtil.monitorNet(new NetworkCallback());
        }
    };

    private void getTarLocation(String address, String city){
        RetrofitServiceManager.getTarLocation(address, city)
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
