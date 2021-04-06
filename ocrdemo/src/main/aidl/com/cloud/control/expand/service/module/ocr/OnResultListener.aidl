// OnResultListener.aidl
package com.cloud.control.expand.service.module.ocr;

// Declare any non-default types here with import statements

interface OnResultListener {
    //识别结果；识别时长
    void onSuccess(String recognitionRes, float inferenceTime);

    void onFailed(String msg);
}
