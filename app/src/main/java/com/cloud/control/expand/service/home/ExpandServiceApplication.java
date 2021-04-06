package com.cloud.control.expand.service.home;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.cloud.control.expand.service.BuildConfig;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.cloud.control.expand.service.utils.XMLParser;
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

    @Override
    public void onCreate() {
        super.onCreate();
        //日志初始化
        KLog.init(BuildConfig.IS_DEBUG);
        //获取配网工具服务器地址，动态配置服务器
        String hostUrl = XMLParser.parseXMLForConfigFile("data/data/com.cloud.phone.control.agent/shared_prefs/DB_CONFIG_1.xml");
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
        return system.getCardSN();
//        return "RK3930C2301900168";
    }

    /**
     * 在调用webView之前使用此方法，初始化时全局调用一次
     * 解决系统应用调用webView异常问题
     * Caused by: java.lang.UnsupportedOperationException: For security reasons, WebView is not allowed in privileged processes
     */
    private void hookWebView() {
        Class<?> factoryClass = null;
        try {
            factoryClass = Class.forName("android.webkit.WebViewFactory");
            Method getProviderClassMethod = null;
            Object sProviderInstance = null;

            if (Build.VERSION.SDK_INT == 25) { //Android 7.1
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
                getProviderClassMethod.setAccessible(true);
                Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
                Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
                Constructor<?> constructor = providerClass.getConstructor(delegateClass);
                if (constructor != null) {
                    constructor.setAccessible(true);
                    Constructor<?> constructor2 = delegateClass.getDeclaredConstructor();
                    constructor2.setAccessible(true);
                    sProviderInstance = constructor.newInstance(constructor2.newInstance());
                }
            } else if (Build.VERSION.SDK_INT == 25) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
                getProviderClassMethod.setAccessible(true);
                Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
                Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
                Constructor<?> constructor = providerClass.getConstructor(delegateClass);
                if (constructor != null) {
                    constructor.setAccessible(true);
                    Constructor<?> constructor2 = delegateClass.getDeclaredConstructor();
                    constructor2.setAccessible(true);
                    sProviderInstance = constructor.newInstance(constructor2.newInstance());
                }
            } else if (Build.VERSION.SDK_INT == 21) {//Android 21无WebView安全限制
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
                getProviderClassMethod.setAccessible(true);
                Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
                sProviderInstance = providerClass.newInstance();
            }
            if (sProviderInstance != null) {
                KLog.e("hookWebView " + sProviderInstance.toString());
                Field field = factoryClass.getDeclaredField("sProviderInstance");
                field.setAccessible(true);
                field.set("sProviderInstance", sProviderInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
