package com.cloud.control.expand.service.utils;

import com.cloud.control.expand.service.log.KLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 正则表达式工具
 */
public class RegexUtils {

    /**
     * 系统版本号：仅支持输入数字、.
     *
     * @param source
     * @return
     */
    public static boolean checkNumbersAndDots(String source) {
        Pattern mPattern = Pattern.compile("([0-9]|\\.)*");
        Matcher matcher = mPattern.matcher(source);
        return matcher.matches();
    }

    /**
     * IMEI/SN/PSN：仅支持输入数字、大小写字母
     *
     * @param source
     * @return
     */
    public static boolean checkNumbersAndLetter(String source) {
        Pattern mPattern = Pattern.compile("^[0-9A-Za-z]*");
        Matcher matcher = mPattern.matcher(source);
        KLog.e("checkNumbersAndLetter " + matcher.matches());
        KLog.e("checkNumbersAndLetter " + matcher.matches());
        return matcher.matches();
    }

    /**
     * 蓝牙-MAC、WIFI-MAC：仅支持输入数字、大小写字母、：
     *
     * @param source
     * @return
     */
    public static boolean checkNumbersAndLetterColon(String source) {
        Pattern mPattern = Pattern.compile("^[0-9A-Za-z\\:]*");
        Matcher matcher = mPattern.matcher(source);
        return matcher.matches();
    }

    /**
     * 检测蓝牙-MAC、WIFI-MAC格式
     *
     * @param source
     * @return
     */
    public static boolean checkMacFormat(String source) {
        Pattern mPattern = Pattern.compile("([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}");
        Matcher matcher = mPattern.matcher(source);
        KLog.e("checkMacFormat " + matcher.matches());
        return matcher.matches();
    }

}
