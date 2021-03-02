// InitModelListener.aidl
package com.cloud.control.expand.service.module.ocr;

// Declare any non-default types here with import statements

interface InitModelListener {
   void onLoadSuccess();

   void onLoadFailed(String msg);
}
