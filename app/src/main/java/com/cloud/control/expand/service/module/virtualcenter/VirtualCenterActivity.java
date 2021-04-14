package com.cloud.control.expand.service.module.virtualcenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.control.expand.service.BuildConfig;
import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.LocationInfoEntity;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;
import com.cloud.control.expand.service.injector.components.DaggerChooseVirtualCenterComponent;
import com.cloud.control.expand.service.injector.modules.VirtualCenterModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationActivity;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneActivity;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wangyou
 * @desc: 选择虚拟中心界面
 * @date :2021/3/8
 */
public class VirtualCenterActivity extends BaseActivity<VirtualCenterPresenter> implements VirtualCenterView {
    private static final String TAG = "VirtualCenterActivity";

    @BindView(R.id.cc_toolbar)
    Toolbar toolbar;
    @BindView(R.id.wb_choose_center)
    WebView wbVirtualCenter;

    private boolean isOnReceivedError = false;
    //当前的经纬度、城市、IP信息
    private LocationInfoEntity mInfoEntity = new LocationInfoEntity();

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_virtual_center;
    }

    @Override
    protected void initInjector() {
        DaggerChooseVirtualCenterComponent.builder()
                .virtualCenterModule(new VirtualCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(toolbar,false,"");
        initWebViewSetting();
        wbVirtualCenter.loadUrl("file:android_asset/map/map.html");
    }

    /**
     * 初始化webView
     */
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    protected void initWebViewSetting() {
        wbVirtualCenter.getSettings().setJavaScriptEnabled(true);
        wbVirtualCenter.getSettings().setDomStorageEnabled(true);
        wbVirtualCenter.getSettings().setAllowFileAccess(false);
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wbVirtualCenter.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wbVirtualCenter.setWebViewClient(new BaseWebViewClient());
        wbVirtualCenter.addJavascriptInterface(new BaseJsInterface(), "android");
        // 允许webView debug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        initWebChromeClient();
    }

    @Override
    public void loadData(VirtualLocationInfoEntity virtualLocationInfoEntity) {
        try {
            //更新默认数据
            mInfoEntity.setLongitude(virtualLocationInfoEntity.getData().getLongitude());
            mInfoEntity.setLatitude(virtualLocationInfoEntity.getData().getLatitude());
            mInfoEntity.setIp((String) virtualLocationInfoEntity.getData().getIp());
            HashMap<String, Object> map = new HashMap<>();
            Map<String, String> objectHashMap = new HashMap<>();
            /*坐标保留4位小数*/
            if (TextUtils.isEmpty(virtualLocationInfoEntity.getData().getLatitude()) || TextUtils.isEmpty(virtualLocationInfoEntity.getData().getLongitude())){
                throw new RuntimeException("未获取到当前位置经纬度");
            }
            String latStr = virtualLocationInfoEntity.getData().getLatitude();
            String lngStr = virtualLocationInfoEntity.getData().getLongitude();
            BigDecimal bdLat = new BigDecimal(latStr);
            BigDecimal bdLng = new BigDecimal(lngStr);
            objectHashMap.put("longitude", bdLng.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
            objectHashMap.put("latitude", bdLat.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
            objectHashMap.put("ip", TextUtils.isEmpty((String) virtualLocationInfoEntity.getData().getIp()) ? "" : (String) virtualLocationInfoEntity.getData().getIp());
            map.put("type", "setGps");
            map.put("data", objectHashMap);
            KLog.e("loadUrl data " + new Gson().toJson(map));
            wbVirtualCenter.loadUrl("javascript:setJingWeiToJs('" + new Gson().toJson(map) + "')");
        } catch (Exception e) {
            KLog.e("e " + e.getMessage());
        }
    }

    /**
     * 基类
     */
    protected class BaseWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isOnReceivedError = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            isOnReceivedError = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //页面加载完成请求数据给到H5
            mPresenter.getData();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
    /**
     * 基类
     */
    protected class BaseWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

    }


    protected void initWebChromeClient() {
        wbVirtualCenter.setWebChromeClient(new BaseWebChromeClient());
    }
    /**
     * js接口基类<br/>
     * 本类包含一些通用的接口
     */
    protected class BaseJsInterface {

        /**
         * 点击事件
         * H5传递过来的参数
         */
        @JavascriptInterface
        public void onClickWebView(String longitude, String latitude, String city) {
            KLog.e("onClickWebView : " + "longitude : " + longitude + ", latitude : " + latitude + ", city : " + city);
            mInfoEntity.setLongitude(longitude);
            mInfoEntity.setLatitude(latitude);
            if(!TextUtils.isEmpty(city) && city.length() > 1 && city.contains("市")) {
                mInfoEntity.setCity(city.substring(0, city.length() - 1));
            }
        }

        @JavascriptInterface
        public void onClickWebViewDetail(String longitude, String latitude, String detail, String City){
            KLog.e("onClickWebViewDetail : " + "longitude : " + longitude + ", latitude : " + latitude + ", detail : " + detail);
            mInfoEntity.setLongitude(longitude);
            mInfoEntity.setLatitude(latitude);
            mInfoEntity.setDetail(detail);
//            mInfoEntity.setCity(City);
        }

        /**
         * 提示
         * H5调用方法
         */
        @JavascriptInterface
        public void setMessage(String message) {
            toastMessage(message);
        }
    }


    @OnClick({R.id.iv_cc_back,R.id.tv_cc_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_cc_back:
                finish();
                break;
            case R.id.tv_cc_confirm:
                if (TextUtils.isEmpty(mInfoEntity.getLongitude()) || TextUtils.isEmpty(mInfoEntity.getLatitude())) {
                    toastMessage("请选择需定位的位置");
                    return;
                }
                mPresenter.startLocation(mInfoEntity.getLongitude(), mInfoEntity.getLatitude(), mInfoEntity.getCity());
                break;
            default:
                break;
        }
    }

    @Override
    public void setResult(){
        Intent result = new Intent(this, VirtualSceneActivity.class);
        result.putExtra("detail_address",mInfoEntity);
        setResult(RESULT_OK,result);
        finish();
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    public void toast(String message) {
        toastMessage(message);
    }

    @Override
    public void dialog(String title, String content, String leftStr, String rightStr) {
        showExpireDialog(title, content, leftStr, rightStr, new MenuCallback() {
            @Override
            public void onLeftButtonClick(Object value) {

            }

            @Override
            public void onRightButtonClick(Object value) {
                //返回上级界面
                finish();
            }
        });
    }
}