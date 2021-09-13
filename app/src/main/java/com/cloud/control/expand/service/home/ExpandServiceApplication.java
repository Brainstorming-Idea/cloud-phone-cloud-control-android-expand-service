package com.cloud.control.expand.service.home;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.cloud.control.expand.service.BuildConfig;
import com.cloud.control.expand.service.entity.HostBean;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.FileUtils;
import com.cloud.control.expand.service.utils.XMLParser;
import com.cloud.control.expand.service.utils.system.DeviceBoard;
import com.cloud.control.expand.service.utils.system.DeviceUtils;
import com.google.gson.Gson;
import com.vclusters.system.VclustersPSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：应用入口
 */
public class ExpandServiceApplication extends Application {

    private static ExpandServiceApplication sApplication;
    private static Context sContext;
    private VclustersPSystem system = new VclustersPSystem();
    //默认服务器地址，没有配网时使用
    private String buildConfigHost = "http://14.215.128.98:8011/";
    /**
     * 是否为虚拟卡
     */
    public static boolean isVirtual = DeviceUtils.getDeviceBoard().equals(DeviceBoard.VIRTUAL.getType());
    /**
     * 是否为RK Android10
     */
    public static boolean isRKAndroid10 = DeviceUtils.getDeviceBoard().equals(DeviceBoard.RK_ANDROID10.getType());

    @Override
    public void onCreate() {
        super.onCreate();
        //日志初始化
        KLog.init(BuildConfig.IS_DEBUG);
        //获取配网工具服务器地址，动态配置服务器
        String hostUrl = XMLParser.parseXMLForConfigFile("cache/DB_CONFIG_1.xml");
        if(!TextUtils.isEmpty(hostUrl) && hostUrl.contains("ws") && hostUrl.length() > 2) {
            buildConfigHost = "http" + hostUrl.substring(2, hostUrl.length() - 2);
            KLog.e("buildConfigHost = " + buildConfigHost);
        }
        //网络请求初始化
        RetrofitServiceManager.init(buildConfigHost);
//        sContext = getContext();
        sApplication = this;
        hookWebView();
    }

    /**
     * 服务器地址
     * @return
     */
    public String getBuildConfigHost(){
        return buildConfigHost;
    }

//    public static Context getContext() {
//        return sContext;
//    }

    public static ExpandServiceApplication getInstance() {
        return sApplication;
    }

    /**
     * 设备SN
     *
     * @return
     */
    public String getCardSn() {
        if (isVirtual){
            return new Gson().fromJson(FileUtils.readFileContent(ConstantsUtils.VIR_INFO_PATH), HostBean.class).getVirtualSn();
        }else {
            return system.getCardSN();
        }
//        return "RK3930C2301900168";
    }

    /**
     * 在调用webView之前使用此方法，初始化时全局调用一次
     * 解决系统应用调用webView异常问题
     * Caused by: java.lang.UnsupportedOperationException: For security reasons, WebView is not allowed in privileged processes
     */
    public static void hookWebView(){
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                KLog.e("hookWebView:sProviderInstance isn't null");
                return;
            }

            Method getProviderClassMethod;
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                KLog.e("hookWebView:Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
            delegateConstructor.setAccessible(true);
            if(sdkInt < 26){//低于Android O版本
                Constructor<?> providerConstructor = factoryProviderClass.getConstructor(delegateClass);
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance());
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String)chromiumMethodName.get(null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass);
                if (staticFactory!=null){
                    sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance());
                }
            }

            if (sProviderInstance != null){
                field.set("sProviderInstance", sProviderInstance);
                KLog.e("hookWebView:Hook success!");
            } else {
                KLog.e("hookWebView:Hook failed!");
            }
        } catch (Throwable e) {
            KLog.e("hookWebView:" + e.getMessage());
        }
    }

}
