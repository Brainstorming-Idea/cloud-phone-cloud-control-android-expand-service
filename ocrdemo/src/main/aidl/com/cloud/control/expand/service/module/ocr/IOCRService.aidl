// IOCRService.aidl
package com.cloud.control.expand.service.module.ocr;

// Declare any non-default types here with import statements
import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.module.ocr.OnResultListener;
import com.cloud.control.expand.service.module.ocr.InitModelListener;

interface IOCRService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    //初始化模型
    void initModel(InitModelListener initModelListener);
    //识别的目标图片
    void inputImg(in TargetImg targetImg, OnResultListener onResultListener);
    //高级设置 置信度（0.00 ~ 1.00）
    void advancedSetup(float confidence, int cpuThreadNum, String cpuPowerMode);
}
