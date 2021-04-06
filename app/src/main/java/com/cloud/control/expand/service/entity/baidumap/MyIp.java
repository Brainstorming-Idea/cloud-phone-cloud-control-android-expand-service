package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 芝麻代理获取本机IP的的返回信息
 * @date :2021/3/8
 */
public class MyIp {
//    {"ip":"119.139.198.238","country":"中国","area":"0","province":"广东省","city":"深圳市","isp":"电信","timestamp":1615189282}
    private String ip;
    private String country;
    private String area;
    private String province;
    private String city;
    private String isp;
    private Long timestamp;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
