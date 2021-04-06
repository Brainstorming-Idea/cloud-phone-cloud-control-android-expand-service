package com.cloud.control.expand.service.module.virtualscene;

import android.content.Context;
import android.util.Log;
import android.util.VclustersCHardwareUtil;
import android.util.vclusters.hardware.VclustersPHardware;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/8
 */
public class HardwareUtil {
    private static final String TAG = "HardwareUtil";

    private static volatile HardwareUtil instance;
    private VclustersCHardwareUtil mVclustersCHardwareUtil;
    private VclustersPHardware vclustersPHardware;

    private HardwareUtil (Context context){
        mVclustersCHardwareUtil = new VclustersCHardwareUtil(context);
        vclustersPHardware = new VclustersPHardware();
    }

    /**
     *
     * @param context must be application level context
     * @return
     */
    public static HardwareUtil getInstance(Context context){
        if (instance == null){
            synchronized (HardwareUtil.class){
                if (instance == null) {
                    instance = new HardwareUtil(context);
                }
            }
        }
        return instance;
    }

    /**
     * 修改GPS坐标
     *
     * @param gpsLocation
     * @return true 成功  false 失败
     */
    public boolean setGpsLocation(String gpsLocation) {
        try {
            mVclustersCHardwareUtil.setGpsLocation(gpsLocation);
            Log.d(TAG, "修改GPS坐标成功");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "修改GPS坐标失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取GPS坐标
     *
     * @return
     */
    public String getGpsLocation() {
        try {
            String strGps = mVclustersCHardwareUtil.getGpsLocation();
            Log.d(TAG,"strGps : " + strGps);
            return strGps;
        } catch (Exception e) {
            Log.d(TAG, "strGps : " + "获取GPS坐标失败");
            e.printStackTrace();
            return "";
        }
    }
} 