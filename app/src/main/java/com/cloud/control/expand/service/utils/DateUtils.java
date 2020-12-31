package com.cloud.control.expand.service.utils;

import android.text.TextUtils;

import com.cloud.control.expand.service.log.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Author：wangwenbin
 * Date：2020/12/30
 * Description： 日期工具
 */
public class DateUtils {

    /**
     * 判断当前时间是否到期
     *
     * @param curTime
     * @param checkTime
     * @return
     */
    public static boolean isExpire(String curTime, String checkTime) {
        boolean isDateExpire = true;
        if (TextUtils.isEmpty(checkTime)) {
            KLog.e("传入的时间参数不能为空");
            return true;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long curTimeMillisecond = dateFormat.parse(curTime).getTime();
            long checkTimeMillisecond = dateFormat.parse(checkTime).getTime();
            if (curTimeMillisecond <= checkTimeMillisecond) {
                isDateExpire = false;
            }
        } catch (ParseException e) {
            KLog.e("请输入正确的时间格式");
            e.printStackTrace();
        }
        return isDateExpire;
    }

}
