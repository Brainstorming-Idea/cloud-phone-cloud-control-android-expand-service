package com.cloud.control.expand.service.utils;


import com.cloud.control.expand.service.log.KLog;

import java.util.Random;

/**
 * Created by：wangwenbin
 * Date：2019/10/15
 * Explain:字符串处理工具类
 */
public class StringUtils {

    private static final String FIXED_NUMBER = "86254841"; //手机IMEI前八位固定值

    /**
     * 生成随机的mac地址
     *
     * @return
     */
    public static String randomMac() {
        return String.format("%02d", new Random().nextInt(99)) + ":" + String.format("%02d", new Random().nextInt(99)) + ":e0:4f:4d:00";
    }

    /**
     * 生成随机的两位数
     *
     * @return
     */
    public static String randomTwoData() {
        return String.format("%02d", new Random().nextInt(99));
    }

    /**
     * 获取随机IMEI
     *
     * @return
     */
    public static String getRandomImei() {
        String imei = FIXED_NUMBER + getRandomNumber(7);
        KLog.e("imei = " + imei);
        return imei;
    }

    /**
     * 获取随机的mac地址
     *
     * @return
     */
    public static String getRandomMacAddress() {
        int maxNum = 16;
        int i;
        int count = 0;
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer buffer = new StringBuffer("");
        Random r = new Random();
        while (count < 12) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                buffer.append(str[i]);
                count++;
            }
        }
        String strMac = getStringAddSymbol(buffer.toString());
        String macAddress = "";
        if (strMac.length() > 1) {
            macAddress = strMac.substring(0, strMac.length() - 1);
            KLog.e("macAddress = " + macAddress);
        }
        return macAddress;
    }

    /**
     * 获取随机生成的序列号
     *
     * @return
     */
    public static String getRandomSysSerialNo() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'S', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer buffer = new StringBuffer("");
        Random r = new Random();
        while (count < 16) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                buffer.append(str[i]);
                count++;
            }
        }
        String sysSerialNo = buffer.toString();
        KLog.e("sysSerialNo = " + sysSerialNo);
        return sysSerialNo;
    }

    /**
     * 每隔两位添加一个符号
     *
     * @param replace
     * @return
     */
    private static String getStringAddSymbol(String replace) {
        String regex = "(.{2})";
        replace = replace.replaceAll(regex, "$1:");
        return replace;
    }

    /**
     * 生成随机数字
     *
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        int maxNum = 10;
        int i;
        int count = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer buffer = new StringBuffer("");
        Random r = new Random();
        while (count < length) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                buffer.append(str[i]);
                count++;
            }
        }
        KLog.e("randomNumber = " + buffer.toString());
        return buffer.toString();
    }

}
