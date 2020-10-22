package com.cloud.control.expand.service.home;

import android.app.Application;
import android.content.Context;

import com.cloud.control.expand.service.BuildConfig;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.retrofit.manager.RetrofitServiceManager;
import com.vclusters.system.VclustersPSystem;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：应用入口
 */
public class ExpandServiceApplication extends Application {

    private static ExpandServiceApplication sApplication;
    private static Context sContext;
    private VclustersPSystem system = new VclustersPSystem();

    @Override
    public void onCreate() {
        super.onCreate();
        //日志初始化
        KLog.init(BuildConfig.IS_DEBUG);
        //网络请求初始化
        RetrofitServiceManager.init();
        sContext = getContext();
        sApplication = this;
    }

    public static Context getContext() {
        return sContext;
    }

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
//        return "RK3930C2301900163";
    }

}
