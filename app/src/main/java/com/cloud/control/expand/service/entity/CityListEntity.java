package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 城市列表实体类
 */
public class CityListEntity {

    /**
     * status : 0
     * msg : 获取城市列表成功
     * data : {"selectedCity":["万州区","三明"],"cityList":["万州区","三明","上饶","东莞","中山","临沧","丹东","丽水"]}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> selectedCity;
        private List<String> cityList;

        public List<String> getSelectedCity() {
            return selectedCity;
        }

        public void setSelectedCity(List<String> selectedCity) {
            this.selectedCity = selectedCity;
        }

        public List<String> getCityList() {
            return cityList;
        }

        public void setCityList(List<String> cityList) {
            this.cityList = cityList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "selectedCity=" + selectedCity +
                    ", cityList=" + cityList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CityListEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
