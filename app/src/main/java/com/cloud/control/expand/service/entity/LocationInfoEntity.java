package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 传给H5的位置信息
 */
public class LocationInfoEntity {
    private String longitude; //经度
    private String latitude; //维度
    private String ip; //IP
    private String city; //城市

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "LocationInfoEntity{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ip='" + ip + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
