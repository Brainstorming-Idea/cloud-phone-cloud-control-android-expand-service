package com.cloud.control.expand.service.utils.bdmap;

import android.util.Log;

import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.MathUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/15
 */
public class BdMapUtils {
private static final String TAG = "BdMapUtils";

    public static double[] getTerminalPoint(double[] centerCoord, int radius){
        if (centerCoord[0] == 0 || centerCoord[1] == 0){
            throw new RuntimeException("尚未获取到中心点坐标！");
        }
        Log.d(TAG, "中心点:"+Arrays.toString(centerCoord));
        DecimalFormat df = new DecimalFormat("0.000000");
//        float unitLat = ConstantsUtils.BaiDuMap.UNIT_LAT;//一秒纬度对应的距离，米
        int oneLat = ConstantsUtils.BaiDuMap.ONE_DEGREE_LAT;//一维度的距离，米
        double radians = Math.toRadians(centerCoord[0]);//转为弧度值
        final double unitLng = oneLat * Math.cos(radians);//一经度对应的距离，米
        double[] lTopCoord ;
        double[] rTopCoord ;
        double[] lBottomCoord;
        double[] rBottomCoord;
        //勾股定理求边长的一半
        double halfSize = MathUtils.sqrt(Math.pow(radius,2) / 2);
        double lTopLat = centerCoord[0] + (halfSize / oneLat);//左上角纬度值
        double lTopLng = centerCoord[1] - (halfSize / unitLng);//左上角经度值
        lTopLat = Double.parseDouble(df.format(lTopLat));
        lTopLng = Double.parseDouble(df.format(lTopLng));
        lTopCoord = new double[]{lTopLat,lTopLng};
        double rTopLat = lTopLat;//右上角纬度值
        double rTopLng = centerCoord[1] + (halfSize / unitLng);//右上角经度值
        rTopLng = Double.parseDouble(df.format(rTopLng));
        rTopCoord = new double[]{rTopLat, rTopLng};
        double lBotLat = centerCoord[0] - (halfSize / oneLat);//左下角纬度值
        double lBotLng = lTopLng;//左下角经度值
        lBotLat = Double.parseDouble(df.format(lBotLat));
        lBottomCoord = new double[]{lBotLat,lBotLng};
        double rBotLat = lBotLat;//右下角纬度值
        double rBotLng = rTopLng;//右下角经度值
        rBottomCoord = new double[]{rBotLat,rBotLng};
        Log.d(TAG, "左上角："+ Arrays.toString(lTopCoord) +"右上角："+Arrays.toString(rTopCoord)+
                "左下角："+Arrays.toString(lBottomCoord) + "右下角"+Arrays.toString(rBottomCoord));
        double[] terminalPoint = calEndPoint(lTopCoord,rBottomCoord);
        Log.d(TAG, "随机终点坐标："+ Arrays.toString(terminalPoint));
        return terminalPoint;
    }

    /**
     * 计算终点坐标
     * @param ltPoint
     * @param rbPoint
     * @return
     */
    private static double[] calEndPoint(double[] ltPoint, double[] rbPoint){
        double endPointLat;
        double endPointLng;
        DecimalFormat df = new DecimalFormat("0.000000");
        double latMin = rbPoint[0];
        double latMax = ltPoint[0];
        double lngMin = ltPoint[1];
        double lngMax = rbPoint[1];
        /*在运动区域内算出随机的经纬度坐标*/
        endPointLat = latMin + Math.random() * (latMax - latMin);
        endPointLat = Double.parseDouble(df.format(endPointLat));
        endPointLng = lngMin + Math.random() * (lngMax - lngMin);
        endPointLng = Double.parseDouble(df.format(endPointLng));
        return new double[]{endPointLat,endPointLng};
    }
} 