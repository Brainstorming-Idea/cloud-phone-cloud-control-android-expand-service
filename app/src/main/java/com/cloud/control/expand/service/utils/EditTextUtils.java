package com.cloud.control.expand.service.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：abin
 * Date：2020/10/20
 * Description： 输入框文字格式
 */
public class EditTextUtils {

    /**
     * 仅支持输入数字、.
     *
     * @param editText
     */
    public static void setEditTextNumberAndDot(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                }
                String speChat = "[\\u4E00-\\u9FA5\\u0020`~✘!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，\\-、_/\\---？abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});

    }

    /**
     * 仅支持输入数字、大小写字母
     *
     * @param editText
     */
    public static void setEditTextNumberAndLetter(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                }
                String speChat = "[\\u4E00-\\u9FA5\\u0020`~✘!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，\\-、_/\\---？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});

    }

    /**
     * 仅支持输入数字、大小写字母、：
     *
     * @param editText
     */
    public static void setEditTextNumberAndLetterAndColon(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                }
                String speChat = "[\\u4E00-\\u9FA5\\u0020`~✘!@#$%^&*()+=|{}';',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，\\-、_/\\---？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});

    }
}
