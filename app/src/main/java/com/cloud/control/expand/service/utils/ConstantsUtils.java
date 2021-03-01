package com.cloud.control.expand.service.utils;

/**
 * Author：abin
 * Date：2020/11/11
 * Description： 常量工具
 */
public class ConstantsUtils {

    //图片中间拼接地址
    public static final String IMAGE_MIDDLE_URL = "api/upgrade/downlodFile/viewImage/?url=";

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
}
