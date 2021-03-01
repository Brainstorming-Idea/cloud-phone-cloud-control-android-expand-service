// IOCRService.aidl
package com.cloud.control.expand.service.module.ocr;

// Declare any non-default types here with import statements
import com.cloud.control.expand.service.aidl.TargetImg;
import com.cloud.control.expand.service.module.ocr.OnResultListener;

interface IOCRService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    //识别的目标图片，置信度（0.0 ~ 1.0） 模型ID（）
    void inputImg(in TargetImg targetImg, String imgFlag, float confidence, int moduleId, OnResultListener onResultListener);
}
