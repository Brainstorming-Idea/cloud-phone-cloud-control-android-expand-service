package com.cloud.control.expand.service.module.virtualscene;

/**
 * @author wangyou
 * @desc: V -> P
 * @date :2021/3/4
 */
public interface IVirtualScene {

    /**
     * 获取中心点位置
     */
    void getCenterLoc();

    /**
     * 获取终点坐标
     * @param radius
     * @return
     */
    double[] getTerminalPoint(int radius);
} 