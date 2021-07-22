package com.cloud.control.expand.service.module.virtualscene;

import android.content.Context;
import android.util.Log;
import android.util.VclustersCHardwareUtil;
import android.util.vclusters.hardware.VclustersPHardware;

import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.SysProp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

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

    private HardwareUtil(Context context) {
        mVclustersCHardwareUtil = new VclustersCHardwareUtil(context);
        vclustersPHardware = new VclustersPHardware();
    }

    /**
     * @param context must be application level context
     * @return
     */
    public static HardwareUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (HardwareUtil.class) {
                if (instance == null) {
                    instance = new HardwareUtil(context);
                }
            }
        }
        return instance;
    }

//    /**
//     * 修改GPS坐标
//     *
//     * @param gpsLocation
//     * @return true 成功  false 失败
//     */
//    public boolean setGpsLocation(String gpsLocation) {
//        try {
//            mVclustersCHardwareUtil.setGpsLocation(gpsLocation);
//            Log.d(TAG, "修改GPS坐标成功");
//            return true;
//        } catch (Exception e) {
//            Log.d(TAG, "修改GPS坐标失败");
//            e.printStackTrace();
//            return false;
//        }
//    }
//    /**
//     * 修改GPS坐标
//     *
//     * @param gpsLocation 系统存储GPS的格式： 维度;经度
//     * @return true 成功  false 失败
//     */
//    public boolean setGpsLocation(String gpsLocation) {
//        Process process = null;
//        DataOutputStream dos = null;
//        try {
//            process = Runtime.getRuntime().exec("vc");
//            dos = new DataOutputStream(process.getOutputStream());
//            dos.writeBytes("echo \""+ gpsLocation + "\" > /data/system/gps/location" + "\n");
//            dos.writeBytes("exit\n");
//            dos.flush();
//            int i = process.waitFor();
//            if (i == 0){
//                Log.d(TAG, "修改GPS坐标成功");
//                return true;
//            }else {
//                Log.e(TAG, "修改GPS坐标失败");
//                return false;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "修改GPS坐标失败");
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (dos != null) {
//                    dos.close();
//                }
//                process.destroy();
//            } catch (Exception e) {
//            }
//        }
//    }

    /**
     * 修改GPS坐标
     *
     * @param gpsLocation 系统存储GPS的格式： 维度;经度
     * @return true 成功  false 失败
     */
    public boolean setGpsLocation(String gpsLocation) {
        /*保留6位小数*/
        double lat = Double.parseDouble(gpsLocation.split(";")[0]);
        double lng = Double.parseDouble(gpsLocation.split(";")[1]);
        DecimalFormat sixDf = new DecimalFormat("0.000000");
        String latStr = sixDf.format(lat);
        String lngStr = sixDf.format(lng);
        if (ExpandServiceApplication.isVirtual){
            try {
                SysProp.set("persist.vclusters.Latitude", latStr);
                SysProp.set("persist.vclusters.Longitude", lngStr);
                return true;
            }catch (Exception e){
                Log.e(TAG, "virtual set error:" + e.getMessage());
                return false;
            }
        }else {
            gpsLocation = latStr + ";" + lngStr;
            return writeFileContent(ConstantsUtils.GPS_FILE_PATH, gpsLocation, true);
        }
    }

//    /**
//     * 获取GPS坐标
//     *
//     * @return
//     */
//    public String getGpsLocation() {
//        try {
//            String strGps = mVclustersCHardwareUtil.getGpsLocation();
//            Log.d(TAG,"strGps : " + strGps);
//            return strGps;
//        } catch (Exception e) {
//            Log.d(TAG, "strGps : " + "获取GPS坐标失败");
//            e.printStackTrace();
//            return "";
//        }
//    }
//    /**
//     * 获取GPS坐标
//     *
//     * @return
//     */
//    public String getGpsLocation() {
//        Process process = null;
//        DataOutputStream dos = null;
//        InputStream ins = null;
//        InputStream errorStream = null;
//        String gpsStr = "";
//        try {
//            process = Runtime.getRuntime().exec("vc");
//            dos = new DataOutputStream(process.getOutputStream());
//            dos.writeBytes("cat /data/system/gps/location\n");
//            dos.writeBytes("exit\n");
//            dos.flush();
//            /*获取结果输入流*/
//            ins = process.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
//            StringBuilder resultSb = new StringBuilder();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null){
//                resultSb.append(line);
//            }
//            /*获取错误流*/
//            errorStream = process.getErrorStream();
//            BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(errorStream));
//            StringBuilder errorSb = new StringBuilder();
//            String errorLine = "";
//            while ((errorLine = errorBuffer.readLine()) != null){
//                errorSb.append(errorLine);
//            }
//            Log.e(TAG, "cat gps file:" + ":"+ errorSb.toString());
//            gpsStr = resultSb.toString();
//            if (!gpsStr.isEmpty()){
//                Log.d(TAG,"strGps : " + gpsStr);
//            }else {
//                gpsStr = "";
//                Log.d(TAG, "strGps : " + "获取GPS坐标失败");
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage()+"");
//        } finally {
//            try {
//                if (dos != null) {
//                    dos.close();
//                }
//                if (ins != null){
//                    ins.close();
//                }
//                if (errorStream != null){
//                    errorStream.close();
//                }
//                if (process != null) {
//                    process.destroy();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return gpsStr;
//    }

    /**
     * 创建文件并写入内容
     *
     * @param filePath
     * @param content
     * @param modify   是否可修改文件内容
     */
    public static boolean writeFileContent(String filePath, String content, boolean modify) {
        Log.d(TAG, "java 写入GPS:"+content);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            //文件是否有内容
            if (file.length() > 0) {
                //是否可修改
                if (modify) {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    Log.d(TAG, "writeFileContent modify success");
                } else {
                    Log.d(TAG, "writeFileContent exits content");
                }
            } else {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                Log.d(TAG, "writeFileContent success");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取GPS坐标
     *
     * @return
     */
    public String getGpsLocation() {
        if (ExpandServiceApplication.isVirtual){
            try {
//            String latitude = SysProp.get("persist.vclusters.Latitude","22.549054");//默认深圳市政府
//            String longitude = SysProp.get("persist.vclusters.Longitude","114.064524");
                String latitude = SysProp.get("persist.vclusters.Latitude","");
                String longitude = SysProp.get("persist.vclusters.Longitude","");
                return latitude + ";" + longitude;
            }catch (Exception e){
                Log.e(TAG, "virtual get gps error:" + e.getMessage());
                return "";
            }
        }else {
            StringBuilder gpsStr = new StringBuilder();
            File file = new File(ConstantsUtils.GPS_FILE_PATH);
            if (!file.exists()) {
                return gpsStr.toString();
            }
            //文件是否有内容
            if (file.length() > 0) {
                try {
                    InputStream inStream = new FileInputStream(file);
                    InputStreamReader inputReader = new InputStreamReader(inStream);
                    BufferedReader buffReader = new BufferedReader(inputReader);
                    String line;
                    //分行读取
                    while ((line = buffReader.readLine()) != null) {
                        gpsStr.append(line);
                    }
                    inStream.close();
                } catch (java.io.FileNotFoundException e) {
                    Log.e(TAG, "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.e(TAG, "" + e.getMessage());
                }
            } else {
                Log.e(TAG, "gps file is empty");
            }
            Log.d(TAG, "java 获取GPS:" + gpsStr);
            return gpsStr.toString();
        }
    }
}