package com.cloud.control.expand.service.utils;

/**
 * Author：abin
 * Date：2020/11/11
 * Description： 常量工具
 */
public class ConstantsUtils {

    //图片中间拼接地址
    public static final String IMAGE_MIDDLE_URL = "api/upgrade/downlodFile/viewImage/?url=";
    //服务过期
    public static final int SERVICE_EXPIRED_CODE = 30011;

    /**
     * IP切换方式
     */
    public interface IpChangeType {
        int RANDOM_SWITCH_SHORT = 0; //短效全国随机
        int ASSIGN_SWITCH_SHORT = 1; //短效指定城市
        int RANDOM_SWITCH_LONG = 2; //长效全国随机
        int ASSIGN_SWITCH_LONG = 3; //长效指定城市
    }

    public interface OcrParams {
        String MODEL_PATH_DEFAULT = "models/ocr_v1_for_cpu";
        String LABEL_PATH_DEFAULT = "labels/ppocr_keys_v1.txt";
        String IMAGE_PATH_DEFAULT = "images/5.jpg";
        int CPU_THREAD_NUM_DEFAULT = 4;
        String CPU_POWER_MODE_DEFAULT = "LITE_POWER_HIGH";
        String INPUT_COLOR_FORMAT_DEFAULT = "BGR";
        String INPUT_SHAPE_DEFAULT = "1,3,960";
        String INPUT_MEAN_DEFAULT = "0.485, 0.456, 0.406";
        String INPUT_STD_DEFAULT = "0.229,0.224,0.225";
        String SCORE_THRESHOLD_DEFAULT = "0.1";
    }

    public interface BaiDuMap{
        String AK = "KHEvgbAbtRI4l3pT49Nx6GmCm7mwnoO2";
        String GC_COORD_TYPE = "gcj02ll";//国测局坐标
        String BD_MC_COORD_TYPE = "bd09mc";//百度墨卡托坐标
        String BD_COORD_TYPE = "bd09ll";//百度坐标系
        float UNIT_LAT = 30.9f;//一秒纬度对应的距离，米
        int ONE_DEGREE_LAT = 111240;//米
        int RATE = 3;//设置坐标的频率，秒
        int RE_TRY_MAX = 10;//重试最大次数

    }
    public interface SpKey{
        String SP_VS_CONFIG = "vs_config";//虚拟场景的配置数据
    }

    public interface BroadCast{
        String KEY_SERVICE_STATUS = "service_status";
        String SERVICE_CONNECTION_ACTION = "service_connection_action";
    }
}
