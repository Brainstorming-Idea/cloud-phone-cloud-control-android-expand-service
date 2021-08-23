package com.cloud.control.expand.service.module.virtualscene;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.entity.BaseResponse;
import com.cloud.control.expand.service.entity.SceneType;
import com.cloud.control.expand.service.entity.ServerErrorCode;
import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.entity.baidumap.RoutePlan;
import com.cloud.control.expand.service.entity.baidumap.Steps;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.GPSUtil;
import com.cloud.control.expand.service.utils.NetUtil;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;
import com.cloud.control.expand.service.utils.bdmap.BdMapUtils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.obermuhlner.math.big.BigDecimalMath;
import rx.Subscriber;

/**
 * @author wangyou
 * @desc: 虚拟场景服务
 * @date :2021/3/11
 */
public class VirtualSceneService extends Service {
    private static final String TAG = "VirtualSceneService";
    private float walkSpeed = 2.0f;//m/s
    private float runSpeed = 3.0f;//m/s
    private float driveSpeed = 30.0f; // m/s
    private double[] centerCoord;
    private double[] startLoc;
    private double[] terminalLoc;
    private int radius;
    private int reTryCount = 0;
    /**
     * 是否开启了路线规划，默认停止
     */
    private volatile AtomicBoolean isStart = new AtomicBoolean(false);
    private volatile AtomicBoolean isRestart = new AtomicBoolean(false);//是否是重启扩展服务
    private double[] newStartCoord;//切换ip重启服务后，新的启动坐标
    //cpu密集型计算，最大线程数为CPU核心数+1，
//    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
//            Runtime.getRuntime().availableProcessors() + 1, 60, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<Runnable>(1024),
//            new BasicThreadFactory.Builder().namingPattern("vs-thread-pool-%d").daemon(true).build());
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Intent broadIntent;
    private MathContext mathContext = new MathContext(6);//指定精度

    public VirtualSceneService() {
    }

    private CallBack callBack;

    public interface CallBack {
        void onStart();

        void onStop();

        void onError(String msg);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 获取服务的运行状态
     * @return 服务是否正在运行
     */
    public boolean getStatus(){
        return isStart.get();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public VirtualSceneService getService() {
            return VirtualSceneService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadIntent = new Intent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLoc = intent.getDoubleArrayExtra("start_loc");
        centerCoord = startLoc;
        terminalLoc = intent.getDoubleArrayExtra("terminal_loc");
        radius = intent.getIntExtra("radius", 0);
        int type = intent.getIntExtra("scene_type", 0);
        SceneType sceneType = SceneType.getVirtualScene(type);
        String origin = startLoc[0] + "," + startLoc[1];
        String destination = terminalLoc[0] + "," + terminalLoc[1];
        isStart.set(true);
        //请求百度接口获取路线规划中的坐标点
        assert sceneType != null;
        getRoutePlan(sceneType, origin, destination);
        Log.d(TAG, "虚拟场景服务已启动");
        sendServiceStatus(true);
        return super.onStartCommand(intent, flags, startId);//TODO 确定服务重启的策略
    }

    /**
     * 获取路线规划路径，NOTE:百度返回的path中的坐标格式：经度，维度 "path": "116.33970647581,40.009358617237"
     *
     * @param sceneType
     * @param origin
     * @param destination
     */
    private void getRoutePlan(SceneType sceneType, String origin, String destination) {
        switch (sceneType) {
            case SIT:
                if (callBack != null){
                    callBack.onStart();
                }
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //创造一个循环，服务停止时退出循环
                        while (isStart.get()){
                            SystemClock.sleep(100);
                        };
                        sendServiceStatus(false);//发送状态广播以判定是否要重启服务
                    }
                });
                break;
            case WALK:
                RetrofitServiceManager.getWalkPlan(origin, destination)
                        .subscribe(new Subscriber<RoutePlan>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "WALK:ERROR:" + e.getMessage());
//                                if (callBack != null) {
//                                    callBack.onError("WALK:" + e.getMessage());
//                                }
                                reGetRoutePlan(SceneType.WALK);
                            }

