package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 经纬度坐标
 * @date :2021/3/8
 */
public class Location {

    //纬度值
    private Float lat;
    //经度值
    private Float lng;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }
}