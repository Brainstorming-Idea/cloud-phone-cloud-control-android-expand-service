package com.cloud.control.expand.service.entity.baidumap;

import java.util.ArrayList;

/**
 * @author wangyou
 * @desc: 路线方案集
 * @date :2021/3/10
 */
public class Routes {
    private Long distance;// 方案距离，单位：米
    private Long duration;//线路耗时，单位：秒
    private ArrayList<Steps> steps;//路线分段

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }
}