                            @Override
                            public void onNext(final RoutePlan routePlan) {
                                if (routePlan != null && routePlan.getStatus() == 0) {
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isStart.get()){
                                                Log.d(TAG, "服务已停止");
                                                sendServiceStatus(false);
                                                return;
                                            }
                                            setGpsLocation(supplyPointsAccurate(routePlan, 10),SceneType.WALK);
                                            sendServiceStatus(false);
                                        }
                                    });
                                }else {
                                    if (routePlan != null){
                                        Log.e(TAG, "WALK:路线规划失败，status:"+routePlan.getStatus());
                                        reGetRoutePlan(SceneType.WALK);
                                    }
                                }
                            }
                        });
                break;
            case RUN:
                RetrofitServiceManager.getWalkPlan(origin, destination)
                        .subscribe(new Subscriber<RoutePlan>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "RUN:ERROR:" + e.getMessage());

                                reGetRoutePlan(SceneType.RUN);

                            }

                            @Override
                            public void onNext(final RoutePlan routePlan) {
                                if (routePlan != null && routePlan.getStatus() == 0) {
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isStart.get()){
                                                Log.d(TAG, "服务已停止");
                                                sendServiceStatus(false);
                                                return;
                                            }
                                            setGpsLocation(supplyPointsAccurate(routePlan, 20),SceneType.RUN);
                                            sendServiceStatus(false);
                                        }
                                    });

                                }else {
                                    if (routePlan != null){
                                        Log.e(TAG, "RUN:路线规划失败，status:"+routePlan.getStatus());
                                        reGetRoutePlan(SceneType.RUN);
                                    }
                                }
                            }
                        });
                break;
            case DRIVE:
                RetrofitServiceManager.getDrivePlan(origin, destination)
                        .subscribe(new Subscriber<RoutePlan>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "DRIVE:" + e.getMessage());
                                reGetRoutePlan(SceneType.DRIVE);

                            }

                            @Override
                            public void onNext(final RoutePlan routePlan) {
                                if (routePlan != null && routePlan.getStatus() == 0) {
                                    calDistance(routePlan);
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isStart.get()){
                                                Log.d(TAG, "服务已停止");
                                                sendServiceStatus(false);
                                                return;
                                            }
                                            setGpsLocation(supplyPointsAccurate(routePlan, 50),SceneType.DRIVE);
                                            sendServiceStatus(false);
                                        }
                                    });
                                }else {
                                    if (routePlan != null){
                                        Log.e(TAG, "DRIVE:路线规划失败，status:"+routePlan.getStatus());
                                        reGetRoutePlan(SceneType.DRIVE);
                                    }
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }


    /**
     * 第一种方案：根据两个坐标点距离，在单位时间内设置坐标点，距离不同则速度不同（TODO:测试一下百度给的坐标点间距大小）
     * 两点距离太长则需要补点
     *
     * @param eachDistance 根据不同场景，确定补点后每段的距离
     * @return 补点后的坐标点集合
     */
    public List<double[]> supplyPoints(RoutePlan routePlan, float eachDistance) {
        long startTime = System.currentTimeMillis();
//        float unitLat = ConstantsUtils.BaiDuMap.UNIT_LAT * 3600;//一维度对应的距离
        float unitLat = ConstantsUtils.BaiDuMap.ONE_DEGREE_LAT;
        List<double[]> coords = getCoords(routePlan);//百度给的点集合，已转为GPS坐标
        Log.d(TAG, "百度点的个数：" + coords.size());
        List<double[]> supplyPoints = new ArrayList<>();//需要返回的点集合
        /*判断每两个坐标点是否需要补点，需要则计算出补出的坐标点，添加到坐标集合中*/
        for (int i = 0; i < coords.size(); i++) {//遍历每一段路程
            if (i == coords.size() - 1) {
                supplyPoints.add(coords.get(i));//把最后一个点加进去
                break;
            }
            double[] start = coords.get(i);//起点
            double[] end = coords.get(i + 1);//终点
            float distance;//百度给的每两点间的距离
            GlobalCoordinates from = new GlobalCoordinates(start[0], start[1]);
            GlobalCoordinates to = new GlobalCoordinates(end[0], end[1]);
            DecimalFormat df = new DecimalFormat("0.0");
            distance = Float.parseFloat(df.format(getDistanceMeter(from, to, Ellipsoid.WGS84)));
            /*以起点为中心建立直角坐标系，计算行进方向与x轴夹角，开闭区间表示是否包含横纵坐标轴*/
            double angle = 0;//与x轴夹角
            double sin = 0;//与x轴夹角的正弦值
            int quadrant = 1;//象限
            BigDecimal startLat = new BigDecimal(Double.toString(start[0]));
            BigDecimal endLat = new BigDecimal(Double.toString(end[0]));
            BigDecimal startLng = new BigDecimal(Double.toString(start[1]));
            BigDecimal endLng = new BigDecimal(Double.toString(end[1]));
            double sin1 = (endLat.subtract(startLat).doubleValue()) * unitLat / distance;
            double sin2 = (startLat.subtract(endLat).doubleValue()) * unitLat / distance;
            if (endLat.compareTo(startLat) > -1 && endLng.compareTo(startLng) > -1) {//第一象限，[x,y]
                sin = sin1;
                quadrant = 1;
            } else if (endLat.compareTo(startLat) > -1 && endLng.compareTo(startLng) < 0) {//第二象限，[x,y)
                sin = sin1;
                quadrant = 2;
            } else if (endLat.compareTo(startLat) < 0 && endLng.compareTo(startLng) < 1) {//第三象限，（x,y]
                sin = sin2;
                quadrant = 3;
            } else if (endLat.compareTo(startLat) < 0 && endLng.compareTo(startLng) > 0) {//第四象限，(x,y)
                sin = sin2;
                quadrant = 4;
            }
            if (Double.isNaN(sin)){
                sin = 0.0;
            }
            BigDecimal sinBd = new BigDecimal(String.valueOf(sin));
            sin = Double.parseDouble(sinBd.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            DecimalFormat sixDf = new DecimalFormat("0.000000");
//            sin = Double.parseDouble(sixDf.format(sin));
            angle = Math.asin(sin);//正弦对应的弧度值
            if (Double.isNaN(angle)){
                angle = 0.0;
            }
            BigDecimal angleBd = new BigDecimal(String.valueOf(angle));
            angle = Double.parseDouble(angleBd.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            angle = Math.toRadians(angle);//角度转为弧度
            //需要补的点数，舍弃小数位取整
            int supplyNum = (int) Math.floor(distance / eachDistance);
            Log.d(TAG, "补点的个数：" + supplyNum);
            /*根据夹角，计算补出的每个点的坐标*/
            List<double[]> eachPoints = new ArrayList<>();//这一段距离补充的坐标点
            supplyPoints.add(start);//先把这一段的起点添加进去
            for (int j = 1; j <= supplyNum; j++) {
                double[] eachCoord = new double[2];//补充的点
                double radians = Math.toRadians(start[0]);//弧度值，需要把维度值转化为弧度值
                double unitLng = unitLat * Math.cos(radians);//这个点的维度对应的1经度的距离 todo 可以直接按start[0] 的值算
                double y = eachDistance * j * sin / unitLat;//补点的纵坐标的绝对值
//                double x = eachDistance * j * Math.cos(angle) / unitLng;//补点的横坐标的绝对值
                switch (quadrant) {//根据不同象限计算坐标
                    case 1:
                        eachCoord[0] = y + start[0];//每个点的维度值
//                        radians = Math.toRadians(eachCoord[0]);//
//                        unitLng = unitLat * Math.cos(radians);//
                        eachCoord[1] = eachDistance * j * Math.cos(angle) / unitLng + start[1];//每个点计算经度值
                        break;
                    case 2:
                        eachCoord[0] = y + start[0];
//                        radians = Math.toRadians(eachCoord[0]);//转为弧度值
//                        unitLng = unitLat * Math.cos(radians);
                        eachCoord[1] = start[1] - eachDistance * j * Math.cos(angle) / unitLng;
                        break;
                    case 3:
                        eachCoord[0] = start[0] - y;
//                        radians = Math.toRadians(eachCoord[0]);//转为弧度值
//                        unitLng = unitLat * Math.cos(radians);
                        eachCoord[1] = start[1] - eachDistance * j * Math.cos(angle) / unitLng;
                        break;
                    case 4:
                        eachCoord[0] = start[0] - y;
//                        radians = Math.toRadians(eachCoord[0]);//转为弧度值
//                        unitLng = unitLat * Math.cos(radians);
                        eachCoord[1] = eachDistance * j * Math.cos(angle) / unitLng + start[1];
                        break;
                    default:
                        break;
                }
                eachCoord[0] = Double.parseDouble(sixDf.format(eachCoord[0]));
                eachCoord[1] = Double.parseDouble(sixDf.format(eachCoord[1]));
                eachPoints.add(eachCoord);//把补出的点添加进去
            }
            //把计算出来的点添加到起点之后
            if (eachPoints.size() > 0) {
                supplyPoints.addAll(eachPoints);
            }
        }
        Log.e(TAG, "补点用时："+(System.currentTimeMillis() - startTime)+ "ms");
        calDis(supplyPoints);
        Log.d(TAG, "补点的个数" + supplyPoints.size());
        return supplyPoints;
    }
    /**
     * 精确的补点计算
     * 第一种方案：根据两个坐标点距离，在单位时间内设置坐标点，距离不同则速度不同（TODO:测试一下百度给的坐标点间距大小）
     * 两点距离太长则需要补点
     *
     * @param eachDistance 根据不同场景，确定补点后每段的距离
     * @return 补点后的坐标点集合
     */
    public List<double[]> supplyPointsAccurate(RoutePlan routePlan, float eachDistance) {
        BigDecimal eachDistanceBD = new BigDecimal(Float.toString(eachDistance));
        long startTime = System.currentTimeMillis();
//        float unitLat = ConstantsUtils.BaiDuMap.UNIT_LAT * 3600;//一维度对应的距离
        float unitLat = ConstantsUtils.BaiDuMap.ONE_DEGREE_LAT;
        List<double[]> coords = getCoords(routePlan);//百度给的点集合，已转为GPS坐标
        Log.d(TAG, "百度点的个数：" + coords.size());
        List<double[]> supplyPoints = new ArrayList<>();//需要返回的点集合
        /*判断每两个坐标点是否需要补点，需要则计算出补出的坐标点，添加到坐标集合中*/
        for (int i = 0; i < coords.size(); i++) {//遍历每一段路程
            if (i == coords.size() - 1) {
                supplyPoints.add(coords.get(i));//把最后一个点加进去
                break;
            }
            double[] start = coords.get(i);//起点
            double[] end = coords.get(i + 1);//终点
            float distance;//百度给的每两点间的距离
            GlobalCoordinates from = new GlobalCoordinates(start[0], start[1]);
            GlobalCoordinates to = new GlobalCoordinates(end[0], end[1]);
            DecimalFormat df = new DecimalFormat("0.0");
            distance = Float.parseFloat(df.format(getDistanceMeter(from, to, Ellipsoid.WGS84)));
            /*以起点为中心建立直角坐标系，计算行进方向与x轴夹角，开闭区间表示是否包含横纵坐标轴*/
//            double angle = 0;//与x轴夹角
            BigDecimal radianBD;//与x轴夹角的弧度值 0 ~ π/2
//            double sin = 0;//与x轴夹角的正弦值
            BigDecimal sinBD = null;//与x轴夹角的正弦值
            int quadrant = 1;//象限
            BigDecimal startLat = new BigDecimal(Double.toString(start[0]));
            BigDecimal endLat = new BigDecimal(Double.toString(end[0]));
            BigDecimal startLng = new BigDecimal(Double.toString(start[1]));
            BigDecimal endLng = new BigDecimal(Double.toString(end[1]));
            BigDecimal unitLatBD = new BigDecimal(Float.toString(unitLat));
            BigDecimal distanceBD = new BigDecimal(Float.toString(unitLat));
            BigDecimal sin1BD;//一二象限与x轴夹角
            BigDecimal sin2BD;//三四象限与x轴夹角
            sin1BD = (endLat.subtract(startLat)).multiply(unitLatBD).divide(distanceBD,6,BigDecimal.ROUND_HALF_UP);
            sin2BD = (startLat.subtract(endLat)).multiply(unitLatBD).divide(distanceBD, 6, BigDecimal.ROUND_HALF_UP);
            if (endLat.compareTo(startLat) > -1 && endLng.compareTo(startLng) > -1) {//第一象限，[x,y]
                sinBD = sin1BD;
                quadrant = 1;
            } else if (endLat.compareTo(startLat) > -1 && endLng.compareTo(startLng) < 0) {//第二象限，[x,y)
                sinBD = sin1BD;
                quadrant = 2;
            } else if (endLat.compareTo(startLat) < 0 && endLng.compareTo(startLng) < 1) {//第三象限，（x,y]
                sinBD = sin2BD;
                quadrant = 3;
            } else if (endLat.compareTo(startLat) < 0 && endLng.compareTo(startLng) > 0) {//第四象限，(x,y)
                sinBD = sin2BD;
                quadrant = 4;
            }
            DecimalFormat sixDf = new DecimalFormat("0.000000");
//            angle = Math.asin(sin);//正弦对应的弧度值
            assert sinBD != null;
            radianBD = BigDecimalMath.asin(sinBD,mathContext);//正弦对应的弧度值
            //角度转为弧度
            //需要补的点数，舍弃小数位取整
            int supplyNum = (int) Math.floor(distance / eachDistance);
            Log.d(TAG, "补点的个数：" + supplyNum);
            /*根据夹角，计算补出的每个点的坐标*/
            List<double[]> eachPoints = new ArrayList<>();//这一段距离补充的坐标点
            supplyPoints.add(start);//先把这一段的起点添加进去
            for (int j = 1; j <= supplyNum; j++) {
                double[] eachCoord = new double[2];//补充的点
                double radians = Math.toRadians(start[0]);//弧度值，需要把维度值转化为弧度值
//                double unitLng = unitLat * Math.cos(radians);//这个点的维度对应的1经度的距离
                BigDecimal unitLngBD = unitLatBD.multiply(BigDecimalMath.cos(new BigDecimal(Double.toString(radians)),mathContext));//这个点的维度对应的1经度的距离
//                double y = eachDistance * j * sin / unitLat;//补点的纵坐标的绝对值
                BigDecimal yBD = eachDistanceBD.multiply(new BigDecimal(j)).multiply(sinBD).divide(unitLatBD,6,BigDecimal.ROUND_HALF_UP);//补点的纵坐标的绝对值
//                double x = eachDistance * j * Math.cos(angle) / unitLng;//补点的横坐标的绝对值
                switch (quadrant) {//根据不同象限计算坐标
                    case 1:
//                        eachCoord[0] = y + start[0];//每个点的维度值
                        eachCoord[0] = yBD.add(new BigDecimal(Double.toString(start[0]))).doubleValue();
//                        eachCoord[1] = eachDistance * j * Math.cos(angle) / unitLng + start[1];//每个点计算经度值
                        eachCoord[1] = eachDistanceBD.multiply(new BigDecimal(j)).multiply(BigDecimalMath.cos(radianBD,mathContext))
                                .divide(unitLngBD,6,BigDecimal.ROUND_HALF_UP)
                                .add(new BigDecimal(Double.toString(start[1]))).doubleValue();//每个点计算经度值
                        break;
                    case 2:
                        eachCoord[0] = yBD.add(new BigDecimal(Double.toString(start[0]))).doubleValue();
//                        eachCoord[1] = start[1] - eachDistance * j * Math.cos(angle) / unitLng;
                        eachCoord[1] = new BigDecimal(Double.toString(start[1])).subtract(eachDistanceBD.multiply(new BigDecimal(j))
                                .multiply(BigDecimalMath.cos(radianBD,mathContext)).divide(unitLngBD,6,BigDecimal.ROUND_HALF_UP)).doubleValue();
                        break;
                    case 3:
//                        eachCoord[0] = start[0] - y;
                        eachCoord[0] = new BigDecimal(Double.toString(start[0])).subtract(yBD).doubleValue();
//                        eachCoord[1] = start[1] - eachDistance * j * Math.cos(angle) / unitLng;
                        eachCoord[1] = new BigDecimal(Double.toString(start[1])).subtract(eachDistanceBD.multiply(new BigDecimal(j))
                                .multiply(BigDecimalMath.cos(radianBD,mathContext)).divide(unitLngBD,6,BigDecimal.ROUND_HALF_UP)).doubleValue();
                        break;
                    case 4:
//                        eachCoord[0] = start[0] - y;
                        eachCoord[0] = new BigDecimal(Double.toString(start[0])).subtract(yBD).doubleValue();
//                        radians = Math.toRadians(eachCoord[0]);//转为弧度值
//                        unitLng = unitLat * Math.cos(radians);
//                        eachCoord[1] = eachDistance * j * Math.cos(angle) / unitLng + start[1];
                        eachCoord[1] = eachDistanceBD.multiply(new BigDecimal(j)).multiply(BigDecimalMath.cos(radianBD,mathContext))
                                .divide(unitLngBD, 6, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(Double.toString(start[1]))).doubleValue();
                        break;
                    default:
                        break;
                }
//                Log.d(TAG, "计算出的补点："+Arrays.toString(eachCoord));
                eachCoord[0] = Double.parseDouble(sixDf.format(eachCoord[0]));
                eachCoord[1] = Double.parseDouble(sixDf.format(eachCoord[1]));
                eachPoints.add(eachCoord);//把补出的点添加进去
            }
            //把计算出来的点添加到起点之后
            if (eachPoints.size() > 0) {
                supplyPoints.addAll(eachPoints);
            }
        }
        Log.e(TAG, "补点用时："+(System.currentTimeMillis() - startTime)+ "ms");
//        calDis(supplyPoints);
        Log.d(TAG, "补点的个数" + supplyPoints.size());
        return supplyPoints;
    }

    /**
     * 设置经纬度坐标 子线程内调用
     *
     * @param points GPS坐标点集合
     */
    public void setGpsLocation(List<double[]> points, SceneType sceneType) {
        if (points != null && points.size() > 0) {
            if (callBack != null) {
                callBack.onStart();
            }
            int rate = ConstantsUtils.BaiDuMap.RATE;
            for (int i = 0; i < points.size(); i++) {
                if (!isStart.get()){
                    Log.d(TAG, "停止更新定位");
//                    sendServiceStatus(false, isRestart.get());
                    return;
                }
                HardwareUtil.getInstance(ExpandServiceApplication.getInstance())
                        .setGpsLocation(points.get(i)[0] + ";" + points.get(i)[1]);
                Log.d(TAG, "已设置坐标："+HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation());
                //3s后再设置下一个坐标
                SystemClock.sleep(rate * 1000);
            }
            //继续获取下一个路线
            reGetRoutePlan(sceneType);
        }else {
            throw new RuntimeException("坐标集合为空！");
        }
    }

    /**
     * 重新确定终点，规划路线(三种情况：1.到达终点 2. 路线规划异常 3.网络异常)
     * @param sceneType
     */
    private void reGetRoutePlan(SceneType sceneType){
        /**
         * 网络不可用时，重试超过指定次数自动停止服务
         */
        if (!NetUtil.isNetworkAvailable(ExpandServiceApplication.getInstance().getApplicationContext())) {
            reTryCount++;
            if (reTryCount > ConstantsUtils.BaiDuMap.RE_TRY_MAX){
                Log.e(TAG, "网络无法访问，虚拟场景服务停止");
                //路线获取失败
                if (callBack != null) {
                    callBack.onError(ExpandServiceApplication.getInstance().getString(R.string.vs_start_failed));
                }
                sendServiceStatus(false);
                stopSelf();
                return;
            }
        }else {
            reTryCount = 0;//重置重试次数
        }
        /*把当前坐标作为下一次的起点*/
        String currLoc = HardwareUtil.getInstance(ExpandServiceApplication.getInstance()).getGpsLocation();
        double currLat = Double.parseDouble(currLoc.split(";")[0]);
        double currLng = Double.parseDouble(currLoc.split(";")[1]);
        DecimalFormat df = new DecimalFormat("0.000000");
        currLat = Double.parseDouble(df.format(currLat));
        currLng = Double.parseDouble(df.format(currLng));
        startLoc = new double[]{currLat, currLng};
        terminalLoc = BdMapUtils.getTerminalPoint(centerCoord, radius);
        String origin = startLoc[0] + "," + startLoc[1];
        String destination = terminalLoc[0] + "," + terminalLoc[1];
        Log.e(TAG, "开始重新规划路线："+"起点："+origin + "终点："+ destination);
        getRoutePlan(sceneType, origin, destination);
    }


    /**
     * 计算补点后每两个点间的距离
     *
     * @param points
     */
    public void calDis(List<double[]> points) {
        for (int i = 0; i < points.size(); i++) {
//            Log.d(TAG, "补点后的坐标：" + Arrays.toString(points.get(i)));
            if (i == points.size() - 1) {
                return;
            }
            GlobalCoordinates from = new GlobalCoordinates(points.get(i)[0], points.get(i)[1]);
            GlobalCoordinates to = new GlobalCoordinates(points.get(i + 1)[0], points.get(i + 1)[1]);
            DecimalFormat df = new DecimalFormat("0.0");
            double distance = Double.parseDouble(df.format(getDistanceMeter(from, to, Ellipsoid.WGS84)));
//            Log.d(TAG, "补点后：" + distance + " m");
        }
    }

    /**
     * 计算设置GPS的频率
     * 第二种方案：获取两个相邻点的坐标，计算两个点间的距离，然后除以速度（根据不同的场景确定速度大小），
     * 得到设置GPS坐标的频率，根据频率设置坐标
     *
     * @deprecated 暂时不用这个方案，如果两点距离太长，会出现坐标更新太慢的情况
     */
    private void calSetGpsRate(RoutePlan routePlan) {
        //1.使用固定的运动速度  2.使用百度给出的路线长度及需要的时长算出速度(驾车模式可能因为堵车导致速度过慢,但更真实)
        List<Double> distances = calDistance(routePlan);
        List<Double> walkRates = new ArrayList<>();//步行坐标设置频率
        for (Double distance : distances) {
            walkRates.add(distance / walkSpeed);
        }
    }

    /**
     * 把路径中所有坐标点抽离出来
     *
     * @param routePlan
     * @return GPS坐标点集合
     */
    public List<double[]> getCoords(RoutePlan routePlan) {
        List<double[]> coords = new ArrayList<>();
        DecimalFormat sixDf = new DecimalFormat("0.000000");
        //默认使用第一种路线方案
        if (routePlan.getResult().getRoutes().size() > 0) {
            ArrayList<Steps> paths = routePlan.getResult().getRoutes().get(0).getSteps();
            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i).getPath();
                String[] points = path.split(";");
                for (int j = 0; j < points.length; j++) {
                    double pointLng = Double.parseDouble(points[j].split(",")[0]);
                    double pointLat = Double.parseDouble(points[j].split(",")[1]);
                    //保留6位小数
                    pointLat = Double.parseDouble(sixDf.format(pointLat));
                    pointLng = Double.parseDouble(sixDf.format(pointLng));
                    double[] pointGps = GPSUtil.bd09_To_gps84(pointLat, pointLng);
                    if (j != points.length - 1) {//上个step结束的点和下个step开始点是重的
                        coords.add(pointGps);
                    }
                    if (i == points.length - 1) {//最后一个step的最后一个点要加上
                        coords.add(pointGps);
                    }
                }
            }
        }
        return coords;
    }

    /**
     * 计算获取到的坐标点的间距
     *
     * @param routePlan
     */
    public List<Double> calDistance(RoutePlan routePlan) {
        List<Double> distances = new ArrayList<>();
        //默认使用第一种路线方案
        if (routePlan.getResult().getRoutes().size() > 0) {
            ArrayList<Steps> paths = routePlan.getResult().getRoutes().get(0).getSteps();
            for (Steps steps : paths) {
                String path = steps.getPath();
                String[] points = path.split(";");
                for (int i = 0; i < points.length; i++) {
                    if (i == points.length - 1) {
                        continue;
                    }
                    double lng = Double.parseDouble(points[i].split(",")[0]);
                    double lat = Double.parseDouble(points[i].split(",")[1]);
                    double toLng = Double.parseDouble(points[i + 1].split(",")[0]);
                    double toLat = Double.parseDouble(points[i + 1].split(",")[1]);
                    double[] fromGps = GPSUtil.bd09_To_gps84(lat, lng);
                    double[] toGps = GPSUtil.bd09_To_gps84(toLat, toLng);
                    GlobalCoordinates from = new GlobalCoordinates(fromGps[0], fromGps[1]);
                    GlobalCoordinates to = new GlobalCoordinates(toGps[0], toGps[1]);
                    DecimalFormat df = new DecimalFormat("0.0");
                    double distance = Double.parseDouble(df.format(getDistanceMeter(from, to, Ellipsoid.WGS84)));
//                    Log.d(TAG, "补点前：" + distance + " m");
                    distances.add(distance);
                }
            }
        }
        return distances;
    }

    /**
     * 获取两个经纬度坐标间的距离
     *
     * @param gpsFrom
     * @param gpsTo
     * @param ellipsoid
     * @return
     */
    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid) {

        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }

    /**
     * 停止路线规划
     */
    public void stopRoute(){
        isStart.set(false);
        //更新下虚拟场景服务的状态
        SharePreferenceHelper spHelper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
        VsConfig vsConfig = spHelper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG,VsConfig.class);
        if (vsConfig != null) {
            vsConfig.setStart(false);
            //根据是否要重启来判断准备恢复的坐标
            if (isRestart.get()){
                //先把坐标设置到新的城市,防止虚拟场景重启失败影响虚拟定位
                Log.d(TAG, "坐标预先设置到新城市："+Arrays.toString(newStartCoord));
                HardwareUtil.getInstance(ExpandServiceApplication.getInstance())
                        .setGpsLocation(newStartCoord[0] + ";" + newStartCoord[1]);
            }else {
                //恢复到启动时的位置
                Log.d(TAG, "准备恢复的坐标：" + Arrays.toString(vsConfig.getCenterCoords()));
                HardwareUtil.getInstance(ExpandServiceApplication.getInstance())
                        .setGpsLocation(vsConfig.getCenterCoords()[0] + ";" + vsConfig.getCenterCoords()[1]);
            }
            spHelper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
        }
        if (callBack != null) {
            callBack.onStop();
        }
        //通知后台服务停止

    }

    /**
     * 重启路线规划
     * @param newStartCoord 新的起点坐标
     */
    public void restartRoute(double[] newStartCoord){
        isRestart.set(true);
        this.newStartCoord = newStartCoord;
        stopRoute();
    }

    /**
     * 设置虚拟场景的状态
     * @param typeId
     * @param sn
     * @param isOpen
     */
    public void setVsStatus(int typeId, String sn, int isOpen){
        RetrofitServiceManager.setVSStatus(typeId, sn, isOpen)
                .subscribe(new Subscriber<BaseResponse<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "setStatus:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<Object> base) {
                        if (base != null){
                            if (base.getStatus() == 0) {
                            }else if (base.getRetCode() == ServerErrorCode.E_30011){
                            }

                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "虚拟场景服务已停止！");
        stopRoute();
        super.onDestroy();
    }

    /**
     * 发送虚拟场景运行状态广播
     * @param status true：启动状态 false:停止状态
     */
    private void sendServiceStatus(boolean status){
        if (!isRestart.get()) return;//重启虚拟场景时才发送广播
        isRestart.set(false);
        broadIntent.setAction(ConstantsUtils.BroadCast.SERVICE_CONNECTION_ACTION);
        broadIntent.putExtra(ConstantsUtils.BroadCast.KEY_SERVICE_STATUS, status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadIntent);
        Log.d(TAG, "发送服务状态广播");
    }
}
