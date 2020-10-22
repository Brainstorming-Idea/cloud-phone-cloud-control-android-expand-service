package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/16
 * Description： 选中城市状态实体类
 */
public class SelectCityStatusEntity {

    private String city;
    private boolean status;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SelectCityStatusEntity{" +
                "city='" + city + '\'' +
                ", status=" + status +
                '}';
    }
}
