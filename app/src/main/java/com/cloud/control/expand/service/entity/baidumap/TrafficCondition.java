package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 分段路况详情
 * @date :2021/3/10
 */
public class TrafficCondition {
    /**
     * 0： 无路况
     * 1： 畅通
     * 2： 缓行
     * 3： 拥堵
     * 4： 严重拥堵
     */
    private Integer status;//路况指数取值范围

    private Integer geo_cnt;//从当前坐标点开始，path中路况相同的坐标点个数

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGeo_cnt() {
        return geo_cnt;
    }

    public void setGeo_cnt(Integer geo_cnt) {
        this.geo_cnt = geo_cnt;
    }


}