package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 地址组成
 * @date :2021/4/7
 */
public class AddressComponent {
    private String country;
    private Integer country_code;
    private String province;
    private String city;
    private Integer city_level;
    private String district;//区县名
    private String town;//乡镇名
    private String street;//街道名
    private String street_number;//街道门牌号
    private Integer adcode;//行政区划代码
    private String addr;//地址信息

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCountry_code() {
        return country_code;
    }

    public void setCountry_code(Integer country_code) {
        this.country_code = country_code;
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

    public Integer getCity_level() {
        return city_level;
    }

    public void setCity_level(Integer city_level) {
        this.city_level = city_level;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public Integer getAdcode() {
        return adcode;
    }

    public void setAdcode(Integer adcode) {
        this.adcode = adcode;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}