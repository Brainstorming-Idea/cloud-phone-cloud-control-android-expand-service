// OnResultListener.aidl
package com.cloud.control.expand.service.module.ocr;

// Declare any non-default types here with import statements

interface OnResultListener {
    void onSuccess(String recognitionRes);

    void onFailed(String msg);
}
