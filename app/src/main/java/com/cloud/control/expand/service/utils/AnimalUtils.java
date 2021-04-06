package com.cloud.control.expand.service.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/18
 */
public class AnimalUtils {
    public static AnimationSet getAlpha(float fromAlpha, float toAlpha) {
        AnimationSet animationSet = new AnimationSet(true);
        //创建 AlphaAnimation 对象
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha,toAlpha);
        //设置动画持续时间
        alphaAnimation.setDuration(2000);
        //添加到AnimationSet
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }
}